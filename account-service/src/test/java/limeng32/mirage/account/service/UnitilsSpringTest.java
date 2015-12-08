package limeng32.mirage.account.service;

import limeng32.mirage.account.persist.AccountPersistService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:account-email.xml",
		"classpath:account-persist.xml", "classpath:account-captcha.xml",
		"classpath:account-service.xml" })
public class UnitilsSpringTest {

	@Autowired
	private AccountPersistService accountPersistService;

	@Test
	public void testSpringBean() {
		Assert.assertNotNull(accountPersistService.select(1));
	}

}
