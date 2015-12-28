package limeng32.mirage.account.service;

import java.util.Collection;

import limeng32.mirage.account.captcha.AccountCaptchaException;
import limeng32.mirage.account.captcha.AccountCaptchaService;
import limeng32.mirage.account.captcha.RandomGenerator;
import limeng32.mirage.account.email.AccountEmailException;
import limeng32.mirage.account.email.AccountEmailService;
import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountPersistService;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountPersistService accountPersistService;

	@Autowired
	private AccountEmailService accountEmailService;

	@Autowired
	private AccountCaptchaService accountCaptchaService;

	@Autowired
	private AccountServiceConfig accountServiceConfig;

	@Override
	public void activate(String activationKey, String activationValue)
			throws AccountServiceException {
		if (activationKey == null || activationValue == null) {
			throw new AccountServiceException(
					"ActivationKey or activationValue is Null.");
		}
		Account ac = new Account();
		ac.setEmail(activationKey);
		ac.setActivateValue(activationValue);
		Collection<Account> accountC = accountPersistService.selectAll(ac);
		switch (accountC.size()) {
		case 1:
			Account account = accountC.toArray(new Account[1])[0];
			if (account.getActivated()) {
				throw new AccountServiceException(
						AccountServiceExceptionEnum.ActivateRepetition
								.toString());
			}
			account.setActivated(true);
			account.setActivateValue("");
			accountPersistService.update(account);
			break;
		case 0:
			throw new AccountServiceException(
					AccountServiceExceptionEnum.ActivateMismatch.toString());
		default:
			throw new AccountServiceException(
					AccountServiceExceptionEnum.ActivateFail.toString());
		}
	}

	@Override
	public Account login(String email, String password)
			throws AccountServiceException {
		if (email == null || password == null) {
			throw new AccountServiceException("Email or password is Null.");
		}
		Account ac = new Account();
		ac.setEmail(email);
		ac.setPassword(DigestUtils.md5Hex(password));
		Collection<Account> accountC = accountPersistService.selectAll(ac);
		switch (accountC.size()) {
		case 1:
			Account account = accountC.toArray(new Account[1])[0];
			if (account.getActivated()) {
				return account;
			} else {
				throw new AccountServiceException(
						AccountServiceExceptionEnum.YourAccountNeedActivate
								.toString());
			}
		case 0:
			throw new AccountServiceException(
					AccountServiceExceptionEnum.EmailOrPasswordIsNotExist
							.toString());
		default:
			throw new AccountServiceException(
					AccountServiceExceptionEnum.YourAccountHasProblem
							.toString());
		}
	}

	@Override
	public void signUpNew(Account account, String captchaValue, String remoteIP)
			throws AccountServiceException {
		String originPassword = account.getPassword();
		try {
			if (!accountCaptchaService.validateCaptchaNew(remoteIP,
					captchaValue)) {
				throw new AccountServiceException("Incorrect Captcha.");
			}

			String activationId = RandomGenerator.getRandomString();
			account.setActivateValue(activationId);
			account.setActivated(false);
			account.setPassword(DigestUtils.md5Hex(account.getPassword()));
			if (account.getName() == null) {
				account.setName(account.getEmail().substring(0,
						account.getEmail().indexOf("@")));
			}
			accountPersistService.insert(account);
			String link = accountServiceConfig.getActivateUrl().endsWith("/") ? accountServiceConfig
					.getActivateUrl() + account.getEmail() + "/" + activationId
					: accountServiceConfig.getActivateUrl() + "?k="
							+ account.getEmail() + "&v=" + activationId;
			accountEmailService.sendMail(account.getEmail(),
					accountServiceConfig.getActivateEmailSubject(),
					accountServiceConfig.getActivateEmailBody() + "<a href='"
							+ link + "'>" + link + "</a>");
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to validate captcha.", e);
		} catch (AccountEmailException e) {
			throw new AccountServiceException(
					"Unable to send actiavtion mail.", e);
		} finally {
			account.setPassword(originPassword);
		}
	}

	@Override
	public boolean test(String email, String password)
			throws AccountServiceException {
		Account ac = new Account();
		ac.setEmail(email);
		ac.setPassword(DigestUtils.md5Hex(password));
		int count = accountPersistService.count(ac);
		switch (count) {
		case 1:
			return true;
		case 0:
			return false;
		default:
			return true;
		}
	}

	@Override
	@Transactional(rollbackFor = { AccountServiceException.class }, readOnly = false, propagation = Propagation.REQUIRED)
	public void transactiveInsert(Account account)
			throws AccountServiceException {
		accountPersistService.insert(account);
		Account ac = new Account();
		ac.setEmail(account.getEmail());
		int c = accountPersistService.count(ac);
		if (c > 1) {
			throw new AccountServiceException("Repetition email.");
		}
	}

	@Override
	public String generateCaptchaKeyNew(String remoteIP)
			throws AccountServiceException {
		try {
			return accountCaptchaService.generateCaptchaKeyNew(remoteIP);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException(
					"Unable to generate Captcha Key.", e);
		}
	}

	@Override
	public byte[] generateCaptchaImageNew(String captchaText)
			throws AccountServiceException {
		try {
			return accountCaptchaService.generateCaptchaImageNew(captchaText);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException(
					"Unable to generate Captcha Image.", e);
		}
	}

	@Override
	public boolean validateCaptchaNew(String remoteIP, String captchaValue)
			throws AccountServiceException {
		try {
			return accountCaptchaService.validateCaptchaNew(remoteIP,
					captchaValue);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to validate Captcha.", e);
		}
	}

	@Override
	public boolean checkCaptcha(String remoteIP, String captchaValue)
			throws AccountServiceException {
		try {
			return accountCaptchaService.checkCaptcha(remoteIP, captchaValue);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to check Captcha.", e);
		}
	}

	@Override
	public boolean checkExist(String email) throws AccountServiceException {
		Account ac = new Account();
		ac.setEmail(email);
		int count = accountPersistService.count(ac);
		boolean result = false;
		switch (count) {
		case 1:
			result = true;
			break;
		case 0:
			result = false;
			break;
		default:
			result = true;
			break;
		}
		return result;
	}

	@Override
	public boolean checkUnique(String email) throws AccountServiceException {
		Account ac = new Account();
		ac.setEmail(email);
		int count = accountPersistService.count(ac);
		boolean result = false;
		if (count == 0) {
			result = true;
		}
		return result;
	}

}
