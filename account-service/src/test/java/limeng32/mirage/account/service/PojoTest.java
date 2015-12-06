package limeng32.mirage.account.service;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.LoginLog;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

public class PojoTest {

	@Test
	public void testReflection1() {
		Account a1 = new Account();
		Account a2 = new Account();
		a1.setEmail("asd");
		a2.setEmail("asd");
		ReflectionAssert.assertReflectionEquals(a1, a2);
	}

	@Test
	public void testReflection2() {
		Account a1 = new Account();
		Account a2 = new Account();
		LoginLog l1 = new LoginLog(), l2 = new LoginLog();
		l1.setLoginIP("l1");
		l2.setLoginIP("l1");
		a1.setEmail("asd");
		a1.addLoginLog(l1);
		a2.setEmail("asd");
		a2.addLoginLog(l2);
		ReflectionAssert.assertReflectionEquals(a1, a2);
	}

	@Test
	public void testReflection3() {
		Account a1 = new Account();
		a1.setEmail("asd");
		ReflectionAssert.assertPropertyReflectionEquals("email", "asd", a1);
	}
}
