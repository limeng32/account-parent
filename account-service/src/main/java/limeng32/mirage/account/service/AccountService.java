package limeng32.mirage.account.service;

import limeng32.mirage.account.persist.Account;

public interface AccountService {
	String generateCaptchaKey() throws AccountServiceException;

	byte[] generateCaptchaImage(String captchaKey)
			throws AccountServiceException;

	void signUp(Account account, String captchaKey, String captchaValue,
			String activateServiceUrl) throws AccountServiceException;

	void signUpNew(Account account, String captchaValue, String remoteIP)
			throws AccountServiceException;

	void activate(String activationKey, String activationValue)
			throws AccountServiceException;

	Account login(String email, String password) throws AccountServiceException;

	boolean test(String email, String password) throws AccountServiceException;
}
