package limeng32.mirage.mapper;

import java.util.Collection;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.LoginLog;
import limeng32.mirage.util.mapper.MapperFace;
import limeng32.mybatis.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatis.mybatisPlugin.cachePlugin.annotation.CacheRoleType;

public interface AccountMapper extends MapperFace<Account> {

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public Account select(int id);

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public Collection<Account> selectAll(Account t);

	@Override
	public void insert(Account t);

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Trigger)
	public int update(Account t);

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Trigger)
	public int updatePersistent(Account t);

	@Override
	public void retrieve(Account t);

	@Override
	public void retrieveOnlyNull(Account t);

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Trigger)
	public int delete(Account t);

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public int count(Account t);

	public void loadLoginLog(Account account, LoginLog loginLog);
}
