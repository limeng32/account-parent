package limeng32.mirage.account.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.service.AccountService;
import limeng32.mirage.account.service.AccountServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountSignInController {

	private static final String relativePath = "account/";

	@Autowired
	AccountService accountService;

	@RequestMapping(method = RequestMethod.GET, value = "/signIn")
	public String get() {
		return relativePath + "signIn";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/signIn/checkExist", params = "email")
	public String checkExist(@RequestParam("email") String email,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap mm) throws IOException {
		try {
			mm.addAttribute("_content", accountService.checkExist(email));
		} catch (AccountServiceException e) {
			response.sendError(400, e.getMessage());
		}
		return AccountSignUpController.UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/signIn/signInTest")
	public String signInTest(Account account, HttpServletRequest request,
			HttpServletResponse response, ModelMap mm) throws IOException {
		try {
			mm.addAttribute(
					"_content",
					accountService.test(account.getEmail(),
							account.getPassword()));
		} catch (AccountServiceException e) {
			response.sendError(400, e.getMessage());
		}
		return AccountSignUpController.UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/signIn/submitNew")
	public String submitNew(Account ac, HttpServletRequest request,
			HttpServletResponse response, ModelMap mm) throws IOException {
		Account account = null;
		try {
			account = accountService.login(ac.getEmail(), ac.getPassword(),
					request.getRemoteAddr());
		} catch (AccountServiceException e) {
			String errorName = e.getMessage();
			return "redirect:../signInError/" + errorName;
		}
		account.setPassword(null);
		request.getSession().setAttribute("accountToken", account.getId());
		return "redirect:../home";
	}
}
