package limeng32.mirage.account.persist;

import static junit.framework.Assert.assertNotNull;

import org.apache.commons.dbcp.BasicDataSource;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:account-persist.xml")
public class LoginLogServiceTest {

	@Autowired
	private BasicDataSource dataSource;

	@Autowired
	private AccountService accountService;

	@Autowired
	private LoginLogService loginLogService;

	@Test
	public void testDataSource() {
		assertNotNull(dataSource);
		assertNotNull(dataSource.getUsername());
	}

	@Test
	public void testLoginLogServiceSelect() {
		LoginLog l = loginLogService.select(1);
		assertNotNull(l);
	}

	// @Test
	// public void testLoginLogServiceInsert() {
	// LoginLog l = new LoginLog();
	// l.setAccount(accountService.select(1));
	// l.setLoginIP("testLoginLogServiceInsert");
	// loginLogService.insert(l);
	// }

	@Test
	public void testLoginLogServiceSelect2() {
		LoginLog l = loginLogService.select(2);
		assertNotNull(l.getAccount());
	}

	@Test
	public void testLoginLogServiceLoad() {
		Account a = accountService.select(1);
		accountService.loadLoginLog(a, new LoginLog());
		LoginLog[] loginLogArray = new LoginLog[a.getLoginLog().size()];
		a.getLoginLog().toArray(loginLogArray);
		Assert.assertThat(loginLogArray[loginLogArray.length - 1].getId(),
				Matchers.greaterThan(loginLogArray[0].getId()));
	}
}
