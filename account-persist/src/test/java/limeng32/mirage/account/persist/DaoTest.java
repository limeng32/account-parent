package limeng32.mirage.account.persist;

import java.util.Collection;
import java.util.Date;

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

import com.alibaba.fastjson.JSON;
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

	// @Test
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

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.loadLoginlog.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.loadLoginlog.xml")
	public void loadLoginlog() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Account _ac = new Account();
		ReflectHelper.setValueByFieldName(_ac, "id", 1);
		_ac.setName("john");
		_ac.setEmail("john@l.c");
		_ac.setPassword("5a690d842935c51f26f473e025c1b97a");
		_ac.setActivated(true);
		_ac.setActivateValue("");
		accountPersistService.insert(_ac);
		LoginLog _log = new LoginLog();
		ReflectHelper.setValueByFieldName(_log, "id", 1);
		_log.setLoginIP("127.0.0.1");
		_log.setAccount(_ac);
		loginLogService.insert(_log);
		LoginLog log = loginLogService.select(1);
		Assert.assertEquals("john", log.getAccount().getName());
		Account account = accountPersistService.select(1);
		account.setName("mary");
		accountPersistService.update(account);
		log = loginLogService.select(1);
		Assert.assertEquals("mary", log.getAccount().getName());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.serializeAccount.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.serializeAccount.xml")
	public void serializeAccount() {
		Account a = new Account();
		try {
			ReflectHelper.setValueByFieldName(a, "id", 2);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		a.setName("john");
		accountPersistService.insert(a);
		LoginLog l = new LoginLog();
		try {
			ReflectHelper.setValueByFieldName(l, "id", 2);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		l.setLoginIP("127.0.0.1");
		l.setAccount(a);
		l.setLoginTime(new Date());
		loginLogService.insert(l);
		a.setEmail("_email");
		accountPersistService.update(a);
		l.setLoginTime(null);
		Account a2 = accountPersistService.select(2);
		accountPersistService.loadLoginLog(a2, new LoginLog());
		a2.getLoginLog().toArray(new LoginLog[a.getLoginLog().size()])[0]
				.setLoginTime(null);
		String jsonA = JSON.toJSONString(a);
		String jsonA2 = JSON.toJSONString(a2);
		Assert.assertEquals(jsonA, jsonA2);
		LoginLog newLog = new LoginLog();
		try {
			ReflectHelper.setValueByFieldName(newLog, "id", 2);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		newLog.setLoginIP("123");
		a2.addLoginLog(newLog);
		String jsonA2o = JSON.toJSONString(a2);
		Assert.assertFalse(jsonA2.equals(jsonA2o));
		Assert.assertTrue(a == a.getLoginLog().toArray(
				new LoginLog[a.getLoginLog().size()])[0].getAccount());
		Assert.assertTrue(a2.equals(a2.getLoginLog().toArray(
				new LoginLog[a2.getLoginLog().size()])[0].getAccount()));
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.testRemoveLoginLog.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/persist/DaoTest.testRemoveLoginLog.xml")
	public void testRemoveLoginLog() {
		Account a = new Account();
		try {
			ReflectHelper.setValueByFieldName(a, "id", 2);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		a.setName("john");
		accountPersistService.insert(a);
		LoginLog l = new LoginLog(), l2 = new LoginLog(), l3 = new LoginLog();
		try {
			ReflectHelper.setValueByFieldName(l, "id", 2);
			ReflectHelper.setValueByFieldName(l2, "id", 3);
			ReflectHelper.setValueByFieldName(l3, "id", 3);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		l.setLoginIP("127.0.0.2");
		l.setAccount(a);
		loginLogService.insert(l);
		l2.setLoginIP("127.0.0.3");
		l2.setAccount(a);
		loginLogService.insert(l2);
		a.setEmail("_email");
		accountPersistService.update(a);
		Account a2 = accountPersistService.select(2);
		accountPersistService.loadLoginLog(a2, new LoginLog());
		Assert.assertEquals(2, a2.getLoginLog().size());
		LoginLog l4 = a2.getLoginLog().toArray(
				new LoginLog[a2.getLoginLog().size()])[1];
		String json1 = JSON.toJSONString(l4);
		a2.removeLoginLog(l3);
		Assert.assertEquals(1, a2.getLoginLog().size());
		String json2 = JSON.toJSONString(l4);
		Assert.assertFalse(json1.equals(json2));
	}
}
