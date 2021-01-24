package main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import chap07.Calculator;
import config.AppCtxWithCache;

public class MainAspectWithCache {
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = 
				new AnnotationConfigApplicationContext(AppCtxWithCache.class);

		Calculator cal = ctx.getBean("calculator", Calculator.class);
		cal.factorial(7);
		cal.factorial(7);
		cal.factorial(5);
		cal.factorial(5);
		ctx.close();
	}
}

//RecCalculator.factorial([7]) 실행시간 : 34400 ns && CacheAspect: Cache에 추가[7] : 15행 결과
//CacheAspect: Cache에서 구함[7] : 16행 결과
//RecCalculator.factorial([5]) 실행시간 : 8500 ns && CacheAspect: Cache에 추가[5] : 17행 결과
//CacheAspect: Cache에서 구함[5] : 18행 결과

/*factorial(7) 실행은 같은데, 첫 번째는 ExeTimeAspect와 CacheAspect가 다 적용, 후자는 CacheAspect만 적용됨
 * 이는, Advice를 "CacheAspect 프록시 - ExeTimeAspect 프록시 - 실제 대상객체" 순으로 적용했기 때문
 * 14행에서 구한 calculator빈은 사실 CacheAspect프록시 객체. 그런데 CacheAspect프록시 객체의 대상 객체는
 * ExeTimeASpect의 프록시 객체. 마지막으로 ExeTimeAspect프록시의 대상객체가 실제 대상객체.
 */