package limeng32.mirage.account.persist;

public class AccountPersistException extends Exception {
	private static final long serialVersionUID = 1L;

	public AccountPersistException(String message) {
		super(message);
	}

	public AccountPersistException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
