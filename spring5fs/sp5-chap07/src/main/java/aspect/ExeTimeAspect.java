package aspect;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect // 공통기능을 제공하는 Aspect 구현클래스의 적용처
public class ExeTimeAspect {
	@Pointcut("execution(public * chap07..*(..))") //Aspect 애노테이션 적용클래스는 advice와 pointcut 제공
	//pointcut은 공통기능 적용할 대상을 설정. chap07패키지와 그 하위 패키지의 public 메서드를 pointcut으로 설정
		private void publicTarget() {
			
		}
	/*
	 * public void publicTarget()을 사용시, 다른 클래스에 위치한 @Around애노테이션에서 publicTarget()메서드의 
	 * Pointcut을 사용할 수 있다. 
	 * 그리고 해당 Pointcut의 완전한 클래스 이름을 포함한 매서드 이름을 @Around 애노테이션에 사용하면 된다.
	 * 예) @Aspect
	 * public class CacheAspect {
	 * 	@Around("aspect.ExeTimeAspect.publicTarget()"0
	 * 	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
	 * 	...}
	 * }
	 */
	
	@Around("publicTarget()") 
	//publicTarget()메서드에 정의한 Pointcut에 공통기능을 적용한다는 뜻. 
	//publicTarget()은 chap07패키지와 그 하위패키지에 위치한 public을 Pointcut으로 설정했으므로,
	//chap07과 그 하위패키지에 속한 빈객체의 public메서드에 @Around가 붙은 measure()메서드를 적용
	
	//measure()메서드의 ProceedingJoinPoint타입 인자는 프록시 대상 객체의 메서드 호출시 사용
	//Object result=joinPoint.preceed()처럼, preceed를 이용해 실제 대상 객체의 메서드를 호출, 실행
	//따라서 시간측정은 그 전(long start)과 후(finally long finish)에 이뤄짐
	public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.nanoTime();
		try {
			Object result = joinPoint.proceed();
			return result;
		}
		finally {
			long finish = System.nanoTime();
			Signature sig= joinPoint.getSignature();
			System.out.printf("%s.%s(%s) 실행시간 : %d ns\n",
					joinPoint.getTarget().getClass().getSimpleName(),
					sig.getName(), Arrays.toString(joinPoint.getArgs()),
					(finish-start));
		}
	}
}
//getSignature(), getTarget(), getArgs()는 호출한 메서드의 시그니처, 대상객체, 인자목록 구성에 쓰임. 
//대상객체 클래스 이름과 메서드 이름을 출력.
//이렇게 공통기능 적용에 필요한 코드 구현 완료