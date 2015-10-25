package limeng32.mirage.core.pojo.condition;

import limeng32.mirage.core.pojo.Book;
import limeng32.mybatisPlugin.cachePlugin.Conditionable;
import limeng32.mybatisPlugin.cachePlugin.Limitable;
import limeng32.mybatisPlugin.cachePlugin.Queryable;
import limeng32.mybatisPlugin.cachePlugin.Sortable;
import limeng32.mybatisPlugin.mapperPlugin.annotation.ConditionMapperAnnotation;
import limeng32.mybatisPlugin.mapperPlugin.annotation.ConditionType;
import limeng32.mybatisPlugin.mapperPlugin.annotation.QueryMapperAnnotation;

@QueryMapperAnnotation(tableName = "Book")
public class BookCondition extends Book implements Conditionable {

	private static final long serialVersionUID = 1L;

	public enum Field implements Queryable {
		tableName("Book"), id("id"), title("title"), origin("alias");

		private final String value;

		private Field(String value) {
			this.value = value;
		}

		@Override
		public String value() {
			return value;
		}

		@Override
		public String getTableName() {
			return tableName.value;
		}
	}

	private Limitable limiter;

	private Sortable sorter;

	@Override
	public Limitable getLimiter() {
		return limiter;
	}

	@Override
	public void setLimiter(Limitable limiter) {
		this.limiter = limiter;
	}

	@Override
	public Sortable getSorter() {
		return sorter;
	}

	@Override
	public void setSorter(Sortable sorter) {
		this.sorter = sorter;
	}

	@ConditionMapperAnnotation(dbFieldName = "title", conditionType = ConditionType.Like)
	private String titleLike;

	public String getTitleLike() {
		return titleLike;
	}

	public void setTitleLike(String titleLike) {
		this.titleLike = titleLike;
	}

}
