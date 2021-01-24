package config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.ChangePasswordService;
import spring.MemberDao;
import spring.MemberInfoPrinter;
import spring.MemberRegisterService;
import spring.VersionPrinter;
import spring.MemberListPrinter;
import spring.MemberPrinter;
import spring.MemberRegisterService;

@Configuration //스프링 설정 클래스. 이를 붙여야 스프링 설정 클래스로 사용 가능
public class AppCtx {
	@Bean //해당 메서드가 생성한 객체를 스프링 빈으로 설정. 각각의 메서드마다 한 개의 빈 객체를 생성.
	public MemberDao memberDao() {	//즉, memberDao()를 통해 생성한 빈객체는 memberDao란 이름으로 스프링에 등록
		return new MemberDao();
	}
	
	@Bean
	public MemberRegisterService memberRegSvc() {
		return new MemberRegisterService(memberDao()); //memberDao()가 생성한 객체를 memberregisterservice 생성자 통해 주입
	}
	
	@Bean
	public ChangePasswordService changepwdsvc() { //setmemberdao()를 이용해 changepasswordservice타입의 빈을 설정, 의존객체 주입
		ChangePasswordService pwdsvc= new ChangePasswordService();
		pwdsvc.setMemberDao(memberDao());
		return pwdsvc;
	}

	//객체 생성, 의존 객체를 주입하는건 스프링 컨테이너, 따라서 설정클래스를 이용해 컨테이너 생성이 필수.
	//컨테이너 생성해야 getbean() 통해 사용할 객체 구하기 가능

	//이전에 서술한 mainforassembler을 스프링 컨테이너를 쓰도록 변경.
	
	//memerlistprinter에서 쓰일 빈
	@Bean
	public MemberPrinter memberPrinter() {
		return new MemberPrinter();
	}

	@Bean
	public MemberListPrinter listPrinter() {
		return new MemberListPrinter(memberDao(), memberPrinter());
	}
	
	//세터메서드를 이용해 멤버다오, 멤버프린터 빈을, 의존을 주입하는 설정코드
	@Bean
	public MemberInfoPrinter infoPrinter() {
		MemberInfoPrinter infoPrinter= new MemberInfoPrinter();
		infoPrinter.setMemberDao(memberDao());
		infoPrinter.setPrinter(memberPrinter());
		
		return infoPrinter;
	}
	
	@Bean
	public VersionPrinter versionPrinter() {
		VersionPrinter versionPrinter = new VersionPrinter();
		versionPrinter.setMajorV(5);
		versionPrinter.setMionrV(0);
		return versionPrinter;
	}
}
