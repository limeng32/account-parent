package limeng32.mirage.account.service;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountPersistService;

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
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:account-email.xml",
		"classpath:account-persist.xml", "classpath:account-captcha.xml",
		"classpath:account-service.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class)
public class UnitilsSpringTest {

	@Autowired
	private AccountPersistService accountPersistService;

	@Autowired
	private AccountService accountService;

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/limeng32/mirage/account/service/dbunitTest-updateA.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/dbunitTest-updateE.xml")
	@DatabaseTearDown(type = DatabaseOperation.CLEAN_INSERT, value = "/limeng32/mirage/account/service/dbunitTest-updateO.xml")
	public void testSelf() {
		Account a = accountPersistService.select(1);
		Assert.assertNotNull(a);
		Assert.assertEquals("john", a.getName());
		Assert.assertEquals(1, accountPersistService.update(a));
		Account a2 = new Account();
		a2.setName("asd");
//		accountPersistService.insert(a2);
//		Assert.assertEquals(1, accountPersistService.updatePersistent(a2));
//		Assert.assertEquals(1, accountPersistService.delete(a2));
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testTransaction.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testTransaction.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.CLEAN_INSERT, value = "/limeng32/mirage/account/service/UnitilsSpringTest.testTransaction.teardown.xml")
	public void testTransaction() {
		Account a = new Account();
		a.setName("unknown");
		a.setEmail("limeng32@live.cn");
		try {
			accountService.transactiveInsert(a);
		} catch (AccountServiceException e) {
			switch (e.getMessage()) {
			case "Repetition email.":
				break;
			default:
				e.printStackTrace();
				break;
			}
		}
	}
}
