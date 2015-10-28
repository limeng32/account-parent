package limeng32.mirage.account.captcha;

public class AccountCaptchaException extends Exception {

	private static final long serialVersionUID = 1L;

	public AccountCaptchaException(String message) {
		super(message);
	}

	public AccountCaptchaException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
