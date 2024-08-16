package loginUnlock;

public class UnlockServiceImpl implements UnlockService {

	@Override
	public Unlock sendUnlockOTP(String otpVendor, String accountId, Unlock unlockVO) {
		String externalMail = unlockVO.getEmail();

		Unlock unlockResult = null;
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		long currentTime = date.getTime();

		String subId = "";

		List<unlockResult> unlockResultList = emailDaoRDB.getEmailAuthInfoByToEmail(externalMail);
		if (unlockResultList.isEmpty()) {
			unlockResult = new unlockResult();
			unlockResult.setCreateDate(currentTime);
			unlockResult.setToEmail(externalMail);
			unlockResult.setStatus("request");
			unlockResult.setRequestTime(currentTime);
			unlockResult.setAuthCode(authCode);
			unlockResult.setUpdateDate(currentTime);

			emailDaoRDB.createEmailAuthInfo(unlockResult);
		} else {
			unlockResult = unlockResultList.get(0);
			unlockResult.setStatus("request");
			unlockResult.setAuthTokenId(authId);
			unlockResult.setRequestTime(currentTime);
			unlockResult.setAuthCode(authCode);
			unlockResult.setUpdateDate(currentTime);

			emailDaoRDB.updateEmailAuthInfo(unlockResult);
		}

		if (!externalMail.isEmpty()) {
			String subject = "";
			String authDescription = "";
			Map<String, Object> pathParams = new HashMap<>();
			pathParams.put("id", subId);
			String templateBody = new TemplateBuilder()
					.formatter("authDescription", authDescription)
					.formatter("effectiveTime", 10)
					.formatter("auth_code", authCode)
					.formatter("homeLink", "")
					.build();
			NotiTemplate notifyTemplateUtil = new NotiTemplate();
			String logoUrl = "";

			settingMail.readyToSendMail(companyId,
					SettingMail.Context.builder()
							.type("")
							.templateBody(templateBody)
							.build()
			);
		}

		return unlockResult;
	}
}
