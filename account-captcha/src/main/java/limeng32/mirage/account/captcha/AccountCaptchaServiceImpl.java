package limeng32.mirage.account.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Service
public class AccountCaptchaServiceImpl implements AccountCaptchaService,
		InitializingBean {

	private DefaultKaptcha producer;

	private Map<String, String> captchaMap = new ConcurrentHashMap<String, String>();

	private List<String> preDefinedTexts;

	private int textCount = 0;

	@Override
	public void afterPropertiesSet() throws Exception {

		producer = new DefaultKaptcha();

		producer.setConfig(new Config(new Properties()));

	}

	@Override
	public List<String> getPreDefinedTexts() {
		return preDefinedTexts;
	}

	@Override
	public void setPreDefinedTexts(List<String> preDefinedTexts) {
		this.preDefinedTexts = preDefinedTexts;
	}

	private String getCaptchaText() {
		if (preDefinedTexts != null && !preDefinedTexts.isEmpty()) {
			String text = preDefinedTexts.get(textCount);

			textCount = (textCount + 1) % preDefinedTexts.size();

			return text;
		} else {
			return producer.createText();
		}
	}

	@Override
	public String generateCaptchaKeyNew(String remoteIP)
			throws AccountCaptchaException {
		String value = getCaptchaText();
		captchaMap.put(remoteIP, value);
		return value;
	}

	@Override
	public byte[] generateCaptchaImageNew(String captchaText)
			throws AccountCaptchaException {

		if (captchaText == null) {
			throw new AccountCaptchaException("captchaText is null!");
		}
		BufferedImage image = producer.createImage(captchaText);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "jpg", out);
		} catch (IOException e) {
			throw new AccountCaptchaException(
					"Failed to write captcha stream!", e);
		}

		return out.toByteArray();
	}

	@Override
	public boolean validateCaptchaNew(String remoteIP, String captchaValue)
			throws AccountCaptchaException {
		String text = captchaMap.get(remoteIP);
		if (text == null) {
			throw new AccountCaptchaException("RemoteIP '" + remoteIP
					+ "' not found!");
		}
		if (text.equals(captchaValue)) {
			captchaMap.remove(remoteIP);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean checkCaptcha(String remoteIP, String captchaValue)
			throws AccountCaptchaException {
		String text = captchaMap.get(remoteIP);
		if (text != null && text.equals(captchaValue)) {
			return true;
		} else {
			return false;
		}
	}

}
