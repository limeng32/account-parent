package limeng32.mirage.account.service;

public class AccountServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public static final String EmailOrPasswordIsNotExist = "Email or password is not exist.";

	public static final String YourAccountHasProblem = "You account has problem. Please contact the admin.";

	public static final String YourAccountNeedActivate = "You account need activate.";

	public static final String ActivateFail = "The activate fail for some reason. Please contact the admin.";

	public static final String ActivateMismatch = "Invalid activationKey or activationValue.";

	public static final String ActivateRepetition = "Your email has been activated already.";

	public AccountServiceException(String message) {
		super(message);
	}

	public AccountServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
