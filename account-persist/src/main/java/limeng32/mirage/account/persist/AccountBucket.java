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

	@FieldMapperAnnotation(dbFieldName = "originalPortrait", jdbcType = JdbcType.VARCHAR)
	private java.lang.String originalPortrait;

	@FieldMapperAnnotation(dbFieldName = "portraitModify", jdbcType = JdbcType.VARCHAR)
	private String portraitModify;

	@FieldMapperAnnotation(dbFieldName = "accountId", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	private Account account;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.lang.String getOriginalPortrait() {
		return originalPortrait;
	}

	public void setOriginalPortrait(java.lang.String originalPortrait) {
		this.originalPortrait = originalPortrait;
	}

	public String getPortrait() {
		return originalPortrait + "@" + portraitModify + "_160w_160h";
	}
	
	public String getSmallPortrait() {
		return originalPortrait + "@" + portraitModify + "_30w_30h";
	}

	public String getPortraitModify() {
		return portraitModify;
	}

	public void setPortraitModify(String portraitModify) {
		this.portraitModify = portraitModify;
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
