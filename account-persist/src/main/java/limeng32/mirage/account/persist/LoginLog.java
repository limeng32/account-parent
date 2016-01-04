package limeng32.mirage.account.persist;

import java.io.Serializable;

import limeng32.mirage.util.pojo.PojoSupport;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.FieldMapperAnnotation;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.TableMapperAnnotation;

import org.apache.ibatis.type.JdbcType;

@TableMapperAnnotation(tableName = "loginLog")
public class LoginLog extends PojoSupport<LoginLog> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "loginTime", jdbcType = JdbcType.TIMESTAMP)
	private java.util.Date loginTime;

	@FieldMapperAnnotation(dbFieldName = "loginIP", jdbcType = JdbcType.VARCHAR)
	private java.lang.String loginIP;

	@FieldMapperAnnotation(dbFieldName = "accountid", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "id")
	private Account account;

	public Integer getId() {
		return id;
	}

	public java.util.Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}

	public java.lang.String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(java.lang.String loginIP) {
		this.loginIP = loginIP;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account newAccount) {
		if (this.account == null || !this.account.equals(newAccount)) {
			if (this.account != null) {
				Account oldAccount = this.account;
				this.account = null;
				oldAccount.removeLoginLog(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addLoginLog(this);
			}
		}
	}

}