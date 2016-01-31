package limeng32.mirage.account.persist;

import static junit.framework.Assert.assertNotNull;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:account-persist.xml")
public class AccountPersistServiceTest {

	@Autowired
	private BasicDataSource dataSource;

	@Autowired
	private AccountPersistService accountService;

	@Test
	@IfProfileValue(name = "VOLATILE", value = "true")
	public void testDataSource() {
		assertNotNull(dataSource);
		assertNotNull(dataSource.getUsername());
	}

}
