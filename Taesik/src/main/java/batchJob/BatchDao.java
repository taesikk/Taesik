package batchJob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
/**
 *  My Batis를 이용한 batch job Dao
 *  batch.xml에 쿼리 등록 후 사용
 *  mySql CRUD
 *
 */
public class BatchDao {
	private static final String NAMESPACE = "batchJob.batch";
	@Qualifier("sampleSqlSessionTemplate")
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public void insert(StampInfoResponse stampInfoResponse) throws Exception {
		log.debug("insert");

		Map<String, String> parameterMap = new HashMap<>();
		try {
			parameterMap.putAll(new BeanMap(stampInfoResponse));
		} catch (Exception e) {
			return;
		}
		this.sqlSessionTemplate.insert(NAMESPACE + "select", parameterMap);
	}
	public int insertBatchStart(StampSyncJob stampSyncJob) throws Exception {
		log.debug("insert BatchStartInfo");

		Map<String, String> parameterMap = new HashMap<>();
		try {
			parameterMap.putAll(new BeanMap(stampSyncJob));
		} catch (Exception e) {
			return;
		}
		int result = this.sqlSessionTemplate.insert(NAMESPACE + "insertBatchStart", parameterMap);

		return result;
	}
}
