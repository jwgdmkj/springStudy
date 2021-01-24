package chap02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class Main {
	public static void main(String[] args) {
		//annotation~은 appcontext에서 정의한 @Bean 설정정보를 읽어와 greeter객체를 생성, 초기화
//		AnnotationConfigApplicationContext ctx =
//				new AnnotationConfigApplicationContext(AppContext.class);
		//getbean메서드는 annotation~이 자바설정을 읽어와 생성한 빈 객체를 검색하 때 사용
		//getbean메서드의 첫번째 파라미터는 @Bean 애노테이션의 메서드 이름인 빈 객체의 이름이고,
		//두번쨰 파라미터는 검색할 빈 객체의 타입
//		Greeter g=ctx.getBean("greeter", Greeter.class);
//		String msg=g.greet("스프링"); 
//		System.out.println(msg);
//		ctx.close();
		//7에서 설정정보 이용해 빈객체 생성, 12에서 빈객체 제공
		
		//1.컨테이너 초기화. 컨테이너는 설정 클래스(AppContext.class)에서 정보를 읽어와 알맞은 빈 객체를 생성,
		//각 빈을 연결(의존주입)
		AnnotationConfigApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppContext.class);
		//2.컨테이너에서 빈 객체 구해서 사용(getBean을 통해 컨테이너에 보관된 빈 객체("greeter"이름의 객체)를 구해ㅗㅁ)
		Greeter g1= ctx.getBean("greeter", Greeter.class);
		Greeter g2= ctx.getBean("greeter", Greeter.class);
		
		System.out.println("(g1 == g2) = " + (g1==g2));
		//3.컨테이너 종료
		ctx.close();
	}
}

/* annotation~context : 자바 어노테이션을 이용해 클래스로부터 객체설정정보 가져옴
 * GenericXmlApplicationContext - xml로부터 객체설정정보 가져옴
 * GenericGroovyApplicationContext - 그루비코드 이용해 설정정보 가져옴
 */
