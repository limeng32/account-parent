package limeng32.mirage.account.service;

public class AccountServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public static final String MessageEmailOrPasswordIsNotExist = "Email or password is not exist.";

	public static final String MessageYouAccountHasProblem = "You account has problem. Please contact the admin.";

	public AccountServiceException(String message) {
		super(message);
	}

	public AccountServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
