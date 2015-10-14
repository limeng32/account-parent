package limeng32.mirage.account.persist;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.apache.commons.dbcp.BasicDataSource;
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
	private AccountService accountService;

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
}
