package limeng32.mirage.account.persist;

import java.util.Collection;

import limeng32.mirage.mapper.AccountBucketMapper;
import limeng32.mirage.util.service.ServiceSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountBucketService extends ServiceSupport<AccountBucket>
		implements AccountBucketMapper {

	@Autowired
	private AccountBucketMapper mapper;

	@Override
	public AccountBucket select(int id) {
		return supportSelect(mapper, id);
	}

	@Override
	public void insert(AccountBucket t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(AccountBucket t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<AccountBucket> selectAll(AccountBucket t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(AccountBucket t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public void retrieve(AccountBucket t) {
		supportRetrieve(mapper, t);
	}

	@Override
	public void retrieveOnlyNull(AccountBucket t) {
		supportRetrieveOnlyNull(mapper, t);
	}

	@Override
	public int delete(AccountBucket t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(AccountBucket t) {
		return supportCount(mapper, t);
	}
}