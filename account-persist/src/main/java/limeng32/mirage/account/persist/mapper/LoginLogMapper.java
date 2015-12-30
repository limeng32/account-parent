package limeng32.mirage.account.persist.mapper;

import java.util.Collection;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.LoginLog;
import limeng32.mirage.util.mapper.MapperFace;
import limeng32.mybatis.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatis.mybatisPlugin.cachePlugin.annotation.CacheRoleType;

public interface LoginLogMapper extends MapperFace<LoginLog> {

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Observer)
	public LoginLog select(int id);

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Observer)
	public Collection<LoginLog> selectAll(LoginLog t);

	@Override
	public void insert(LoginLog t);

	@Override
	@CacheAnnotation(MappedClass = { LoginLog.class }, role = CacheRoleType.Trigger)
	public int update(LoginLog t);

	@Override
	@CacheAnnotation(MappedClass = { LoginLog.class }, role = CacheRoleType.Trigger)
	public int updatePersistent(LoginLog t);

	@Override
	public void retrieve(LoginLog t);

	@Override
	public void retrieveOnlyNull(LoginLog t);

	@Override
	@CacheAnnotation(MappedClass = { LoginLog.class }, role = CacheRoleType.Trigger)
	public int delete(LoginLog t);

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Observer)
	public int count(LoginLog t);
}
