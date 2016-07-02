package limeng32.mirage.account.service;

public enum AccountServiceExceptionEnum {

	EmailOrPasswordIsNotExist("您输入的帐号或者密码并不存在。"), YourAccountHasProblem(
			"您的帐号出现问题，现在无法登陆，具体情况请联系管理员。"), YourAccountNeedActivate(
			"您的帐号还没有激活，请登录您的注册邮箱进行激活。"), ActivateFail("您的邮箱激活失败，请与管理员联系。"), ActivateMismatch(
			"您的邮箱与激活码不匹配，未能完成激活。"), ActivateRepetition("您的邮箱已经是激活状态，无需再次激活。"), RepetitionEmail(
			"您的邮箱已经注册过，无法再次注册"), CannotFindLoginLog("您的帐号找不到登录日志，现在无法登录"), FailedToInsertLoginLog(
			"为新帐号增加登录日志时发生异常"), CannotFindAccount("无法找到您的帐号");

	private final String description;

	private AccountServiceExceptionEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}
}
