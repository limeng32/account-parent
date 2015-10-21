package limeng32.mirage.account.service;

import java.util.HashMap;
import java.util.Map;

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

	private Map<String, Integer> activationMap = new HashMap<>();

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
	public void signUp(SignUpRequest signUpRequest)
			throws AccountServiceException {
		try {

			if (!accountCaptchaService.validateCaptcha(
					signUpRequest.getCaptchaKey(),
					signUpRequest.getCaptchaValue())) {

				throw new AccountServiceException("Incorrect Captcha.");
			}

			Account account = new Account();
			account.setEmail(signUpRequest.getEmail());
			account.setName(signUpRequest.getName());
			account.setPassword(signUpRequest.getPassword());
			account.setActivated(false);

			accountPersistService.insert(account);

			String activationId = RandomGenerator.getRandomString();

			activationMap.put(activationId, account.getId());

			String link = signUpRequest.getActivateServiceUrl().endsWith("/") ? signUpRequest
					.getActivateServiceUrl() + activationId
					: signUpRequest.getActivateServiceUrl() + "?key="
							+ activationId;

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
	public void activate(String activationId) throws AccountServiceException {
		Integer accountId = activationMap.get(activationId);

		if (accountId == null) {
			throw new AccountServiceException("Invalid account activation ID.");
		}

		Account account = accountPersistService.select(accountId);
		account.setActivated(true);
		accountPersistService.update(account);

	}

	@Override
	public Account login(Integer id) throws AccountServiceException {
		try {
			return accountPersistService.select(id);
		} catch (Exception e) {
			throw new AccountServiceException("Unable to login.", e);
		}

	}

}
