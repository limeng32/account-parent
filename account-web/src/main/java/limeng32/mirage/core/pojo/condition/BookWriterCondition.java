package limeng32.mirage.core.pojo.condition;

import limeng32.mirage.core.pojo.BookWriter;
import limeng32.mybatisPlugin.cachePlugin.Conditionable;
import limeng32.mybatisPlugin.cachePlugin.Limitable;
import limeng32.mybatisPlugin.cachePlugin.Queryable;
import limeng32.mybatisPlugin.cachePlugin.Sortable;
import limeng32.mybatisPlugin.mapperPlugin.annotation.QueryMapperAnnotation;

@QueryMapperAnnotation(tableName = "BookWriter")
public class BookWriterCondition extends BookWriter implements Conditionable {

	private static final long serialVersionUID = 1L;

	public enum Field implements Queryable {
		tableName("BookWriter"), id("id"), bookid("bookid"), writerid(
				"writerid");

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

}
