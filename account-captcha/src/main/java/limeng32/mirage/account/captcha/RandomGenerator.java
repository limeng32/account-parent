package limeng32.mirage.account.captcha;

import java.util.Random;

public class RandomGenerator {

	private static String range = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static int size = 8;

	public static synchronized String getRandomString() {

		Random random = new Random();

		StringBuffer result = new StringBuffer();

		for (int i = 0; i < size; i++) {
			result.append(range.charAt(random.nextInt(range.length())));
		}

		return result.toString();
	}

}
