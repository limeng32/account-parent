package limeng32.mirage.account.service;

import limeng32.mirage.account.persist.Account;

public interface AccountService {
	String generateCaptchaKey() throws AccountServiceException;

	byte[] generateCaptchaImage(String captchaKey)
			throws AccountServiceException;

	void signUp(SignUpRequest signUpRequest) throws AccountServiceException;

	void activate(String activationNumber) throws AccountServiceException;

	Account login(Integer id) throws AccountServiceException;
}
