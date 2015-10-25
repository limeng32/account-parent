package limeng32.mirage.core.mapper;

import java.util.List;

import limeng32.mirage.core.pojo.Book;
import limeng32.mirage.core.pojo.BookWriter;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheRoleType;
import limeng32.mybatisPlugin.mapperPlugin.able.AbleMapperFace;

public interface BookMapper extends MapperFace<Book>, AbleMapperFace<Book> {

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public Book select(int id);

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public List<Book> selectAll(Book t);

	@Override
	public void insert(Book t);

	@Override
	@CacheAnnotation(MappedClass = { Book.class }, role = CacheRoleType.Trigger)
	public void update(Book t);

	@Override
	@CacheAnnotation(MappedClass = { Book.class }, role = CacheRoleType.Trigger)
	public void updatePersistent(Book t);

	@Override
	public void retrieve(Book t);

	@Override
	public void retrieveOnlyNull(Book t);

	@Override
	@CacheAnnotation(MappedClass = { Book.class }, role = CacheRoleType.Trigger)
	public void delete(Book t);

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public int count(Book t);

	@Override
	@CacheAnnotation(MappedClass = { Book.class }, role = CacheRoleType.Trigger)
	public void disable(Book t);

	@Override
	@CacheAnnotation(MappedClass = { Book.class }, role = CacheRoleType.Trigger)
	public void enable(Book t);

	public void loadBookWriter(Book book, BookWriter bookWriter);

}
