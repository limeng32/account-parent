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
	public String generateCaptchaKey() throws AccountServiceException {
		try {
			return accountCaptchaService.generateCaptchaKey();
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException(
					"Unable to generate Captcha key.", e);
		}
	}

	@Override
	public byte[] generateCaptchaImage(String captchaKey)
			throws AccountServiceException {
		try {
			return accountCaptchaService.generateCaptchaImage(captchaKey);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException(
					"Unable to generate Captcha Image.", e);
		}
	}

	@Override
	public void signUp(Account account, String captchaKey, String captchaValue,
			String activateServiceUrl) throws AccountServiceException {
		String originPassword = account.getPassword();
		try {
			if (!accountCaptchaService
					.validateCaptcha(captchaKey, captchaValue)) {
				throw new AccountServiceException("Incorrect Captcha.");
			}
			account.setPassword(DigestUtils.md5Hex(account.getPassword()));
			accountPersistService.insert(account);
			String activationId = RandomGenerator.getRandomString();
			account.setActivateValue(activationId);
			accountPersistService.update(account);
			String link = activateServiceUrl.endsWith("/") ? activateServiceUrl
					: activateServiceUrl + "?";
			link += "k=" + account.getEmail() + "&v=" + activationId;
			accountEmailService.sendMail(account.getEmail(),
					"Please Activate Your Account", link);
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
				System.out.println(":"+AccountServiceExceptionEnum.YourAccountNeedActivate
								.toString());
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

}
