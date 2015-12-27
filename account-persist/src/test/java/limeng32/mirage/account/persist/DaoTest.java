package limeng32.mirage.account.persist;

import java.util.Collection;

import limeng32.mybatis.mybatisPlugin.util.ReflectHelper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:account-persist.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class)
public class DaoTest {

	@Autowired
	private AccountPersistService accountPersistService;

	@Autowired
	private LoginLogService loginLogService;

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.updateAccount.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/persist/DaoTest.updateAccount.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.updateAccount.xml")
	public void updateAccount() {
		Account ac = new Account();
		try {
			ReflectHelper.setValueByFieldName(ac, "id", 1);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ac.setName("john");
		ac.setEmail("john@l.c");
		ac.setPassword("5a690d842935c51f26f473e025c1b97a");
		ac.setActivated(true);
		ac.setActivateValue("");
		accountPersistService.insert(ac);
		Account a = accountPersistService.select(1);
		Assert.assertNotNull(a);
		a.setName("mary");
		accountPersistService.update(a);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.updatePersistAccount.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/persist/DaoTest.updatePersistAccount.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.updatePersistAccount.xml")
	public void updatePersistAccount() throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Account ac = new Account();
		ReflectHelper.setValueByFieldName(ac, "id", 1);
		ac.setName("john");
		ac.setEmail("john@l.cn");
		ac.setPassword("5a690d842935c51f26f473e025c1b97a");
		ac.setActivated(true);
		ac.setActivateValue("");
		accountPersistService.insert(ac);
		Account a = accountPersistService.select(1);
		a.setName("mary");
		a.setPassword(null);
		int i = accountPersistService.updatePersistent(a);
		Assert.assertEquals(1, i);
		Account a2 = accountPersistService.select(1);
		Assert.assertNull(a2.getPassword());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.deleteAccount.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.deleteAccount.xml")
	public void deleteAccount() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Account ac = new Account();
		ReflectHelper.setValueByFieldName(ac, "id", 1);
		ac.setName("john");
		ac.setEmail("delete@l.c");
		ac.setPassword("5a690d842935c51f26f473e025c1b97a");
		ac.setActivated(true);
		ac.setActivateValue("");
		accountPersistService.insert(ac);
		Account a = accountPersistService.select(1);
		int i = accountPersistService.delete(a);
		Assert.assertEquals(1, i);
		Account a2 = accountPersistService.select(1);
		Assert.assertNull(a2);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.updateLoginLog.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/persist/DaoTest.updateLoginLog.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.updateLoginLog.xml")
	public void updateLoginLog() throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		LoginLog ll = new LoginLog();
		ReflectHelper.setValueByFieldName(ll, "id", 1);
		ll.setLoginIP("2222");
		loginLogService.insert(ll);

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				LoginLog log1 = loginLogService.select(1);
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LoginLog lc = new LoginLog();
				lc.setLoginIP("1111");
				Collection<LoginLog> logC = loginLogService.selectAll(lc);
				LoginLog[] logs = logC.toArray(new LoginLog[logC.size()]);
				log1 = logs[0];
				log1.setLoginIP("2222");
				loginLogService.update(log1);
			}
		});

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				LoginLog log1 = loginLogService.select(1);
				log1.setLoginIP("1111");
				loginLogService.update(log1);
			}
		});

		t1.start();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		t2.start();
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
