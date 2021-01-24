package main;

import chap07.ExeTimeCalculator;
import chap07.*;

public class MainProxy {
	public static void main(String[] args) {
		ExeTimeCalculator ttCal1 = new ExeTimeCalculator(new ImpeCalculator());
		System.out.println(ttCal1.factorial(20));
		
		ExeTimeCalculator ttCal2 = new ExeTimeCalculator(new ImpeCalculator());
		System.out.println(ttCal2.factorial(20));
	}
}

/*ExeTimeCalculator 클래스의 구현 방식: factorial()의 기능을 직접 구현하지 않고, 다른 객체에 factorial() 실행을 위임
 * 계산 기능 외의 다른 부가적 기능(여기서는 '실행시간 측정')을 실행
 * 이런, '핵심 기능의 실행을 다른 객체에 위임 후, 부가적 기능을 제공하는 객체'가 프록시.
 * ExeTimeCalculator가 프록시, ImpeCalculator 객체는 프록시의 대상객체
 */
