package config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.MemberDao;

/* 자주 쓰는 객체를 미리 만들어 두고, 필요할 때마다 빌리고, 사용한 다음 반납하는 방식을 풀링(pooling)이라고 한다.
 * 이렇게 여러 개의 객체를 모아 둔것을 객체 풀(object pool)이라고 하고, 여러 개의 DB커넥션을 관리하는 객체가 DB 커넥션풀.
 * 커넥션 풀: 커넥션 생성 및 유지. 커넥션풀에 커넥션 요청시, 해당커넥션은 활성상태. 풀에 반환시 유휴상태.
 */

@Configuration
public class AppCtx {
	
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		DataSource ds = new DataSource();
		//jdbc 드라이버 클래스 지정 & MySQL 드라이버 클래스 사용
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		//jdbc url 지정.db와 테이블의 캐릭터셋을 utf-8로 설정했으니, characterEncoding 파라미터를 이용해
		//mysql에 연결시, 사용할 캐릭터셋을 utf-8로 지정
		ds.setUrl("jdbc:mysql://localhost/spring5fs?characterEncoding=utf8");
		//db에 연결시 사용할 계정과 암호
		ds.setUsername("spring5");
		ds.setPassword("spring5");
		ds.setInitialSize(2);	//커넥션 퓰 초기화 시 생성할 초기 커낵션 개수
		ds.setMaxActive(10);	//커넥션풀에서 가져올 수 있는 최대 커넥션 개수
		
		/* DBMS는 일정시간 내 쿼리를 실행하지 않으면 DB연결이 끊긴다. 5분넘게 특정커넥션이 유휴상태일 시,
		 * DBMS가 해당 커넥션 연결을 끊지만, 여전히 커넥션은 풀에 남은 상태. 이 때, 이 커넥션을 풀에서 가져돠 사용시,
		 * 연결이 끊어진 커넥션이므로 익셉션이 발생.
		 * 이를 방지하기 위해, 10초 주기로 유휴커넥션이 유효한지 확인(setTestWhileIdle)
		 * 최소유휴시간을 3분으로 지정(setMinEvictableIdleTimeMillis)
		 */
		ds.setTestWhileIdle(true); //유휴커넥션 검사
		ds.setMinEvictableIdleTimeMillis(1000*60*3); //최소유휴시간 3분
		ds.setTimeBetweenEvictionRunsMillis(1000*10); //10초주기
		return ds;
	}
	
	@Bean
	public MemberDao memberDao() {
		return new MemberDao(dataSource());
	}
}
