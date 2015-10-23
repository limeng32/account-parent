package limeng32.mirage.account.service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

import limeng32.mirage.account.captcha.AccountCaptchaService;
import limeng32.mirage.account.captcha.RandomGenerator;
import limeng32.mirage.account.email.AccountEmailService;
import limeng32.mirage.account.persist.Account;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
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

	@Before
	public void prepare() throws Exception {

		List<String> preDefinedTexts = new ArrayList<String>();
		preDefinedTexts.add("abcde");
		accountCaptchaService.setPreDefinedTexts(preDefinedTexts);

		greenMail = new GreenMail(ServerSetupTest.SMTP);
		greenMail.start();
	}

	@Test
	public void testAccountService() throws Exception {

		// 1. Get captcha
		String captchaKey = accountService.generateCaptchaKey();
		accountService.generateCaptchaImage(captchaKey);
		String captchaValue = "abcde";

		// 2. Submit sign up Request
		Account account = new Account();
		account.setName(RandomGenerator.getRandomString(6));
		account.setEmail(RandomGenerator.getRandomString(8) + "@live.cn");
		account.setPassword(RandomGenerator.getRandomString(8));
		account.setActivated(false);
		accountService.signUp(account, captchaKey, captchaValue,
				"http://localhost:8080/account/activate");

		// 3. Read activation link
		greenMail.waitForIncomingEmail(2000, 1);
		Message[] msgs = greenMail.getReceivedMessages();
		Assert.assertEquals(1, msgs.length);
		Assert.assertEquals("Please Activate Your Account",
				msgs[0].getSubject());
		String activationLink = GreenMailUtil.getBody(msgs[0]).trim();

		// 3a. Try login but not activated
		try {
			Account a = accountService.login(account.getEmail(),
					account.getPassword());
			Assert.assertNotNull(a);
			Assert.assertFalse(a.getActivated());
		} catch (AccountServiceException e) {
		}

		// 4. Activate account
		String activationValue = activationLink.substring(activationLink
				.lastIndexOf("=") + 1);
		activationLink = activationLink.substring(0,
				activationLink.lastIndexOf("&"));
		String activationKey = activationLink.substring(activationLink
				.lastIndexOf("=") + 1);
		accountService.activate(activationKey, activationValue);

		// 5. Login with correct id and password
		Account a2 = accountService.login(account.getEmail(),
				account.getPassword());
		Assert.assertTrue(a2.getActivated());

		// 5a. Login with incorrect password
		try {
			accountService.login(account.getEmail(), "admin456");
		} catch (AccountServiceException e) {
			Assert.assertEquals("Email or password is not exist.",
					e.getMessage());
		}
	}

	@After
	public void stopMailServer() throws Exception {
		greenMail.stop();
	}
}
