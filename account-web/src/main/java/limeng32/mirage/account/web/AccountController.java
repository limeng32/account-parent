package limeng32.mirage.account.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import limeng32.mirage.account.persist.AccountPersistService;
import limeng32.mirage.account.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountController {

	@Autowired
	AccountPersistService accountPersistService;

	@Autowired
	AccountService accountService;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/")
	public String get(HttpServletRequest request) {
		if (request.getSession().getAttribute("accountToken") != null) {
			request.setAttribute("authToken", 5);
		}
		return "index";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/signOut")
	public String checkExist(HttpServletRequest request,
			HttpServletResponse response, ModelMap mm) throws IOException {
		request.getSession().removeAttribute("accountToken");
		request.getSession().removeAttribute("authToken");
		mm.addAttribute("_content", true);
		return AccountSignUpController.UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/signInError")
	public String signInError(HttpServletRequest request) {
		return "signInError";
	}
}
