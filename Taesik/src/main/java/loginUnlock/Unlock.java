package loginUnlock;

import com.google.gson.Gson;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;

/**
 *  table_unlock 테이블 연동 JPA 모델 객체
 *  내부 코드 연동으로 RDB 테이블 연동 후 CRUD
 */

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "table_unlock")
public class Unlock {
	@Id
	@Column(nullable = false)
	private Long id;
	@Column(name = "create_date")
	private Long createDate;

	public Unlock setUnlock(String body) {
		Gson gson = new Gson();
		return gson.fromJson(body, Unlock.class);
	};
}
