package config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.ChangePasswordService;
import spring.MemberInfoPrinter;
import spring.MemberListPrinter;
import spring.MemberPrinter;
import spring.MemberDao;
import spring.MemberRegisterService;
import spring.VersionPrinter;

@Configuration
public class AppConf2 {
	
	//Autowired는 스프링의 자동주입기능을 위함. 이를 붙인 타입의 빈을 찾아 필드에 할당
	//즉, 스프링컨테이너는 멤버다오 타입의 빈을 멤버다입 필드에 할당
	//appconf1클래스에 멤버다오 타입의 빈을 설정, 따라서 appconf2클래스의 멤버다오필드엔 appconf1클래스에서 설정한 빈이 할당
	
	//autowired 애노테이션 통해 다른 설정 파일에 정의한 빈을 필드에 할당시, 설정 메서드에서 이 필드를 사용해 필요한 빈을 주입하면 됨
	//밑에서, 필드로 주입받은 빈 객체를 생성자를 이용해 주입하는 걸 알 수 있음
	//이때, ctx=new AnnotationConfigApplicationContext(AppConf1.class, AppConf2.class)로 표현가능
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberPrinter memberprinter;
	
	@Bean
	public MemberRegisterService memberRegsvc() {
		return new MemberRegisterService(memberDao);
	}
	
	@Bean
	public ChangePasswordService changepwdsvc() {
		ChangePasswordService pwdsvc= new ChangePasswordService();
		pwdsvc.setMemberDao(memberDao);
		return pwdsvc;
	}
	
	@Bean
	public MemberListPrinter listPrinter() {
		return new MemberListPrinter(memberDao, memberprinter);
	}
	
	@Bean
	public MemberInfoPrinter infoprinter() {
		MemberInfoPrinter infoPrinter= new MemberInfoPrinter();
		infoPrinter.setMemberDao(memberDao);
		infoPrinter.setPrinter(memberprinter);
		return infoPrinter;
	}
	
	@Bean
	public VersionPrinter versionprinter() {
		VersionPrinter versionprinter = new VersionPrinter();
		versionprinter.setMajorV(5);
		versionprinter.setMionrV(0);
		return versionprinter;
	}
}
