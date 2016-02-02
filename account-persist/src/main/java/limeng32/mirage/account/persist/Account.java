package limeng32.mirage.account.persist;

import java.io.Serializable;

import limeng32.mirage.account.persist.face.CommentFace;
import limeng32.mirage.account.persist.face.StoryFace;
import limeng32.mirage.util.pojo.PojoSupport;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.FieldMapperAnnotation;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.OpLockType;
import limeng32.mybatis.mybatisPlugin.mapperPlugin.annotation.TableMapperAnnotation;

import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.annotation.JSONField;

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

	@FieldMapperAnnotation(dbFieldName = "opLock", jdbcType = JdbcType.INTEGER, opLockType = OpLockType.Version)
	@JSONField(serialize = false)
	private Integer opLock;

	/**
	 * 是否已激活
	 * */
	@FieldMapperAnnotation(dbFieldName = "activated", jdbcType = JdbcType.BOOLEAN)
	private Boolean activated;

	/**
	 * 激活码
	 * 
	 */
	@JSONField(serialize = false)
	@FieldMapperAnnotation(dbFieldName = "activateValue", jdbcType = JdbcType.VARCHAR)
	private java.lang.String activateValue;

	private java.util.Collection<LoginLog> loginLog;

	private java.util.Collection<CommentFace> comment;

	private java.util.Collection<StoryFace> story;

	public Integer getId() {
		return id;
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

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public java.lang.String getActivateValue() {
		return activateValue;
	}

	public void setActivateValue(java.lang.String activateValue) {
		this.activateValue = activateValue;
	}

	public Integer getOpLock() {
		return opLock;
	}

	public java.util.Collection<LoginLog> getLoginLog() {
		if (loginLog == null)
			loginLog = new java.util.LinkedHashSet<LoginLog>();
		return loginLog;
	}

	@JSONField(serialize = false)
	public java.util.Iterator<LoginLog> getIteratorLoginLog() {
		if (loginLog == null)
			loginLog = new java.util.LinkedHashSet<LoginLog>();
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
			this.loginLog = new java.util.LinkedHashSet<LoginLog>();
		if (!this.loginLog.contains(newLoginLog)) {
			this.loginLog.add(newLoginLog);
			newLoginLog.setAccount(this);
		} else {
			for (LoginLog temp : this.loginLog) {
				if (newLoginLog.equals(temp)) {
					if (temp != newLoginLog) {
						removeLoginLog(temp);
						this.loginLog.add(newLoginLog);
						newLoginLog.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeLoginLog(LoginLog oldLoginLog) {
		if (oldLoginLog == null)
			return;
		if (this.loginLog != null)
			if (this.loginLog.contains(oldLoginLog)) {
				for (LoginLog temp : this.loginLog) {
					if (oldLoginLog.equals(temp)) {
						if (temp != oldLoginLog) {
							temp.setAccount((Account) null);
						}
						break;
					}
				}
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
			loginLog.clear();
		}
	}

	public java.util.Collection<? extends CommentFace> getComment() {
		if (comment == null)
			comment = new java.util.LinkedHashSet<CommentFace>();
		return comment;
	}

	@JSONField(serialize = false)
	public java.util.Iterator<? extends CommentFace> getIteratorComment() {
		if (comment == null)
			comment = new java.util.LinkedHashSet<CommentFace>();
		return comment.iterator();
	}

	public void setComment(
			java.util.Collection<? extends CommentFace> newComment) {
		removeAllComment();
		for (java.util.Iterator<? extends CommentFace> iter = newComment
				.iterator(); iter.hasNext();)
			addComment((CommentFace) iter.next());
	}

	public void addComment(CommentFace newComment) {
		if (newComment == null)
			return;
		if (this.comment == null)
			this.comment = new java.util.LinkedHashSet<CommentFace>();
		if (!this.comment.contains(newComment)) {
			this.comment.add(newComment);
			newComment.setAccount(this);
		} else {
			for (CommentFace temp : this.comment) {
				if (newComment.equals(temp)) {
					if (temp != newComment) {
						removeComment(temp);
						this.comment.add(newComment);
						newComment.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeComment(CommentFace oldComment) {
		if (oldComment == null)
			return;
		if (this.comment != null)
			if (this.comment.contains(oldComment)) {
				for (CommentFace temp : this.comment) {
					if (oldComment.equals(temp)) {
						if (temp != oldComment) {
							temp.setAccount((Account) null);
						}
						break;
					}
				}
				this.comment.remove(oldComment);
				oldComment.setAccount((Account) null);
			}
	}

	public void removeAllComment() {
		if (comment != null) {
			CommentFace oldComment;
			for (java.util.Iterator<? extends CommentFace> iter = getIteratorComment(); iter
					.hasNext();) {
				oldComment = (CommentFace) iter.next();
				iter.remove();
				oldComment.setAccount((Account) null);
			}
			comment.clear();
		}
	}

	public java.util.Collection<StoryFace> getStory() {
		if (story == null)
			story = new java.util.LinkedHashSet<StoryFace>();
		return story;
	}

	@JSONField(serialize = false)
	public java.util.Iterator<StoryFace> getIteratorStory() {
		if (story == null)
			story = new java.util.LinkedHashSet<StoryFace>();
		return story.iterator();
	}

	public void setStory(java.util.Collection<StoryFace> newStory) {
		removeAllStory();
		for (java.util.Iterator<StoryFace> iter = newStory.iterator(); iter
				.hasNext();)
			addStory((StoryFace) iter.next());
	}

	public void addStory(StoryFace newStory) {
		if (newStory == null)
			return;
		if (this.story == null)
			this.story = new java.util.LinkedHashSet<StoryFace>();
		if (!this.story.contains(newStory)) {
			this.story.add(newStory);
			newStory.setAccount(this);
		} else {
			for (StoryFace temp : this.story) {
				if (newStory.equals(temp)) {
					if (temp != newStory) {
						removeStory(temp);
						this.story.add(newStory);
						newStory.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeStory(StoryFace oldStory) {
		if (oldStory == null)
			return;
		if (this.story != null)
			if (this.story.contains(oldStory)) {
				for (StoryFace temp : this.story) {
					if (oldStory.equals(temp)) {
						if (temp != oldStory) {
							temp.setAccount((Account) null);
						}
						break;
					}
				}
				this.story.remove(oldStory);
				oldStory.setAccount((Account) null);
			}
	}

	public void removeAllStory() {
		if (story != null) {
			StoryFace oldStory;
			for (java.util.Iterator<StoryFace> iter = getIteratorStory(); iter
					.hasNext();) {
				oldStory = (StoryFace) iter.next();
				iter.remove();
				oldStory.setAccount((Account) null);
			}
			story.clear();
		}
	}

}
