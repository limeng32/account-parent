package limeng32.mirage.account.persist;

import java.io.Serializable;

import limeng32.mirage.util.pojo.PojoSupport;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.FieldMapperAnnotation;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.TableMapperAnnotation;

import org.apache.ibatis.type.JdbcType;

@TableMapperAnnotation(tableName = "accountbucket")
public class AccountBucket extends PojoSupport<AccountBucket> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "portrait", jdbcType = JdbcType.VARCHAR)
	private java.lang.String portrait;

	@FieldMapperAnnotation(dbFieldName = "accountId", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	private Account account;

	public Integer getId() {
		return id;
	}

	public java.lang.String getPortrait() {
		return portrait;
	}

	public void setPortrait(java.lang.String portrait) {
		this.portrait = portrait;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account newAccount) {
		if (this.account == null || this.account != newAccount) {
			if (this.account != null) {
				Account oldAccount = this.account;
				this.account = null;
				oldAccount.removeAccountBucket(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addAccountBucket(this);
			}
		}
	}
}
