package limeng32.mirage.account.persist.condition;

import limeng32.mirage.account.persist.Account;
import limeng32.mybatis.mybatisPlugin.cachePlugin.Conditionable;
import limeng32.mybatis.mybatisPlugin.cachePlugin.Limitable;
import limeng32.mybatis.mybatisPlugin.cachePlugin.Queryable;
import limeng32.mybatis.mybatisPlugin.cachePlugin.Sortable;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.QueryMapperAnnotation;

@QueryMapperAnnotation(tableName = "Article")
public class AccountCondition extends Account implements Conditionable {

	private static final long serialVersionUID = 1L;

	public static final String field_tableName = "article";

	public static final String field_id = "id";

	public static final String field_userid = "userid";

	public static final String field_title = "title";

	public static final String field_content = "content";

	public enum Field implements Queryable {
		tableName(field_tableName), id(field_id), userid(field_userid), title(
				field_title), content(field_content);

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
