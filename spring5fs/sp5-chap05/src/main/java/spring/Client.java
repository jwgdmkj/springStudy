package spring;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class Client implements InitializingBean, DisposableBean{
	private String host;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Clinet.afterPropertiesSet() 실행");
	}

	public void send() {
		System.out.println("Clinet.send() to " + host);
	}
	
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Clinet.destroy() 실행");
	}

	public void setHost(String host2) {
		// TODO Auto-generated method stub
		this.host= host;
	}
}
