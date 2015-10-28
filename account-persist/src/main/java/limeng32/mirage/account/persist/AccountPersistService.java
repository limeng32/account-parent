package limeng32.mirage.account.persist;

import java.util.Collection;

import limeng32.mirage.account.persist.mapper.AccountMapper;
import limeng32.mirage.util.service.ServiceSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountPersistService extends ServiceSupport<Account> implements
		AccountMapper {

	@Autowired
	private AccountMapper mapper;

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public Account select(int id) {
		return supportSelect(mapper, id);
	}

	@Override
	public void insert(Account t) {
		supportInsert(mapper, t);
	}

	@Override
	public void update(Account t) {
		supportUpdate(mapper, t);
	}

	@Override
	public Collection<Account> selectAll(Account t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public void updatePersistent(Account t) {
		supportUpdatePersistent(mapper, t);
	}

	@Override
	public void retrieve(Account t) {
		supportRetrieve(mapper, t);
	}

	@Override
	public void retrieveOnlyNull(Account t) {
		supportRetrieveOnlyNull(mapper, t);
	}

	@Override
	public void delete(Account t) {
		supportDelete(mapper, t);
	}

	@Override
	public int count(Account t) {
		return supportCount(mapper, t);
	}

	@Override
	public void loadLoginLog(Account account, LoginLog loginLog) {
		loginLog.setAccount(account);
		account.setLoginLog(loginLogService.selectAll(loginLog));
	}
}