package limeng32.mirage.account.persist;

import java.util.List;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.util.mapper.MapperFace;

public interface AccountMapper extends MapperFace<Account> {

	@Override
	public Account select(int id);

	@Override
	public List<Account> selectAll(Account t);

	@Override
	public void insert(Account t);

	@Override
	public void update(Account t);

	@Override
	public void updatePersistent(Account t);

	@Override
	public void retrieve(Account t);

	@Override
	public void retrieveOnlyNull(Account t);

	@Override
	public void delete(Account t);

	@Override
	public int count(Account t);

}
