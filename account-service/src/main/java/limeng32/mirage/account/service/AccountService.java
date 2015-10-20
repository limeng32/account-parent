package limeng32.mirage.account.service;

import javax.servlet.http.HttpServletRequest;

public interface AccountService {
	String generateCaptchaKey() throws AccountServiceException;

	byte[] generateCaptchaImage(String captchaKey)
			throws AccountServiceException;

	void signUp(HttpServletRequest signUpRequest)
			throws AccountServiceException;

	void activate(String activationNumber) throws AccountServiceException;

	void login(String id, String password) throws AccountServiceException;
}
