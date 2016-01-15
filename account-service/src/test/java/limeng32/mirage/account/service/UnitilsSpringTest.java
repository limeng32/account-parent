package limeng32.mirage.account.service;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountPersistService;
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
@ContextConfiguration({ "classpath:account-service.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class)
public class UnitilsSpringTest {

	@Autowired
	private AccountPersistService accountPersistService;

	@Autowired
	private AccountService accountService;

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testSelf.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testSelf.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testSelf.xml")
	public void testSelf() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Account ac = new Account();
		ReflectHelper.setValueByFieldName(ac, "id", 1);
		ac.setName("john");
		ac.setEmail("john@l.c");
		ac.setPassword("5a690d842935c51f26f473e025c1b97a");
		ac.setActivated(true);
		ac.setActivateValue("");
		accountPersistService.insert(ac);
		Account a1 = accountPersistService.select(1);
		Account a2 = accountPersistService.select(1);
		a1.setName("mary");
		a2.setName("mary");
		accountPersistService.update(a1);
		accountPersistService.update(a2);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testTransaction.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testTransaction.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testTransaction.xml")
	public void testTransaction() throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Account ac1 = new Account();
		ReflectHelper.setValueByFieldName(ac1, "id", 1);
		ac1.setName("limeng32");
		ac1.setEmail("limeng32@live.cn");
		ac1.setPassword("5a690d842935c51f26f473e025c1b97a");
		ac1.setActivated(true);
		ac1.setActivateValue("");
		accountPersistService.insert(ac1);
		Account a = new Account();
		a.setName("unknown");
		a.setEmail("limeng32@live.cn");
		try {
			accountService.insertAccountTransactive(a);
		} catch (AccountServiceException e) {
			Assert.assertEquals(e.getMessage(),
					AccountServiceExceptionEnum.RepetitionEmail.toString());
		}
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testTransaction2.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testTransaction2.xml")
	public void testTransaction2() throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Account ac1 = new Account();
		ReflectHelper.setValueByFieldName(ac1, "id", 1);
		ac1.setName("limeng321");
		ac1.setEmail("limeng32@live.cn");
		ac1.setPassword("5a690d842935c51f26f473e025c1b97a");
		ac1.setActivated(true);
		ac1.setActivateValue("");
		accountPersistService.insert(ac1);
		Account a = accountPersistService.select(1);
		Assert.assertEquals("limeng321", a.getName());
	}
}
