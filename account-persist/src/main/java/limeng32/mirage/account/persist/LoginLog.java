package limeng32.mirage.account.persist;

public class LoginLog {

	public int id;
	public java.util.Date loginTime;
	public java.lang.String loginIP;

	public Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account newAccount) {
		if (this.account == null || !this.account.equals(newAccount)) {
			if (this.account != null) {
				Account oldAccount = this.account;
				this.account = null;
				oldAccount.removeLoginLog(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addLoginLog(this);
			}
		}
	}

}