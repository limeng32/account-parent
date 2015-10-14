package limeng32.mirage.account.email;

public class AccountEmailException extends Exception {
	private static final long serialVersionUID = 1L;

	public AccountEmailException(String message) {
		super(message);
	}

	public AccountEmailException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
