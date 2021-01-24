package config;

//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import aspect.ExeTimeAspect;
import chap07.*;

//Aspect를 붙인 클래스를 공통기능으로 적용하기 위해, EnableAspectJAutoProxy를 설정클래스에 붙일 필요가 있다
//이를 추가시, 스프링은 @Aspect가 붙은 빈 객체를 찾아 빈 객체의 @Pointcut설정과 @Around설정을 사용한다.
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass= true)
public class AppCtx {
	@Bean
	public ExeTimeAspect exeTimeAspect() {
		return new ExeTimeAspect();
	}
	
	@Bean
	public Calculator calculator() {
		return new RecCalculator();
	}
}
/*
@Pointcut("execution(public *chap07..*(..)") 
	private void publicTrget() {
	}

@Around("publicTrget()") 
public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
}

에서, @Around는 Pointcut으로 publicTrget()메서드를 설정
publicTarget()의 @Pointcut은 chap07과 하위에 속한 빈 객체의 public 메서드를 설정
calculator은 chap07내에 속하므로ㅡ calculator 빈에 ExeTimeAspect클래스에 정의한 공통기능인 measure()을 적용
*/