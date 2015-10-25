package limeng32.mirage.core.mapper;

import java.util.List;

import limeng32.mirage.core.pojo.Article;
import limeng32.mirage.core.pojo.User;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheRoleType;

public interface UserMapper extends MapperFace<User> {
	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public User select(int id);

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public List<User> selectAll(User t);

	@Override
	public void insert(User t);

	@Override
	@CacheAnnotation(MappedClass = { User.class }, role = CacheRoleType.Trigger)
	public void update(User t);

	@Override
	@CacheAnnotation(MappedClass = { User.class }, role = CacheRoleType.Trigger)
	public void updatePersistent(User t);

	@Override
	public void retrieve(User t);

	@Override
	public void retrieveOnlyNull(User t);

	@Override
	@CacheAnnotation(MappedClass = { User.class }, role = CacheRoleType.Trigger)
	public void delete(User t);

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public int count(User t);

	public void loadArticle(User user, Article article);
}
