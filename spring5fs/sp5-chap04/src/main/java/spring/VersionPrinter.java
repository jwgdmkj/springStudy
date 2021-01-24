//두 개의 int타입을 세터 메서드로 전달받는 코드
package spring;

public class VersionPrinter {
	private int majorV;
	private int minorV;
	
	public void print() {
		System.out.printf("이 프로그램 버전은 %d.%d\n", majorV, minorV);
	}
	
	public void setMajorV(int majorV) {
		this.majorV=majorV;
	}
	public void setMionrV(int minorV) {
		this.minorV= minorV;
	}
}
