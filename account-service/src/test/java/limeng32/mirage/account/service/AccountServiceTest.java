package limeng32.mirage.account.service;

import java.util.ArrayList;
import java.util.List;

import limeng32.mirage.account.captcha.AccountCaptchaService;
import limeng32.mirage.account.email.AccountEmailService;
import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountPersistService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:account-email.xml",
		"classpath:account-persist.xml", "classpath:account-captcha.xml",
		"classpath:account-service.xml" })
public class AccountServiceTest {
	private GreenMail greenMail;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountEmailService accountEmailService;

	@Autowired
	private AccountCaptchaService accountCaptchaService;

	@Autowired
	private AccountPersistService accountPersistService;

	private MockHttpServletRequest request;

	@Before
	public void prepare() throws Exception {

		List<String> preDefinedTexts = new ArrayList<String>();
		preDefinedTexts.add("abcde");
		preDefinedTexts.add("12345");
		accountCaptchaService.setPreDefinedTexts(preDefinedTexts);

		greenMail = new GreenMail(ServerSetupTest.SMTP);
		// greenMail.setUser("test@juvenxu.com", "123456");
		greenMail.start();

		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
	}

	@Test
	public void testAccountService() throws Exception {
		// 0. Test AccountEmailService
		String sendTo = "test2@limeng32.com";
		String subject = "Test Subject";
		String htmlText = "<h3>Test</h3>";
		accountEmailService.sendMail(sendTo, subject, htmlText);

		// 1. Get captcha
		String captchaKey = accountService.generateCaptchaKey();
		accountService.generateCaptchaImage(captchaKey);
		String captchaValue = "abcde";

		// 1a. Test AccountPersistService
		Assert.assertNotNull(accountPersistService.select(1));

		// 1b. Test AccountService.login
		Assert.assertNotNull(accountService.login(1));

		// 2. Submit sign up Request
		Account account = new Account();
		account.setName("limeng0");
		account.setEmail("limeng32@live.cn");
		account.setPassword("admin123");
		accountService.signUp(account, captchaKey, captchaValue,
				"http://localhost:8080/account/activate");
		//
		// // 3. Read activation link
		// greenMail.waitForIncomingEmail(2000, 1);
		// Message[] msgs = greenMail.getReceivedMessages();
		// Assert.assertEquals(1, msgs.length);
		// Assert.assertEquals("Please Activate Your Account",
		// msgs[0].getSubject());
		// String activationLink = GreenMailUtil.getBody(msgs[0]).trim();

		// // 3a. Try login but not activated
		// try
		// {
		// accountService.login( "juven", "admin123" );
		// fail( "Disabled account shouldn't be able to log in." );
		// }
		// catch ( AccountServiceException e )
		// {
		// }
		//
		// // 4. Activate account
		// String activationCode = activationLink.substring(
		// activationLink.lastIndexOf( "=" ) + 1 );
		// accountService.activate( activationCode );
		//
		// // 5. Login with correct id and password
		// accountService.login( "juven", "admin123" );
		//
		// // 5a. Login with incorrect password
		// try
		// {
		// accountService.login( "juven", "admin456" );
		// fail( "Password is incorrect, shouldn't be able to login." );
		// }
		// catch ( AccountServiceException e )
		// {
		// }

	}

	@After
	public void stopMailServer() throws Exception {
		greenMail.stop();
	}
}
