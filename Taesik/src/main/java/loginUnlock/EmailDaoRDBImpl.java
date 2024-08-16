package loginUnlock;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmailDaoRDBImpl implements EmailDaoRDB {
	@Override
	public void createEmailAuthInfo(Unlock unlock) throws Exception {
		EntityManager entityManager = this.manager(unlock.getAuthTokenId());
		TX.transactional(entityManager, (manager, transaction) -> {
			manager.persist(unlock);
			return true;
		});
	}

	@Override
	public List<Unlock> getEmailAuthInfoByToEmail(String toEmail) throws Exception {
		final String jpql = "SELECT p " +
				"FROM Unlock p " +
				"WHERE p.toEmail = :toEmail " +
				"ORDER BY p.createDate DESC";

		EntityManager entityManager = this.manager(null);
		return TX.transactional(entityManager, (manager, transaction) -> {
			TypedQuery<unlock> query = manager.createQuery(jpql, unlock.class)
					.setParameter("toEmail", toEmail);
			return query.getResultList();
		});
	}

}
