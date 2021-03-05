package Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import controller.ChangePwdController;
import controller.LoginComtroller;
import controller.LogoutController;
import controller.MemberDetailController;
import controller.MemberListController;
import controller.RegisterController;
import controller.RestMemberController;
import spring.AuthService;
import spring.ChangePasswordService;
import spring.MemberDao;
import spring.MemberRegisterService;
import survey.SurveyController;

//RegisterContoller클래스를 빈으로 등록
@Configuration
public class ControllerConfig {
	//클래스 필드에 붙임으로써, MemberRegisterService타입의 빈을 memberRegSvc에 할당
	/* Autowired 안붙이면, 원래는 실제 실행클래스에서 세터메서드 넣는게 필수. 
	 * private MemberRegisterService memberRegSvc() {
	 * 	return new MemberRegisterService();
	 * } 여야 함.
	 */
	@Autowired
	private MemberRegisterService memberRegSvc;
	@Autowired
	private AuthService authService;
	@Autowired
	private ChangePasswordService changePasswordService;
	
	//ReigsterController의 의존객체 memberRegSvc를 set하고있다.
	@Bean
	public RegisterController registerController() {
		RegisterController controller= new RegisterController();
		controller.setMemberRegisterService(memberRegSvc);
		return controller;
	}
	
	@Bean
	public SurveyController surveyController() { 
		return new SurveyController(); 
	}
	
	//logincomtroller의 의존객체 authservice를 set하고 있다.
	@Bean
	public LoginComtroller loginController() {
		LoginComtroller controller= new LoginComtroller();
		controller.setAuthService(authService);
		return controller;
	}
	
	@Bean
	public LogoutController logoutController() {
		return new LogoutController();
	}
	
	/* 커맨드객체 생성 - 커맨드객체 검증클래스 생성 - 비밀번호 변경 요청 처리하는 컨트롤러클래스 생성
	 * 이때, 이 컨트롤러클래스는 현 로그인한 사용자 정보 구하기 위해, HttpSession의 authInfo속성 사용
	 * 이후 컨트롤러의 처리 결과를 보여줄 changePwdForm뷰와 changePwd뷰코드 작성, 후 프로퍼티파일 수정
	 * 마지막으로, ControllerConfig설정에 changePwdController를 빈으로 등록
	 * 
	 *  단, 서버 재시작시 세션정보가 유지되지 않기에, 세션에 보관된 authInfo객체정보가 삭제되므로,
	 *  ChangePwdController클래스의 AuthInfo authInfo=(AuthInfo)session.getAttribute("authInfo")
	 *  는 null을 리턴. 
	 */
	@Bean
	public ChangePwdController changePwdController() {
		ChangePwdController controller= new ChangePwdController();
		controller.setChangePasswordService(changePasswordService);
		return controller;
	}
	
	//MemberListController는 memberDao를 의존주입받으므로, Autowired해야함
	@Autowired
	private MemberDao memberDao;
	
	@Bean
	public MemberListController memberListConroller() {
		MemberListController controller= new MemberListController();
		controller.setMemberDao(memberDao);
		
		return controller;
	}
	
	@Bean
	public MemberDetailController memberDetailController() {
		MemberDetailController controller= new MemberDetailController();
		controller.setMemberDao(memberDao);
		return controller;
	}
	
	@Bean
	public RestMemberController restMemberController() {
		RestMemberController controller= new RestMemberController();
		controller.setMemberDao(memberDao);
		controller.setRegisterService(memberRegSvc);
		
		return controller;
	}
}
