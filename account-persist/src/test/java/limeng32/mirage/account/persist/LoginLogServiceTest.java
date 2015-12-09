package limeng32.mirage.account.persist;

import static junit.framework.Assert.assertNotNull;

import java.util.Collection;

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
	private AccountPersistService accountService;

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

	// @Test
	// public void testLoginLogServiceInsert2() {
	// LoginLog l = new LoginLog();
	// l.setLoginIP("testLoginLogServiceInsert2");
	// loginLogService.insert(l);
	// loginLogService.retrieveOnlyNull(l);
	// assertNotNull(l.getLoginTime());
	// }

	// @Test
	// public void testLoginLogServiceInsert3() {
	// LoginLog l = loginLogService.select(4);
	// l.setLoginTime(new Date());
	// loginLogService.update(l);
	// }

	// @Test
	public void testLoginLogServiceSelect2() {
		LoginLog l = loginLogService.select(2);
		assertNotNull(l.getAccount());
	}

	// @Test
	public void testLoginLogServiceLoad() {
		Account a = accountService.select(1);
		accountService.loadLoginLog(a, new LoginLog());
		LoginLog[] loginLogArray = new LoginLog[a.getLoginLog().size()];
		a.getLoginLog().toArray(loginLogArray);
		Assert.assertThat(loginLogArray[loginLogArray.length - 1].getId(),
				Matchers.greaterThan(loginLogArray[0].getId()));
	}

	@Test
	public void testLoginLogServiceSelectAll() {
		Collection<LoginLog> c = loginLogService.selectAll(new LoginLog());
		LoginLog[] loginLogArray = new LoginLog[c.size()];
		c.toArray(loginLogArray);
		Assert.assertThat(loginLogArray[loginLogArray.length - 1].getId(),
				Matchers.greaterThan(loginLogArray[0].getId()));
	}

	@Test
	public void testLoginLogServiceSelectAll2() {
		Collection<LoginLog> c = loginLogService.selectAll(new LoginLog());
		int origin = c.size();
		LoginLog l = loginLogService.select(1);
		c.add(l);
		int latest = c.size();
		Assert.assertEquals(1, latest - origin);
	}

	@Test
	public void testRemoveSetId() {
		LoginLog l = loginLogService.select(1);
		Assert.assertEquals(0, l.getId() - 1);
	}
}
