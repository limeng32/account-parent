package limeng32.mirage.account.persist;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:account-persist.xml")
public class AccountServiceTest {

	@Autowired
	private BasicDataSource dataSource;

	@Autowired
	private AccountPersistService accountService;

	@Test
	public void testDataSource() {
		assertNotNull(dataSource);
		assertNotNull(dataSource.getUsername());
	}

	@Test
	public void testAccountService() {
		Account a = accountService.select(1);
		assertNotNull(a);
		a.setName("socool");
		accountService.update(a);
		Account b = accountService.select(1);
		assertNotNull(b);
		assertEquals("socool", b.getName());
	}

	// @Test
	// public void testAccountServiceInsert() {
	// Account a = new Account();
	// a.setName("deadlycool");
	// a.setActivated(true);
	// accountService.insert(a);
	// }

	@Test
	public void testBooleanHandler() {
		Account a = accountService.select(1);
		Assert.assertTrue(a.isActivated());
		Account a2 = accountService.select(2);
		Assert.assertFalse(a2.isActivated());
	}
}
