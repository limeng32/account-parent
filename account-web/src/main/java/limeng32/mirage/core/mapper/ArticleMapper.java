package limeng32.mirage.core.mapper;

import java.util.List;

import limeng32.mirage.core.pojo.Article;
import limeng32.mirage.core.pojo.User;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheRoleType;


public interface ArticleMapper extends MapperFace<Article> {

	@Override
	@CacheAnnotation(MappedClass = { User.class }, role = CacheRoleType.Observer)
	public Article select(int id);

	@Override
	@CacheAnnotation(MappedClass = { User.class }, role = CacheRoleType.Observer)
	public List<Article> selectAll(Article t);

	@Override
	public void insert(Article t);

	@Override
	@CacheAnnotation(MappedClass = { Article.class }, role = CacheRoleType.Trigger)
	public void update(Article t);

	@Override
	@CacheAnnotation(MappedClass = { Article.class }, role = CacheRoleType.Trigger)
	public void updatePersistent(Article t);

	@Override
	public void retrieve(Article t);

	@Override
	public void retrieveOnlyNull(Article t);

	@Override
	@CacheAnnotation(MappedClass = { Article.class }, role = CacheRoleType.Trigger)
	public void delete(Article t);

	@Override
	@CacheAnnotation(MappedClass = { User.class }, role = CacheRoleType.Observer)
	public int count(Article t);

}
