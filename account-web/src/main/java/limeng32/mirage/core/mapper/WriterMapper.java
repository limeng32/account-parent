package limeng32.mirage.core.mapper;

import java.util.List;

import limeng32.mirage.core.pojo.Association;
import limeng32.mirage.core.pojo.BookWriter;
import limeng32.mirage.core.pojo.Level;
import limeng32.mirage.core.pojo.Writer;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheRoleType;
import limeng32.mybatisPlugin.mapperPlugin.able.AbleMapperFace;

public interface WriterMapper extends MapperFace<Writer>,
		AbleMapperFace<Writer> {

	@Override
	@CacheAnnotation(MappedClass = { Association.class, Level.class }, role = CacheRoleType.Observer)
	public Writer select(int id);

	@Override
	@CacheAnnotation(MappedClass = { Association.class, Level.class }, role = CacheRoleType.Observer)
	public List<Writer> selectAll(Writer t);

	@Override
	public void insert(Writer t);

	@Override
	@CacheAnnotation(MappedClass = { Writer.class }, role = CacheRoleType.Trigger)
	public void update(Writer t);

	@Override
	@CacheAnnotation(MappedClass = { Writer.class }, role = CacheRoleType.Trigger)
	public void updatePersistent(Writer t);

	@Override
	public void retrieve(Writer t);

	@Override
	public void retrieveOnlyNull(Writer t);

	@Override
	@CacheAnnotation(MappedClass = { Writer.class }, role = CacheRoleType.Trigger)
	public void delete(Writer t);

	@Override
	@CacheAnnotation(MappedClass = { Association.class, Level.class }, role = CacheRoleType.Observer)
	public int count(Writer t);

	@Override
	@CacheAnnotation(MappedClass = { Writer.class }, role = CacheRoleType.Trigger)
	public void disable(Writer t);

	@Override
	@CacheAnnotation(MappedClass = { Writer.class }, role = CacheRoleType.Trigger)
	public void enable(Writer t);

	public void loadBookWriter(Writer writer, BookWriter bookWriter);

}
