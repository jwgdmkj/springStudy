package Assembler;
/* 객체 생성에 사용할 클래스를, 그 객체를 쓰는 코드를 변경하지 않은채 변경하기 위해
 * 객체를 주입하는 코드 하나만 변경시키려 한다. 실제 객체를 생성하는 코드는, 여기서 만든다
 * 객체를 생성하고, 의조 ㄴ객체를 주입하는 클래스를 따로 작성하는 방식으로
 * 가령, 회원 가입이나 암호 변경 기능을 제공하는 클래스의 객체를 생성하고, 
 * 의존 대상이 되는 객체를 주입하는 조립기 클래스
 */
import spring.ChangePasswordService;
import spring.MemberDao;
import spring.MemberRegisterService;

public class assembler {
	private MemberDao memberdao;
	private MemberRegisterService regsrv;
	private ChangePasswordService pwdsvc;
	
	//멤버레지스터서비스와 체인지패스워드서비스의 객체에 대한 의존을 주입
	//멤버레지스터는 생성자를 통해 memberdao 객체를 주입받고,
	//체인지는 세터를 통해 주입받는다.
	public void Assembler() {
		memberdao= new MemberDao();
		regsrv= new MemberRegisterService(memberdao);
		pwdsvc = new ChangePasswordService();
		pwdsvc.setMemberDao(memberdao);
	}
	
	public MemberDao getMemberDao() {
		return memberdao;
	}
	public MemberRegisterService getMemberreg() {
		return regsrv;
	}
	public ChangePasswordService getChangePwdsrv() {
		return pwdsvc;
	}
}
