package limeng32.mirage.account.service;

import junit.framework.Assert;
import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountBucket;
import limeng32.mirage.account.persist.AccountBucketService;
import limeng32.mirage.account.persist.AccountPersistService;
import limeng32.mybatis.mybatisPlugin.util.ReflectHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
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
@ContextConfiguration({ "classpath:account-service-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class)
public class AccountService2Test {

	@Autowired
	private AccountBucketService accountBucketService;

	@Autowired
	private AccountPersistService accountPersistService;

	@Autowired
	private AccountService accountService;

	@Test
	@IfProfileValue(name = "VOLATILE", value = "true")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/AccountService2Test.updateAccountBucket.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/AccountService2Test.updateAccountBucket.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/AccountService2Test.updateAccountBucket.xml")
	public void updateAccountBucket() {
		Account a = new Account();
		AccountBucket ab = new AccountBucket();
		try {
			ReflectHelper.setValueByFieldName(a, "id", 1);
			ReflectHelper.setValueByFieldName(ab, "id", 2);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		accountPersistService.insert(a);
		ab.setAccount(a);
		ab.setOriginalPortrait("a.b");
		accountBucketService.insert(ab);
		AccountBucket ab2 = accountBucketService.select(2);
		ab2.setOriginalPortrait("b.a");
		try {
			accountService.updateAccountBucketTransactive(ab2);
		} catch (AccountServiceException e) {
			e.printStackTrace();
		}
		Integer temp = ab2.getId();
		try {
			ab2.setId(null);
			accountService.updateAccountBucketTransactive(ab2);
		} catch (AccountServiceException e1) {
			Assert.assertEquals(
					AccountServiceExceptionEnum.CannotFindAccountBucket
							.toString(), e1.getMessage());
		} finally {
			ab2.setId(temp);
		}
		Account a2 = accountPersistService.select(1);
		accountPersistService.delete(a2);
		try {
			accountService.updateAccountBucketTransactive(ab2);
		} catch (AccountServiceException e) {
			Assert.assertEquals(
					AccountServiceExceptionEnum.CannotFindAccount.toString(),
					e.getMessage());
		}
	}

	@Test
	@IfProfileValue(name = "VOLATILE", value = "true")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/AccountService2Test.updateAccountBucket2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/AccountService2Test.updateAccountBucket2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/AccountService2Test.updateAccountBucket2.xml")
	public void updateAccountBucket2() {
		Account a = new Account();
		AccountBucket ab = new AccountBucket();
		try {
			ReflectHelper.setValueByFieldName(a, "id", 1);
			ReflectHelper.setValueByFieldName(ab, "id", 2);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		accountPersistService.insert(a);
		ab.setAccount(a);
		ab.setOriginalPortrait("a.b");
		accountBucketService.insert(ab);
		AccountBucket ab2 = accountBucketService.select(2);
		ab2.setOriginalPortrait("b.a");
		try {
			ReflectHelper.setValueByFieldName(ab2, "id", 2);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		accountBucketService.delete(ab);
		try {
			accountService.updateAccountBucketTransactive(ab2);
		} catch (AccountServiceException e) {
			Assert.assertEquals(
					AccountServiceExceptionEnum.ConnotUpdateAccountBucket
							.toString(), e.getMessage());
		}
	}

	@Test
	@IfProfileValue(name = "VOLATILE", value = "true")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/AccountService2Test.insertAccountBucket.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/AccountService2Test.insertAccountBucket.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/AccountService2Test.insertAccountBucket.xml")
	public void insertAccountBucket() {
		Account a = new Account();
		AccountBucket ab = new AccountBucket();
		try {
			ReflectHelper.setValueByFieldName(a, "id", 1);
			ReflectHelper.setValueByFieldName(ab, "id", 2);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		accountPersistService.insert(a);
		ab.setAccount(a);
		ab.setOriginalPortrait("a.b");
		try {
			accountService.insertAccountBucketTransactive(ab);
		} catch (AccountServiceException e) {
			e.printStackTrace();
		}
		AccountBucket ab2 = null;
		try {
			accountService.insertAccountBucketTransactive(ab2);
		} catch (AccountServiceException e) {
			Assert.assertEquals(
					AccountServiceExceptionEnum.CannotFindAccountBucket
							.toString(), e.getMessage());
		}
		ab2 = new AccountBucket();
		try {
			accountService.insertAccountBucketTransactive(ab2);
		} catch (AccountServiceException e) {
			Assert.assertEquals(
					AccountServiceExceptionEnum.CannotFindAccount.toString(),
					e.getMessage());
		}
	}

	@Test
	@IfProfileValue(name = "VOLATILE", value = "true")
	@DatabaseSetup(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/AccountService2Test.insertAccountBucket2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "/limeng32/mirage/account/service/AccountService2Test.insertAccountBucket2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/limeng32/mirage/account/service/AccountService2Test.insertAccountBucket2.xml")
	public void insertAccountBucket2() {
		Account a = new Account();
		AccountBucket ab = new AccountBucket(), ab2 = new AccountBucket();
		try {
			ReflectHelper.setValueByFieldName(a, "id", 1);
			ReflectHelper.setValueByFieldName(ab, "id", 2);
			ReflectHelper.setValueByFieldName(ab2, "id", 3);
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		accountPersistService.insert(a);
		ab.setAccount(a);
		ab.setOriginalPortrait("a.b");
		Account a2 = accountPersistService.select(1);
		ab2.setAccount(a2);
		ab2.setOriginalPortrait("a.b");
		try {
			accountService.insertAccountBucketTransactive(ab);
		} catch (AccountServiceException e) {
			e.printStackTrace();
		}
		try {
			accountService.insertAccountBucketTransactive(ab2);
		} catch (AccountServiceException e) {
			Assert.assertEquals(
					AccountServiceExceptionEnum.RepetitionAccountBucket
							.toString(), e.getMessage());
		}
	}
}
