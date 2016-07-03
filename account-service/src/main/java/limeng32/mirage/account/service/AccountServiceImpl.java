package limeng32.mirage.account.service;

import java.util.Collection;
import java.util.Date;

import limeng32.mirage.account.captcha.AccountCaptchaException;
import limeng32.mirage.account.captcha.AccountCaptchaService;
import limeng32.mirage.account.captcha.RandomGenerator;
import limeng32.mirage.account.email.AccountEmailException;
import limeng32.mirage.account.email.AccountEmailService;
import limeng32.mirage.account.persist.Account;
import limeng32.mirage.account.persist.AccountBucket;
import limeng32.mirage.account.persist.AccountBucketService;
import limeng32.mirage.account.persist.AccountPersistService;
import limeng32.mirage.account.persist.LoginLog;
import limeng32.mirage.account.persist.LoginLogService;
import limeng32.mybatis.mybatisPlugin.util.ReflectHelper;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountPersistService accountPersistService;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private AccountEmailService accountEmailService;

	@Autowired
	private AccountCaptchaService accountCaptchaService;

	@Autowired
	private AccountServiceConfig accountServiceConfig;

	@Autowired
	private AccountBucketService accountBucketService;

	@Override
	public void activate(String activationKey, String activationValue)
			throws AccountServiceException {
		if (activationKey == null || activationValue == null) {
			throw new AccountServiceException(
					"ActivationKey or activationValue is Null.");
		}
		Account ac = new Account();
		ac.setEmail(activationKey);
		ac.setActivateValue(activationValue);
		Collection<Account> accountC = accountPersistService.selectAll(ac);
		switch (accountC.size()) {
		case 1:
			Account account = accountC.toArray(new Account[1])[0];
			if (account.getActivated()) {
				throw new AccountServiceException(
						AccountServiceExceptionEnum.ActivateRepetition
								.toString());
			}
			account.setActivated(true);
			account.setActivateValue("");
			accountPersistService.update(account);
			break;
		case 0:
			throw new AccountServiceException(
					AccountServiceExceptionEnum.ActivateMismatch.toString());
		default:
			throw new AccountServiceException(
					AccountServiceExceptionEnum.ActivateFail.toString());
		}
	}

	@Override
	public Account login(String email, String password, String remoteAddress)
			throws AccountServiceException {
		if (email == null || password == null) {
			throw new AccountServiceException("Email or password is Null.");
		}
		Account ac = new Account();
		ac.setEmail(email);
		ac.setPassword(DigestUtils.md5Hex(password));
		Collection<Account> accountC = accountPersistService.selectAll(ac);
		switch (accountC.size()) {
		case 1:
			Account account = accountC.toArray(new Account[1])[0];
			if (account.getActivated()) {
				accountPersistService.loadLoginLog(account, new LoginLog());
				switch (account.getLoginLog().size()) {
				case 1:
					LoginLog[] logs = account.getLoginLog().toArray(
							new LoginLog[account.getLoginLog().size()]);
					logs[0].setLoginIP(remoteAddress);
					logs[0].setLoginTime(new Date());
					loginLogService.update(logs[0]);
					return account;
				default:
					throw new AccountServiceException(
							AccountServiceExceptionEnum.CannotFindLoginLog
									.toString());
				}
			} else {
				throw new AccountServiceException(
						AccountServiceExceptionEnum.YourAccountNeedActivate
								.toString());
			}
		case 0:
			throw new AccountServiceException(
					AccountServiceExceptionEnum.EmailOrPasswordIsNotExist
							.toString());
		default:
			throw new AccountServiceException(
					AccountServiceExceptionEnum.YourAccountHasProblem
							.toString());
		}
	}

	@Override
	@Transactional(rollbackFor = { AccountServiceException.class }, readOnly = false, propagation = Propagation.REQUIRED)
	public void signUpNew(Account account, String captchaValue, String remoteIP)
			throws AccountServiceException {
		String originPassword = account.getPassword();
		try {
			if (!accountCaptchaService.validateCaptchaNew(remoteIP,
					captchaValue)) {
				throw new AccountServiceException("Incorrect Captcha.");
			}

			String activationId = RandomGenerator.getRandomString();
			account.setActivateValue(activationId);
			account.setActivated(false);
			account.setPassword(DigestUtils.md5Hex(account.getPassword()));
			if (account.getName() == null) {
				account.setName(account.getEmail().substring(0,
						account.getEmail().indexOf("@")));
			}
			insertAccountTransactive(account);
			LoginLog log = new LoginLog();
			log.setAccount(account);
			insertLoginLogTransactive(log);
			String link = accountServiceConfig.getActivateUrl().endsWith("/") ? accountServiceConfig
					.getActivateUrl() + account.getEmail() + "/" + activationId
					: accountServiceConfig.getActivateUrl() + "?k="
							+ account.getEmail() + "&v=" + activationId;
			accountEmailService.sendMail(account.getEmail(),
					accountServiceConfig.getActivateEmailSubject(),
					accountServiceConfig.getActivateEmailBody() + "<a href='"
							+ link + "'>" + link + "</a>");
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to validate captcha.", e);
		} catch (AccountEmailException e) {
			throw new AccountServiceException(
					"Unable to send actiavtion mail.", e);
		} finally {
			account.setPassword(originPassword);
		}
	}

	@Override
	public boolean test(String email, String password)
			throws AccountServiceException {
		Account ac = new Account();
		ac.setEmail(email);
		ac.setPassword(DigestUtils.md5Hex(password));
		int count = accountPersistService.count(ac);
		switch (count) {
		case 1:
			return true;
		case 0:
			return false;
		default:
			return true;
		}
	}

	@Override
	@Transactional(rollbackFor = { AccountServiceException.class }, readOnly = false, propagation = Propagation.REQUIRED)
	/**新增account时确保email是唯一的*/
	public void insertAccountTransactive(Account account)
			throws AccountServiceException {
		accountPersistService.insert(account);
		Account ac = new Account();
		ac.setEmail(account.getEmail());
		int c = accountPersistService.count(ac);
		if (c > 1) {
			throw new AccountServiceException(
					AccountServiceExceptionEnum.RepetitionEmail.toString());
		}
	}

	@Override
	public String generateCaptchaKeyNew(String remoteIP)
			throws AccountServiceException {
		try {
			return accountCaptchaService.generateCaptchaKeyNew(remoteIP);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException(
					"Unable to generate Captcha Key.", e);
		}
	}

	@Override
	public byte[] generateCaptchaImageNew(String captchaText)
			throws AccountServiceException {
		try {
			return accountCaptchaService.generateCaptchaImageNew(captchaText);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException(
					"Unable to generate Captcha Image.", e);
		}
	}

	@Override
	public boolean validateCaptchaNew(String remoteIP, String captchaValue)
			throws AccountServiceException {
		try {
			return accountCaptchaService.validateCaptchaNew(remoteIP,
					captchaValue);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to validate Captcha.", e);
		}
	}

	@Override
	public boolean checkCaptcha(String remoteIP, String captchaValue)
			throws AccountServiceException {
		try {
			return accountCaptchaService.checkCaptcha(remoteIP, captchaValue);
		} catch (AccountCaptchaException e) {
			throw new AccountServiceException("Unable to check Captcha.", e);
		}
	}

	@Override
	public boolean checkExist(String email) throws AccountServiceException {
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
		return result;
	}

	@Override
	public boolean checkUnique(String email) throws AccountServiceException {
		Account ac = new Account();
		ac.setEmail(email);
		int count = accountPersistService.count(ac);
		boolean result = false;
		if (count == 0) {
			result = true;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = { AccountServiceException.class }, readOnly = false, propagation = Propagation.REQUIRED)
	/**新增loginlog时确保accountId是唯一的*/
	public void insertLoginLogTransactive(LoginLog loginLog)
			throws AccountServiceException {
		if (loginLog.getAccount() == null) {
			return;
		}
		loginLogService.insert(loginLog);
		LoginLog logc = new LoginLog();
		logc.setAccount(loginLog.getAccount());
		int c = loginLogService.count(logc);
		if (c != 1) {
			throw new AccountServiceException(
					AccountServiceExceptionEnum.FailedToInsertLoginLog
							.toString());
		}
	}

	@Override
	@Transactional(rollbackFor = { AccountServiceException.class }, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void updateAccountBucketTransactive(int accountBucketId,
			AccountBucket accountBucket) throws AccountServiceException {
		if (accountBucket == null || accountBucketId <= 0) {
			throw new AccountServiceException(
					AccountServiceExceptionEnum.CannotFindAccountBucket
							.toString());
		}
		if (accountBucket.getAccount() == null
				|| accountPersistService.select(accountBucket.getAccount()
						.getId()) == null) {
			throw new AccountServiceException(
					AccountServiceExceptionEnum.CannotFindAccount.toString());
		}
		Integer temp = accountBucket.getId();
		try {
			ReflectHelper.setValueByFieldName(accountBucket, "id",
					accountBucketId);
			if (accountBucketService.update(accountBucket) != 1) {
				throw new AccountServiceException(
						AccountServiceExceptionEnum.ConnotUpdateAccountBucket
								.toString());
			}
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			throw new AccountServiceException(e.getMessage());
		} finally {
			try {
				ReflectHelper.setValueByFieldName(accountBucket, "id", temp);
			} catch (SecurityException | NoSuchFieldException
					| IllegalArgumentException | IllegalAccessException e1) {
				throw new AccountServiceException(e1.getMessage());
			}

		}
	}

	@Override
	@Transactional(rollbackFor = { AccountServiceException.class }, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void insertAccountBucketTransactive(AccountBucket accountBucket)
			throws AccountServiceException {
		if (accountBucket == null) {
			throw new AccountServiceException(
					AccountServiceExceptionEnum.CannotFindAccountBucket
							.toString());
		}
		if (accountBucket.getAccount() == null
				|| accountPersistService.select(accountBucket.getAccount()
						.getId()) == null) {
			throw new AccountServiceException(
					AccountServiceExceptionEnum.CannotFindAccount.toString());
		}
		accountBucketService.insert(accountBucket);
		AccountBucket abc = new AccountBucket();
		abc.setAccount(new Account());
		try {
			ReflectHelper.setValueByFieldName(abc.getAccount(), "id",
					accountBucket.getAccount().getId());
		} catch (SecurityException | NoSuchFieldException
				| IllegalArgumentException | IllegalAccessException e) {
			throw new AccountServiceException(e.getMessage());
		}
		int c = accountBucketService.count(abc);
		if (c > 1) {
			throw new AccountServiceException(
					AccountServiceExceptionEnum.RepetitionAccountBucket
							.toString());
		}
	}
}
