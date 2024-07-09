package batchJob;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@PropertySource(value = "classpath:batch.properties", ignoreResourceNotFound = true)
public class BatchServiceImpl implements BatchcService {
	private static final Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);

	@Autowired
	private BatchDao batchDao;
	@Value("${properties.baseUrl}")
	private String baseUrl;
	@Value("${properties.clientId}")
	private String clientId;

	@Scheduled(cron = "${cron-expression}")
	@Override
	public void createJob() throws Exception {
		logger.debug("StampSync - createJob");
		long currentTime = LocalDate.now()
				.atStartOfDay(ZoneId.systemDefault())
				.toInstant()
				.toEpochMilli();
		int batchCheck = batchDao.insertBatchStart(new StampSyncJob(String.valueOf(currentTime), "D", null, null));
		if (batchCheck == 0) {
			return;
		}

		try {
			// 마당 api 요청 및 반환
			String sealInfo = connectionSealInfo(baseUrl, clientId, clientSecret, grantType, scope);

			if ("".equals(sealInfo)) {
				return;
			}

			// response 파싱
			JsonParser parser = new JsonParser();
			Object obj = parser.parse(sealInfo);
			JsonObject joResponseSample = (JsonObject) obj;
			JsonObject joResult = joResponseSample.get("result").getAsJsonObject().get("ifRslt").getAsJsonObject();
			JsonArray jaSeal = joResult.get("seal").getAsJsonArray();
			List<StampInfoResponse> stampInfoResponseList = new ArrayList<>();
			for (int i = 0; i < jaSeal.size(); i++) {
				stampInfoResponseList.add(new StampInfoResponse().setStampInfoResponse(jaSeal.get(i).toString()));
			}

			// rdb 업데이트
			syncStampList(stampInfoResponseList, currentTime, companyId, memberId);

			// 동기화 작업 완료
			this.batchDao.updateBatchInfo(new StampSyncJob(String.valueOf(currentTime), "S", String.valueOf(stampInfoResponseList.size()), "Sync Complete"));
		} catch (Exception exception) {
			this.batchDao.updateBatchInfo(new StampSyncJob(String.valueOf(currentTime), "F", null, exception.getMessage()));
		}
	}



	private String getAccessToken(String baseUrl, String clientId, String clientSecret, String grant_type, String scope) throws Exception {
		String result = "";
		String tokenUrl = baseUrl + "/oauth2/token";
		try {
			URL url =  new URL(tokenUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");

			String params = "";

			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = params.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			String response = getHttpUrlConnectionResult(conn);
			JsonParser parser = new JsonParser();
			JsonObject jo = (JsonObject) parser.parse(response);
			result = jo.get("access_token").getAsString();
		} catch (Exception e) {
			logger.error("URL: {}. client_id: {}", tokenUrl, clientId);
			return;
		}

		return result;
	}

	private String getHttpUrlConnectionResult(HttpURLConnection conn) throws Exception, IOException{
		String result = "";
		int responseCode = conn.getResponseCode();
		if (responseCode == 200) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				result = response.toString();
				logger.debug("Response Code : " + responseCode);
				logger.debug("Response : " + result);
			}
		} else {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
				StringBuilder errorResponse = new StringBuilder();
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					errorResponse.append(responseLine.trim());
				}
				logger.debug("Error Code : " + responseCode);
				logger.debug("Error Response: " + errorResponse.toString());
				return;
			}
		}
		return result;
	}

	private void syncStampList(List<StampInfoResponse> stampInfoResponseList, long currentTime, String companyId, String memberId) throws Exception, IOException, ParseException {
		for (int i = 0; i < stampInfoResponseList.size(); i++) {
			String sealNo = stampInfoResponseList.get(i).getSealNo();
			String responseDate = stampInfoResponseList.get(i).getUpdDttm();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			Date date = dateFormat.parse(responseDate);
			long updateTime = date.getTime();
			List<StampInfoResponse> stampList = this.batchDao.getStampSyncInfoList(sealNo);

			if (stampList.size() > 0) {
				String stampId = stampList.get(0).getStamp_id();
				String newStampId = "";
				if (updateTime > Long.parseLong(stampList.get(0).getUpdDttm())){

				// rdb 업데이트
				if (newStampId != null) {
					stampInfoResponseList.get(i).setUpdDttm(String.valueOf(updateTime));
					stampInfoResponseList.get(i).setBatch_time(String.valueOf(currentTime));
					this.batchDao.update(stampInfoResponseList.get(i));
				}
			} else {
				// rdb 업데이트
				if (newStampId != null) {
					stampInfoResponseList.get(i).setStamp_id(newStampId);
					stampInfoResponseList.get(i).setUpdDttm(String.valueOf(updateTime));
					stampInfoResponseList.get(i).setBatch_time(String.valueOf(currentTime));
					this.batchDao.insert(stampInfoResponseList.get(i));
				}

			}
		}
		// 현재 배치 시간보다 작으면 delete
		this.batchDao.delete(currentTime);
	}


	private boolean validatePath(String stampPath) {
		long limitSize = 100000;
		if (Base64.decodeBase64(stampPath).length > limitSize) {
			return false;
		} else {
			return true;
		}
	}
}
