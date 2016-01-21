package limeng32.mirage.account.persist.face;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.util.pojo.PojoFace;

public interface CommentFace<T> extends PojoFace<T>{

	void setAccount(Account account);

}
