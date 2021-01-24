package config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

//@Compontent애노테이션이 붙은 클래스를 스캔해 스프링 빈으로 등록하려면, 설정클래스에 @Component 적용이 필수
//알아서 클래스를 검색해서 빈으로 등록하니, 일일이 import spring.xxx가 필요없음
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import spring.MemberPrinter;
import spring.MemberSummaryPrinter;
import spring.VersionPrinter;

import spring.Client;
import spring.Client2;

@Configuration
@ComponentScan(basePackages = {"spring"}) //스프링과 그 하위패키지를 대상(즉, import spring.xxx)
//스캔 대상에 해당하는 클래스 중 @Component가 붙은 클래스의 객체를 생성해 빈으로 등록

public class AppCtx {

	@Bean
	@Qualifier("printer")
	public MemberPrinter memberPrinter1() {
		return new MemberPrinter();
	}
	
	@Bean
	@Qualifier("summaryPrinter")
	public MemberSummaryPrinter memberPrinter2() {
		return new MemberSummaryPrinter();
	}
	
	@Bean
	public VersionPrinter versionPrinter() {
		VersionPrinter versionPrinter = new VersionPrinter();
		versionPrinter.setMajorVersion(5);
		versionPrinter.setMinorVersion(0);
		return versionPrinter;
	}
	
	//Client클래스를 위한 설정클래스
	@Scope("prototype") //프로토타입 빈으로 설정
	@Bean
	public Client client() {
		Client client= new Client();
		client.setHost("host");
		return client;
	}
	//Clinet2를 위한 설정클래스
	//이를 빈으로 사용하려면, 초기화 과정에서 connect를 실행, 소별에서 close 써야한다면 
	//@Bean에서 initMethod와 destroyMethod속성에 초기화와 소멸 과정에서 쓸 메서드 이름인 connect와 close지정 필수
	@Bean(initMethod= "connect", destroyMethod = "close") 
	@Scope("singletone")
	public Client2 client2() {
		Client2 client = new Client2();
		client.setHost("host");
		//client.connect(); 또는, initMethod를 실행하지 않고 이 코드를 넣을 수도 있다
		return client;
	}
}
/*
프로토타입 빈으로 설정 시, 빈 객체를 구할 때마다 매번 새로운 객체를 생서
Client client1 = ctx.getBean("client", Client.class);
Client client2 = ctx.getBean("client", Client.class);
이 때, client1 != client2 --> true. 
getBean()메서드는 매번 새로운 객체를 생성해 리턴하기에, client1과 2는 서로 다른 객체가 된다.
만일 설정 안하면, 스프링컨테이너는 빈객체를 하나만 생성하므로, 둘은 동일한 빈객체를 참조하게 된다.
만일 싱글톤 범위를 명시하려면, @Scope("signletone")을 삽입

컨테이너의 종료는 빈 객체를 소멸시키지 않는다. 만일 프로토타입 범위의 빈을 사용할 땐, 빈 객체의 소멸처리를 코드에서 직접해야함.
*/