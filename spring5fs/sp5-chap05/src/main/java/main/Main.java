package main;
//챕터6용 main

import java.io.IOException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import spring.Client;
import config.AppCtx;

public class Main {
	public static void main(String[] args) throws IOException {
		AbstractApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppCtx.class);
		
		Client client = ctx.getBean(Client.class);
		client.send();
		ctx.close();
	}
}

//순서- 우선 afterPropertiesSet()메소드 실행. 스프링 컨테이너는 빈 객체 생성 마무리 후 초기화 메서드 실행. 
//마지막은 destroy실행. 만약 ctx.clse()가 없다면 ,컨테이너 종료가 수행되지 않으므로 빈 객체 소멸과정도 실행되지 않음