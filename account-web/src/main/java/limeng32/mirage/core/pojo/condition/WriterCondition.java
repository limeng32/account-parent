package limeng32.mirage.core.pojo.condition;

import limeng32.mirage.core.pojo.Writer;
import limeng32.mybatisPlugin.mapperPlugin.annotation.ConditionMapperAnnotation;
import limeng32.mybatisPlugin.mapperPlugin.annotation.ConditionType;
import limeng32.mybatisPlugin.mapperPlugin.annotation.QueryMapperAnnotation;

@QueryMapperAnnotation(tableName = "Writer")
public class WriterCondition extends Writer {

	private static final long serialVersionUID = 1L;

	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.Like)
	private String nameLike;

	public String getNameLike() {
		return nameLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

}
