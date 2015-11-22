package limeng32.mirage.account.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountPersistService;
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

	@Autowired
	AccountPersistService accountPersistService;

	@Autowired
	AccountService accountService;

	@RequestMapping(method = RequestMethod.GET, value = "/signIn")
	public String get() {
		return "signIn";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/signIn/checkExist", params = "email")
	public String checkExist(@RequestParam("email") String email,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap mm) throws IOException {
		Account ac = new Account();
		ac.setEmail(email);
		int count = accountPersistService.count(ac);
		boolean result = false;
		switch (count) {
		case 1:
			result = true;
			break;
		case 0:
			result = false;
			break;
		default:
			result = true;
			break;
		}
		mm.addAttribute("_content", result);
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
			account = accountService.login(ac.getEmail(), ac.getPassword());
		} catch (AccountServiceException e) {
			String errorName = "";
			switch (e.getMessage()) {
			case AccountServiceException.YourAccountHasProblem:
				errorName = "YourAccountHasProblem";
				break;
			case AccountServiceException.EmailOrPasswordIsNotExist:
				errorName = "EmailOrPasswordIsNotExist";
				break;
			case AccountServiceException.YourAccountNeedActivate:
				errorName = "YourAccountNeedActivate";
				break;
			}
			return "redirect:../signInError/" + errorName;
		}
		account.setPassword(null);
		request.getSession().setAttribute("accountToken", account.getId());
		request.getSession().setAttribute("authToken", 5);
		return "redirect:../";
	}
}
