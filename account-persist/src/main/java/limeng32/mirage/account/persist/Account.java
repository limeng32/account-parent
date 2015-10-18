package limeng32.mirage.account.persist;

import java.io.Serializable;

import limeng32.mirage.util.pojo.PojoSupport;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.FieldMapperAnnotation;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.TableMapperAnnotation;

import org.apache.ibatis.type.JdbcType;

@TableMapperAnnotation(tableName = "account")
public class Account extends PojoSupport<Account> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "id", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	@FieldMapperAnnotation(dbFieldName = "email", jdbcType = JdbcType.VARCHAR)
	private java.lang.String email;

	@FieldMapperAnnotation(dbFieldName = "password", jdbcType = JdbcType.VARCHAR)
	private java.lang.String password;

	/**
	 * 是否已激活
	 * */
	private boolean activated;

	public java.util.Collection<LoginLog> loginLog;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public java.util.Collection<LoginLog> getLoginLog() {
		if (loginLog == null)
			loginLog = new java.util.HashSet<LoginLog>();
		return loginLog;
	}

	public java.util.Iterator<LoginLog> getIteratorLoginLog() {
		if (loginLog == null)
			loginLog = new java.util.HashSet<LoginLog>();
		return loginLog.iterator();
	}

	public void setLoginLog(java.util.Collection<LoginLog> newLoginLog) {
		removeAllLoginLog();
		for (java.util.Iterator<LoginLog> iter = newLoginLog.iterator(); iter
				.hasNext();)
			addLoginLog((LoginLog) iter.next());
	}

	public void addLoginLog(LoginLog newLoginLog) {
		if (newLoginLog == null)
			return;
		if (this.loginLog == null)
			this.loginLog = new java.util.HashSet<LoginLog>();
		if (!this.loginLog.contains(newLoginLog)) {
			this.loginLog.add(newLoginLog);
			newLoginLog.setAccount(this);
		}
	}

	public void removeLoginLog(LoginLog oldLoginLog) {
		if (oldLoginLog == null)
			return;
		if (this.loginLog != null)
			if (this.loginLog.contains(oldLoginLog)) {
				this.loginLog.remove(oldLoginLog);
				oldLoginLog.setAccount((Account) null);
			}
	}

	public void removeAllLoginLog() {
		if (loginLog != null) {
			LoginLog oldLoginLog;
			for (java.util.Iterator<LoginLog> iter = getIteratorLoginLog(); iter
					.hasNext();) {
				oldLoginLog = (LoginLog) iter.next();
				iter.remove();
				oldLoginLog.setAccount((Account) null);
			}
		}
	}

}
