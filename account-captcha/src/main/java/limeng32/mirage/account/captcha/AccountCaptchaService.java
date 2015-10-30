package limeng32.mirage.account.captcha;

import java.util.List;

public interface AccountCaptchaService {

	String generateCaptchaKey() throws AccountCaptchaException;

	String generateCaptchaKeyNew(String remoteIP)
			throws AccountCaptchaException;

	byte[] generateCaptchaImageNew(String captchaText)
			throws AccountCaptchaException;

	boolean validateCaptchaNew(String remoteIP, String captchaValue)
			throws AccountCaptchaException;

	byte[] generateCaptchaImage(String captchaKey)
			throws AccountCaptchaException;

	boolean validateCaptcha(String captchaKey, String captchaValue)
			throws AccountCaptchaException;

	List<String> getPreDefinedTexts();

	void setPreDefinedTexts(List<String> preDefinedTexts);

}
