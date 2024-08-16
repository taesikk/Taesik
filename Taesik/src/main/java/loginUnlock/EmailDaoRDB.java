package loginUnlock;

import java.util.List;

public interface EmailDaoRDB {
	public void createEmailAuthInfo(Unlock unlock) throws Exception;
	public List<Unlock> getEmailAuthInfoByToEmail(String toEmail) throws Exception;
}
