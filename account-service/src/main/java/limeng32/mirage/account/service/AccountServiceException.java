package limeng32.mirage.account.service;

public class AccountServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public AccountServiceException(String message) {
		super(message);
	}

	public AccountServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
