package main;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import chap07.Calculator;
import config.AppCtx;
import chap07.RecCalculator;

public class MainAspect {
public static void main(String[] args) {
	AnnotationConfigApplicationContext ctx = 
			new AnnotationConfigApplicationContext(AppCtx.class);
	
	RecCalculator cal= ctx.getBean("calculator", RecCalculator.class);
//	Calculator cal = ctx.getBean("calculator", Calculator.class);
	long fiveFact= cal.factorial(5);
	System.out.println("cal.factorial(5) = " + fiveFact);
	System.out.println(cal.getClass().getName());
	ctx.close();
}
}
