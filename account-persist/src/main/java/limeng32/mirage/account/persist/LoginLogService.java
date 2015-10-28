package limeng32.mirage.account.persist;

import java.util.Collection;

import limeng32.mirage.account.persist.mapper.LoginLogMapper;
import limeng32.mirage.util.service.ServiceSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginLogService extends ServiceSupport<LoginLog> implements
		LoginLogMapper {

	@Autowired
	private LoginLogMapper mapper;

	@Override
	public LoginLog select(int id) {
		return supportSelect(mapper, id);
	}

	@Override
	public void insert(LoginLog t) {
		supportInsert(mapper, t);
	}

	@Override
	public void update(LoginLog t) {
		supportUpdate(mapper, t);
	}

	@Override
	public Collection<LoginLog> selectAll(LoginLog t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public void updatePersistent(LoginLog t) {
		supportUpdatePersistent(mapper, t);
	}

	@Override
	public void retrieve(LoginLog t) {
		supportRetrieve(mapper, t);
	}

	@Override
	public void retrieveOnlyNull(LoginLog t) {
		supportRetrieveOnlyNull(mapper, t);
	}

	@Override
	public void delete(LoginLog t) {
		supportDelete(mapper, t);
	}

	@Override
	public int count(LoginLog t) {
		return supportCount(mapper, t);
	}
}