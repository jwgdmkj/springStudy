package chap02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration= 해당 클래스를 스프링 설정 클래스로 지정.
@Configuration
public class AppContext {
	@Bean //스프링이 생성하는 객체는 빈 객체. 이 빈객체에 대한 정보를 담고 있는 메서드가 greeter()메서드
	//@Bean 어노테이션을 메서드에 붙일시, 해당 메서드가 생성한 객체를 스프링이 관리하는 빈 객체로 등록
	public Greeter greeter() {
		Greeter g= new Greeter();
		g.setFormat("%s, 안녕하세요!");
		return g;
	}
}
