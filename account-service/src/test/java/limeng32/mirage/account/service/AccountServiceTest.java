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
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:account-service-test.xml" })
public class AccountServiceTest {
	private GreenMail greenMail;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountEmailService accountEmailService;

	@Autowired
	private AccountCaptchaService accountCaptchaService;

	@Autowired
	private AccountServiceConfig accountServiceConfig;

	@Before
	public void prepare() throws Exception {

		List<String> preDefinedTexts = new ArrayList<String>();
		preDefinedTexts.add("abcde");
		accountCaptchaService.setPreDefinedTexts(preDefinedTexts);

		greenMail = new GreenMail(ServerSetupTest.SMTP);
		greenMail.start();
	}

	@Test
	@IfProfileValue(name = "VOLATILE", value = "true")
	public void testAccountService() throws Exception {

		// 1. Get captcha
		String remoteIP = "0.0.0.1";
		String captchaKey = accountService.generateCaptchaKeyNew(remoteIP);
		accountService.generateCaptchaImageNew(captchaKey);
		// String captchaValue = "abcde";

		// 2. Submit sign up Request
		Account account = new Account();
		account.setName(RandomGenerator.getRandomString(6));
		account.setEmail(RandomGenerator.getRandomString(8) + "@live.cn");
		account.setPassword(RandomGenerator.getRandomString(8));
		account.setActivated(false);
		accountService.signUpNew(account, captchaKey, remoteIP);

		// 3. Read activation link
		greenMail.waitForIncomingEmail(2000, 1);
		Message[] msgs = greenMail.getReceivedMessages();
		Assert.assertEquals(1, msgs.length);
		Assert.assertEquals("Welcome to Mirage!", msgs[0].getSubject());
		String activationLink = GreenMailUtil.getBody(msgs[0]).trim();
		// 3a. Try login but not activated
		try {
			Account a = accountService.login(account.getEmail(),
					account.getPassword(), remoteIP);
			Assert.assertNotNull(a);
			Assert.assertFalse(a.getActivated());
		} catch (AccountServiceException e) {
		}

		// 3b. Test AccountServiceConfig
		Assert.assertNotNull(accountServiceConfig.getActivateEmailBody());
		Assert.assertNotNull(accountServiceConfig.getActivateEmailSubject());
		Assert.assertNotNull(accountServiceConfig.getActivateUrl());

		// 4. Activate account
		String activationValue = activationLink.substring(
				activationLink.lastIndexOf("=") + 1,
				activationLink.lastIndexOf("<"));
		activationLink = activationLink.substring(0,
				activationLink.lastIndexOf("&"));
		String activationKey = activationLink.substring(activationLink
				.lastIndexOf("=") + 1);
		accountService.activate(activationKey, activationValue);

		// 5. Login with correct id and password
		Account a2 = accountService.login(account.getEmail(),
				account.getPassword(), remoteIP);
		Assert.assertTrue(a2.getActivated());

		// 5a. Login with incorrect password
		try {
			accountService.login(account.getEmail(), "admin456", remoteIP);
		} catch (AccountServiceException e) {
			Assert.assertEquals(
					AccountServiceExceptionEnum.EmailOrPasswordIsNotExist
							.toString(), e.getMessage());
		}
	}

	@After
	public void stopMailServer() throws Exception {
		greenMail.stop();
	}
}
