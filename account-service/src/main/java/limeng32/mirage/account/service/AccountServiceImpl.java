package limeng32.mirage.account.service;

import java.util.Collection;

import limeng32.mirage.account.captcha.AccountCaptchaException;
import limeng32.mirage.account.captcha.AccountCaptchaService;
import limeng32.mirage.account.captcha.RandomGenerator;
import limeng32.mirage.account.email.AccountEmailException;
import limeng32.mirage.account.email.AccountEmailService;
import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountPersistService;

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
		try {
			if (!accountCaptchaService
					.validateCaptcha(captchaKey, captchaValue)) {
				throw new AccountServiceException("Incorrect Captcha.");
			}

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
			account.setActivated(true);
			account.setActivateValue("");
			accountPersistService.update(account);
			break;
		case 0:
			throw new AccountServiceException(
					"Invalid activationKey or activationValue.");
		default:
			throw new AccountServiceException("Unable to activate.");
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
		ac.setPassword(password);
		Collection<Account> accountC = accountPersistService.selectAll(ac);
		switch (accountC.size()) {
		case 1:
			return accountC.toArray(new Account[1])[0];
		case 0:
			throw new AccountServiceException("Email or password is not exist.");
		default:
			throw new AccountServiceException(
					"You account has problem. Please contact the admin.");
		}
	}
}
