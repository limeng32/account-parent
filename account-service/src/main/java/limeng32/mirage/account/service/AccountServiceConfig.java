package limeng32.mirage.account.service;

public class AccountServiceConfig {

	private String activateUrl;

	private String activateEmailBody;

	private String activateEmailSubject;

	public String getActivateUrl() {
		return activateUrl;
	}

	public void setActivateUrl(String activateUrl) {
		this.activateUrl = activateUrl;
	}

	public String getActivateEmailBody() {
		return activateEmailBody;
	}

	public void setActivateEmailBody(String activateEmailBody) {
		this.activateEmailBody = activateEmailBody;
	}

	public String getActivateEmailSubject() {
		return activateEmailSubject;
	}

	public void setActivateEmailSubject(String activateEmailSubject) {
		this.activateEmailSubject = activateEmailSubject;
	}

}
