package limeng32.mirage.account.web;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import limeng32.mirage.account.captcha.AccountCaptchaException;
import limeng32.mirage.account.captcha.AccountCaptchaService;
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
@RequestMapping(value = "/signUp")
public class AccountSignUpController {

	public static final String UNIQUE_VIEW_NAME = "__unique_view_name";

	@Autowired
	AccountPersistService accountPersistService;

	@Autowired
	AccountService accountService;

	@Autowired
	AccountCaptchaService accountCaptchaService;

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		return "signUp";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/captcha")
	public String getCaptcha(ModelMap mm, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			String captchaText = accountCaptchaService
					.generateCaptchaKeyNew(request.getRemoteHost());
			mm.addAttribute("_content", captchaText);
		} catch (AccountCaptchaException e) {
			response.sendError(400, e.getMessage());
		}
		return UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/captchaImage")
	public void getCaptchaImage(ModelMap mm, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");
		try {
			String captchaText = accountCaptchaService
					.generateCaptchaKeyNew(request.getRemoteHost());
			ServletOutputStream out = response.getOutputStream();
			out.write(accountCaptchaService
					.generateCaptchaImageNew(captchaText));
			out.close();
		} catch (IOException | AccountCaptchaException e) {
			response.sendError(400, e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/verify", params = "captchaValue")
	public String verifyCaptcha(
			@RequestParam("captchaValue") String captchaValue,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap mm) throws IOException {
		boolean result = false;
		try {
			result = accountCaptchaService.validateCaptchaNew(
					request.getRemoteAddr(), captchaValue);
		} catch (AccountCaptchaException e) {
			response.sendError(400, e.getMessage());
		}
		mm.addAttribute("_content", result);
		return UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/submit", params = "captchaValue")
	public String signUpSubmit(
			@RequestParam("captchaValue") String captchaValue,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap mm, Account account) throws IOException {
		boolean result = false;
		try {
			accountService.signUpNew(account, captchaValue,
					request.getRemoteAddr());
			result = true;
		} catch (AccountServiceException e) {
			response.sendError(400, e.getMessage());
		}
		mm.addAttribute("_content", result);
		return UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/checkUnique", params = "email")
	public String checkUnique(@RequestParam("email") String email,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap mm) throws IOException {
		Account ac = new Account();
		ac.setEmail(email);
		int count = accountPersistService.count(ac);
		boolean result = false;
		if (count == 0) {
			result = true;
		}
		mm.addAttribute("_content", result);
		return UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/captCheck", params = "captValue")
	public String captCheck(@RequestParam("captValue") String captValue,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap mm) throws IOException {
		boolean result = false;
		try {
			result = accountCaptchaService.checkCaptcha(
					request.getRemoteAddr(), captValue);
		} catch (AccountCaptchaException e) {
			response.sendError(400, e.getMessage());
		}
		mm.addAttribute("_content", result);
		return UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/submitNew", params = "captchaValue")
	public String signUpSubmitNew(HttpServletRequest request,
			HttpServletResponse response, ModelMap mm, Account account,
			@RequestParam("captchaValue") String captchaValue)
			throws IOException {
		boolean result = false;
		try {
			accountService.signUpNew(account, captchaValue,
					request.getRemoteAddr());
			result = true;
		} catch (AccountServiceException e) {
			response.sendError(400, e.getMessage());
		}
		System.out.println(":" + result);
		mm.addAttribute("_content", "");
		return UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/activate", params = {
			"k", "v" })
	public String activateAccount(@RequestParam("k") String activationKey,
			@RequestParam("v") String activationValue,
			HttpServletResponse response) throws IOException {
		try {
			accountService.activate(activationKey, activationValue);
		} catch (AccountServiceException e) {
			response.sendError(400, e.getMessage());
		}
		return null;
	}
}
