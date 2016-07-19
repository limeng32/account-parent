package limeng32.mirage.account.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountBucket;
import limeng32.mirage.account.persist.AccountBucketService;
import limeng32.mirage.account.persist.AccountPersistService;
import limeng32.mirage.account.service.AccountService;
import limeng32.mirage.account.service.AccountServiceException;
import limeng32.mirage.account.service.AccountServiceExceptionEnum;
import limeng32.mirage.account.service.AliyunForAccount;
import limeng32.mirage.util.upload.UploadNamingPolicy;
import limeng32.mirage.util.upload.UploadedFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AccountController {

	private static final String relativePath = "account/";

	@Autowired
	AccountService accountService;

	@Autowired
	AccountPersistService accountPersistService;

	@Autowired
	AccountBucketService accountBucketService;

	@Autowired
	AliyunForAccount aliyun;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/")
	public String get(HttpServletRequest request) {
		return relativePath + "index";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/signOut")
	public String checkExist(HttpServletRequest request,
			HttpServletResponse response, ModelMap mm) throws IOException {
		request.getSession().removeAttribute("accountToken");
		mm.addAttribute("_content", true);
		return AccountSignUpController.UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/signInError")
	public String signInError(HttpServletRequest request) {
		return relativePath + "signInError";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/signInError/{errorName}")
	public String signInError2(HttpServletRequest request,
			@PathVariable String errorName) {
		String reason = null;
		try {
			AccountServiceExceptionEnum asee = AccountServiceExceptionEnum
					.valueOf(errorName);
			reason = asee.description();
		} catch (IllegalArgumentException e) {
			reason = "未知异常";
		}
		request.setAttribute("reason", reason);
		return relativePath + "signInError";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/signUpSuccess")
	public String signUpSuccess(HttpServletRequest request) {
		return relativePath + "signUpSuccess";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/signUpSuccess/{successName}")
	public String signUpSuccess2(HttpServletRequest request,
			@PathVariable String successName) {
		request.setAttribute("reason", AccountWebEnum.valueOf(successName)
				.description());
		return relativePath + "signUpSuccess";
	}

	@RequestMapping(method = { RequestMethod.POST }, value = "/getAccount")
	public String getAccount(HttpServletRequest request,
			HttpServletResponse response, ModelMap mm, int id)
			throws IOException {
		Account result = accountPersistService.select(id);
		mm.addAttribute("_content", result);
		return AccountSignUpController.UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = { RequestMethod.POST }, value = "/getAccountWithBucket")
	public String getAccountWithBucket(HttpServletRequest request,
			HttpServletResponse response, ModelMap mm, int id)
			throws IOException {
		Account result = accountPersistService.select(id);
		accountPersistService.loadAccountBucket(result, new AccountBucket());
		if (result.getAccountBucket().size() == 0) {
			AccountBucket newAb = new AccountBucket();
			newAb.setAccount(result);
			try {
				accountService.insertAccountBucketTransactive(newAb);
			} catch (AccountServiceException e) {
				response.sendError(400, e.getMessage());
			}
		}
		mm.addAttribute("_content", result);
		return AccountSignUpController.UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/editUser")
	public String editUser(HttpServletRequest request) {
		return relativePath + "editUser";
	}

	@RequestMapping(method = { RequestMethod.POST }, value = "/edit/savePortraitModify")
	public String savePortraitModify(HttpServletRequest request,
			HttpServletResponse response, AccountBucket accountBucket,
			String portraitModifyX, String portraitModifyY,
			String portraitModifyW, String portraitModifyH,
			String portraitModify, ModelMap mm) throws IOException {
		Integer accountToken = null;
		try {
			accountToken = Integer.parseInt(request.getSession()
					.getAttribute("accountToken").toString());
		} catch (Exception e) {
			response.sendError(400, e.getMessage());
		}
		Account account = accountPersistService.select(accountToken);
		accountBucket.setAccount(account);
		String _portraitModify = portraitModifyX + "-" + portraitModifyY + "-"
				+ portraitModifyW + "-" + portraitModifyH + "a";
		accountBucket.setPortraitModify(_portraitModify);
		try {
			accountService.updateAccountBucketTransactive(accountBucket);
		} catch (AccountServiceException e) {
			response.sendError(400, e.getMessage());
		}
		AccountBucket result = accountBucketService.select(accountBucket
				.getId());
		mm.addAttribute("_content", result);
		return AccountSignUpController.UNIQUE_VIEW_NAME;
	}

	@RequestMapping(method = { RequestMethod.POST }, value = "/uploadPortrait")
	public String uploadFile(HttpServletRequest request,
			@RequestParam("Filedata") MultipartFile file,
			HttpServletResponse response, ModelMap mm) throws IOException {
		UploadedFile result = new UploadedFile();
		if (!file.isEmpty()) {
			String fileName = aliyun.buildPortraitFileName(UploadNamingPolicy
					.dateWithUUIDName(file.getOriginalFilename()));
			AliyunForAccount.getOSSClient().putObject(aliyun.getOssBucket(),
					fileName, file.getInputStream());
			result.setStatus(1);
			result.setType("ajax");
			result.setName(file.getOriginalFilename());
			String portraitURL = aliyun.ossUrl(fileName);
			result.setUrl(portraitURL);

			AccountBucket ab = new AccountBucket();
			ab.setOriginalPortrait(portraitURL);
			Integer accountToken = null;
			try {
				accountToken = Integer.parseInt(request.getSession()
						.getAttribute("accountToken").toString());
			} catch (Exception e) {
				result.setStatus(0);
				result.setMessage(AccountServiceExceptionEnum.NoLogin
						.description());
				mm.addAttribute("_content", result);
				return AccountSignUpController.UNIQUE_VIEW_NAME;
			}
			Account account = accountPersistService.select(accountToken);
			accountPersistService.loadAccountBucket(account,
					new AccountBucket());
			if (account.getAccountBucket().size() == 0) {
				ab.setAccount(account);
				try {
					accountService.insertAccountBucketTransactive(ab);
				} catch (AccountServiceException e) {
					response.sendError(400, e.getMessage());
				}
			} else {
				AccountBucket[] accountBuckets = account.getAccountBucket()
						.toArray(
								new AccountBucket[account.getAccountBucket()
										.size()]);
				ab.setId(accountBuckets[0].getId());
				ab.setAccount(account);
				try {
					accountService.updateAccountBucketTransactive(ab);
				} catch (AccountServiceException e) {
					response.sendError(400, e.getMessage());
				}
			}
		} else {
			result.setStatus(0);
			result.setMessage("上传失败！");
		}
		mm.addAttribute("_content", result);
		return AccountSignUpController.UNIQUE_VIEW_NAME;
	}
}
