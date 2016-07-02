package limeng32.mirage.mapper;

import java.util.Collection;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountBucket;
import limeng32.mirage.util.mapper.MapperFace;
import limeng32.mybatis.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatis.mybatisPlugin.cachePlugin.annotation.CacheRoleType;

public interface AccountBucketMapper extends MapperFace<AccountBucket> {

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Observer)
	public AccountBucket select(int id);

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Observer)
	public Collection<AccountBucket> selectAll(AccountBucket t);

	@Override
	public void insert(AccountBucket t);

	@Override
	@CacheAnnotation(MappedClass = { AccountBucket.class }, role = CacheRoleType.Trigger)
	public int update(AccountBucket t);

	@Override
	@CacheAnnotation(MappedClass = { AccountBucket.class }, role = CacheRoleType.Trigger)
	public int updatePersistent(AccountBucket t);

	@Override
	public void retrieve(AccountBucket t);

	@Override
	public void retrieveOnlyNull(AccountBucket t);

	@Override
	@CacheAnnotation(MappedClass = { AccountBucket.class }, role = CacheRoleType.Trigger)
	public int delete(AccountBucket t);

	@Override
	@CacheAnnotation(MappedClass = { Account.class }, role = CacheRoleType.Observer)
	public int count(AccountBucket t);
}
