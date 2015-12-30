package limeng32.mirage.account.service;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.LoginLog;

public interface AccountService {

	String generateCaptchaKeyNew(String remoteIP)
			throws AccountServiceException;

	byte[] generateCaptchaImageNew(String captchaText)
			throws AccountServiceException;

	void signUpNew(Account account, String captchaValue, String remoteIP)
			throws AccountServiceException;

	void activate(String activationKey, String activationValue)
			throws AccountServiceException;

	boolean validateCaptchaNew(String remoteIP, String captchaValue)
			throws AccountServiceException;

	boolean checkCaptcha(String remoteIP, String captchaValue)
			throws AccountServiceException;

	Account login(String email, String password, String remoteAddress)
			throws AccountServiceException;

	boolean test(String email, String password) throws AccountServiceException;

	void insertAccountTransactive(Account account)
			throws AccountServiceException;

	void insertLoginLogTransactive(LoginLog loginLog)
			throws AccountServiceException;

	boolean checkExist(String email) throws AccountServiceException;

	boolean checkUnique(String email) throws AccountServiceException;

}
