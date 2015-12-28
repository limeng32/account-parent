package limeng32.mirage.account.captcha;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:account-captcha.xml")
public class AccountCaptchaServiceTest {

	@Autowired
	private AccountCaptchaService service;

	@Test
	public void testGenerateCaptcha() throws Exception {
		String captchaKey = service.generateCaptchaKeyNew("1");
		assertNotNull(captchaKey);

		byte[] captchaImage = service.generateCaptchaImageNew(captchaKey);
		assertTrue(captchaImage.length > 0);

		File image = new File("target/" + captchaKey + ".jpg");
		OutputStream output = null;
		try {
			output = new FileOutputStream(image);
			output.write(captchaImage);
		} finally {
			if (output != null) {
				output.close();
			}
		}
		assertTrue(image.exists() && image.length() > 0);
	}

}
