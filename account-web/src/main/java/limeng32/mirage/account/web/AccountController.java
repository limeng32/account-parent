package limeng32.mirage.account.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import limeng32.mirage.account.persist.AccountPersistService;
import limeng32.mirage.account.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/signInError/{errorName}")
	public String signInError2(HttpServletRequest request,
			@PathVariable String errorName) {
		switch (errorName) {
		case "YourAccountHasProblem":
			request.setAttribute("reason", "您的帐号出现问题，现在无法登陆，具体情况请联系管理员。");
			break;
		case "EmailOrPasswordIsNotExist":
			request.setAttribute("reason", "您输入的帐号或者密码并不存在。");
			break;
		case "YourAccountNeedActivate":
			StringBuffer reasonBuffer = new StringBuffer(
					"您的帐号还没有激活，请登录您的注册邮箱进行激活。");
			request.setAttribute("reason", reasonBuffer.toString());
			break;
		case "ActivateFail":
			request.setAttribute("reason", "您的邮箱激活失败，请与管理员联系。");
			break;
		case "ActivateMismatch":
			request.setAttribute("reason", "您的邮箱与激活码不匹配，未能完成激活。");
			break;
		case "ActivateRepetition":
			request.setAttribute("reason", "您的邮箱已经是激活状态，无需再次激活。");
			break;
		case "ActivateNonentityEmail":
			request.setAttribute("reason", "您要激活的邮箱并不存在。");
			break;
		}
		return "signInError";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/signUpSuccess")
	public String signUpSuccess(HttpServletRequest request) {
		return "signUpSuccess";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/signUpSuccess/{successName}")
	public String signUpSuccess2(HttpServletRequest request,
			@PathVariable String successName) {
		switch (successName) {
		case "ActivateSuccess":
			request.setAttribute("reason", "恭喜您激活成功，现在您可以使用这个邮箱登录了");
			break;
		}
		return "signUpSuccess";
	}
}
