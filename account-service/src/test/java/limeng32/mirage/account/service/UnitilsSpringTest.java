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
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
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
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class)
public class UnitilsSpringTest {

	@Autowired
	private AccountPersistService accountPersistService;

	// @Test
	// public void testSpringBean() {
	// Assert.assertNotNull(accountPersistService.select(1));
	// }

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/limeng32/mirage/account/service/dbunitTest-updateA.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/dbunitTest-updateE.xml")
	@DatabaseTearDown(type = DatabaseOperation.CLEAN_INSERT, value = "/limeng32/mirage/account/service/dbunitTest-updateO.xml")
	public void findUserByUserName() {
		Account a = accountPersistService.select(1);
		Assert.assertNotNull(a);
		Assert.assertEquals("john", a.getName());
	}
}