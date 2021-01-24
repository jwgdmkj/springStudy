package spring;

public class Client2 {
	
	private String host;
	public void setHost(String host) {
		this.host= host;
	}
	
	public void connect() {
		System.out.println("Clinet2.connect() 실행");
	}
	public void send() {
		System.out.println("Client2.send() to" + host);
	}
	public void close() {
		System.out.println("Clinet2.close() 실행");
	}
}
