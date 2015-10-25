package limeng32.mirage.core.mapper;

import java.awt.print.Book;
import java.util.List;

import limeng32.mirage.core.pojo.BookWriter;
import limeng32.mirage.core.pojo.Writer;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheRoleType;
import limeng32.mybatisPlugin.mapperPlugin.able.AbleMapperFace;

public interface BookWriterMapper extends MapperFace<BookWriter>,
		AbleMapperFace<BookWriter> {

	@Override
	@CacheAnnotation(MappedClass = { Writer.class, Book.class }, role = CacheRoleType.Observer)
	public BookWriter select(int id);

	@Override
	@CacheAnnotation(MappedClass = { Writer.class, Book.class }, role = CacheRoleType.Observer)
	public List<BookWriter> selectAll(BookWriter t);

	@Override
	public void insert(BookWriter t);

	@Override
	@CacheAnnotation(MappedClass = { BookWriter.class }, role = CacheRoleType.Trigger)
	public void update(BookWriter t);

	@Override
	@CacheAnnotation(MappedClass = { BookWriter.class }, role = CacheRoleType.Trigger)
	public void updatePersistent(BookWriter t);

	@Override
	public void retrieve(BookWriter t);

	@Override
	public void retrieveOnlyNull(BookWriter t);

	@Override
	@CacheAnnotation(MappedClass = { BookWriter.class }, role = CacheRoleType.Trigger)
	public void delete(BookWriter t);

	@Override
	@CacheAnnotation(MappedClass = { Writer.class, Book.class }, role = CacheRoleType.Observer)
	public int count(BookWriter t);

	@Override
	@CacheAnnotation(MappedClass = { BookWriter.class }, role = CacheRoleType.Trigger)
	public void disable(BookWriter t);

	@Override
	@CacheAnnotation(MappedClass = { BookWriter.class }, role = CacheRoleType.Trigger)
	public void enable(BookWriter t);
}
