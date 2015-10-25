package limeng32.mirage.core.service;

import limeng32.mirage.core.mapper.ArticleMapper;
import limeng32.mirage.core.pojo.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TxService2 {

	@Autowired
	private ArticleMapper articleMapper;

	@Transactional(noRollbackFor = { RuntimeException.class }, readOnly = true, propagation = Propagation.REQUIRED)
	public void insert() {
		Article a1 = new Article();
		a1.setTitle("c1");
		articleMapper.insert(a1);
		if (true) {
			throw new RuntimeException("qwe");
		}
	}
}
