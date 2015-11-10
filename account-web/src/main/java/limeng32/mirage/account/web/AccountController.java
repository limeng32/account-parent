package limeng32.mirage.account.web;

import limeng32.mirage.account.persist.AccountPersistService;
import limeng32.mirage.account.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountController {

	@Autowired
	AccountPersistService accountPersistService;

	@Autowired
	AccountService accountService;

	@RequestMapping(method = RequestMethod.GET, value = "/signInSuccess")
	public String getSignInSuccess() {
		return "signInSuccess";
	}

}
