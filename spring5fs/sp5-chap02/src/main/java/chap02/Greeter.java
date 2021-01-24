package chap02;

public class Greeter {
	private String format;
	
	//새로운 문자열 생성. greet()메서드에서 쓸 문자열 포맷은 setFormat()을 이용해 설정
	public String greet(String guest) {
		return String.format(format, guest);
	}
	
	public void setFormat(String format) {
		this.format=format;
	}
}
