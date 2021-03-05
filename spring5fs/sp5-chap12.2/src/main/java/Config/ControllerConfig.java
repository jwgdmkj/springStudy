package Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import controller.RegisterController;
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
	
	//
	@Bean
	public RegisterController registerController() {
		RegisterController controller= new RegisterController();
		controller.setMemberRegisterService(memberRegSvc);
		return controller;
	}
	
	@Bean
	public SurveyController surveyController() { 
		return new SurveyController(); }
}
