package limeng32.mirage.core.mapper;

import java.util.List;

import limeng32.mirage.core.pojo.Association;
import limeng32.mirage.core.pojo.Writer;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheAnnotation;
import limeng32.mybatisPlugin.cachePlugin.annotation.CacheRoleType;

public interface AssociationMapper extends MapperFace<Association> {

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public Association select(int id);

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public List<Association> selectAll(Association t);

	@Override
	public void insert(Association t);

	@Override
	@CacheAnnotation(MappedClass = { Association.class }, role = CacheRoleType.Trigger)
	public void update(Association t);

	@Override
	@CacheAnnotation(MappedClass = { Association.class }, role = CacheRoleType.Trigger)
	public void updatePersistent(Association t);

	@Override
	public void retrieve(Association t);

	@Override
	public void retrieveOnlyNull(Association t);

	@Override
	@CacheAnnotation(MappedClass = { Association.class }, role = CacheRoleType.Trigger)
	public void delete(Association t);

	@Override
	@CacheAnnotation(MappedClass = {}, role = CacheRoleType.Observer)
	public int count(Association t);

	public void loadWriter(Association association, Writer writer);

}
