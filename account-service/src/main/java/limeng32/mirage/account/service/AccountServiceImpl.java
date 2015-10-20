package limeng32.mirage.account.service;

import javax.servlet.http.HttpServletRequest;

public class AccountServiceImpl implements AccountService {

	@Override
	public String generateCaptchaKey() throws AccountServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] generateCaptchaImage(String captchaKey)
			throws AccountServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void signUp(HttpServletRequest signUpRequest)
			throws AccountServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate(String activationNumber)
			throws AccountServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void login(String id, String password)
			throws AccountServiceException {
		// TODO Auto-generated method stub

	}

}
