package aspect;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect //aspect가 있는 클래스는 공통으로 쓰일 것들
public class CacheAspect {

	@Around("execution(public * chap07..*(..))")
	
//	private Map<Long, Object> cache = new HashMap<>();
	
//	@Pointcut("execution(public * chap07..*(long))")
//	public void cacheTarget() {	
//	}
	
//	@Around("cacheTarget()")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		Long num = (Long)joinPoint.getArgs()[0];	//첫번째 인자를 long타입으로 구함
		if(cache.containsKey(num)) {	//위에서 구한 키값이 cache에 존재하면, 키에 해당하는 값을 구해 리턴
			System.out.printf("CacheAspect: Cache에서 구함[%d]\n", num);
			return cache.get(num);
		}
		
		Object result = joinPoint.proceed(); //long num 키값이 cache에 부재하면, 프록시 대상 객체를 실행
		cache.put(num,  result); //프록시 대상 객체의 결과(result)를 cache에 추가
		System.out.printf("CacheAspect: Cache에 추가[%d]\n", num);
		return result; //프록시 대상 객체의 실행결과를 리턴
	}
}

/*
@Around값은 cacheTarget. @Pointcut 설정은 첫 번쨰 인자가 long인 메서드를 대상으로 한다. 따라서 execute()메서드는
Calculator의 factorial(long)메서드에 적용된다.
새 Aspect를 구현했으니, 스프링 설정 클래스에서 두 개의 Aspect 추가가 가능. 두 Aspect에서 설정한 pointcut은 모두 
Calculator타입의 factorial()메서드에 적용됨
*/