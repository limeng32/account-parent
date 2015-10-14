package limeng32.mirage.account.email;

import static junit.framework.Assert.assertEquals;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import limeng32.mirage.account.email.AccountEmailException;
import limeng32.mirage.account.email.AccountEmailService;

import org.junit.After;
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
@ContextConfiguration("classpath:account-email.xml")
public class AccountEmailServiceTest {

	private GreenMail greenMail;

	@Autowired
	private AccountEmailService accountEmailService;

	public static final String sendFrom = "test1@limeng32.com";

	@Before
	public void startMailServer() {
		greenMail = new GreenMail(ServerSetupTest.SMTP);
		greenMail.start();
	}

	@Test
	public void testSendMail() throws AccountEmailException,
			InterruptedException, MessagingException {
		String sendTo = "test2@limeng32.com";
		String subject = "Test Subject";
		String htmlText = "<h3>Test</h3>";
		accountEmailService.sendMail(sendTo, subject, htmlText);

		greenMail.waitForIncomingEmail(2000, 1);

		Message[] msgs = greenMail.getReceivedMessages();
		assertEquals(1, msgs.length);
		assertEquals(subject, msgs[0].getSubject());
		assertEquals(htmlText, GreenMailUtil.getBody(msgs[0]).trim());
		assertEquals(sendFrom, (msgs[0].getFrom()[0]).toString());
		assertEquals(sendTo,
				(msgs[0].getRecipients(RecipientType.TO)[0]).toString());
	}

	@After
	public void stopMailServer() {
		greenMail.stop();
	}

}
