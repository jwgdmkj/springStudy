package config;

import org.springframework.context.annotation.*;
import spring.MemberDao;
import spring.MemberPrinter;

//AppCtx의 설정을 둘로 나눠 표현
@Configuration
public class AppConf1 {
	@Bean
	public MemberDao memberDao() {
		return new MemberDao();
	}
	
	@Bean
	public MemberPrinter memberPrinter() {
		return new MemberPrinter();
	}
}
