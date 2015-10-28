package limeng32.mirage.account.persist.mapper;

import java.util.Collection;

import limeng32.mirage.account.persist.LoginLog;
import limeng32.mirage.util.mapper.MapperFace;

public interface LoginLogMapper extends MapperFace<LoginLog> {

	@Override
	public LoginLog select(int id);

	@Override
	public Collection<LoginLog> selectAll(LoginLog t);

	@Override
	public void insert(LoginLog t);

	@Override
	public void update(LoginLog t);

	@Override
	public void updatePersistent(LoginLog t);

	@Override
	public void retrieve(LoginLog t);

	@Override
	public void retrieveOnlyNull(LoginLog t);

	@Override
	public void delete(LoginLog t);

	@Override
	public int count(LoginLog t);
}
