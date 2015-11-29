package limeng32.mirage.account.web;

public enum AccountWebEnum {

	ActivateSuccess("恭喜您激活成功，现在您可以使用这个邮箱登录了");

	private final String description;

	private AccountWebEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

}
