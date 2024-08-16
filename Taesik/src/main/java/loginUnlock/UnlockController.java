package loginUnlock;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/unlock")
public class UnlockController {

	/**
	 * js 파일에서 ajax 통신으로 넘어온 요청 받는 controller
	 * Restful API (POST, GET)
	 */
	@Autowired
	UnlockService unlockService;
	@RequestMapping(value = "/controller", method = RequestMethod.POST)
	public Map<String, String> unlock(
			HttpServletRequest request, HttpServletResponse response, Locale locale, @RequestParam(required = false) String lang,
			@PathVariable String accountId, @PathVariable String otpVendor,
			@RequestBody String requestBody
	) throws Exception {


		Unlock unlockVO = new Unlock(requestBody);
		Unlock unlockOTP = unlockService.sendUnlockOTP(otpVendor, accountId, unlockVO);
		if (unlockOTP == null) {
			return;
		}
		List<Unlock> unlockList = daoRDB.getEmailAuthInfoByToEmail(accountId);

		return ResponseEntity.status(HttpStatus.OK).body(Body.build());
	}

	public List<Unlock> getTableUnlock(String createDate) throws Exception {
		final String jpql = "SELECT p " +
				"FROM Unlock p " +
				"WHERE p.createDate = :createDate " +
				"ORDER BY p.createDate DESC";

		EntityManager entityManager = this.manager(null);
		return TX.transactional(entityManager, (manager, transaction) -> {
			TypedQuery<Unlcok> query = manager.createQuery(jpql, Unlock.class)
					.setParameter("createDate", createDate);
			return query.getResultList();
		});
	}
}
