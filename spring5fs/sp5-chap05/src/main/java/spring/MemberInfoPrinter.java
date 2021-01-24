package spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//component 애노테이션에 속성값을 준 예(@Component("infoPrinter"))
//속성값을 주지 않으면, 클래스 이름의 첫 글자가 소문자인 것이 속성값(MemberDao는 memberDao로)
@Component("infoPrinter")
public class MemberInfoPrinter {

	private MemberDao memDao;
	private MemberPrinter printer;

	public void printMemberInfo(String email) {
		Member member = memDao.selectByEmail(email);
		if (member == null) {
			System.out.println("데이터 없음\n");
			return;
		}
		printer.print(member);
		System.out.println();
	}

	@Autowired
	public void setMemberDao(MemberDao memberDao) {
		this.memDao = memberDao;
	}

	@Autowired
	@Qualifier("printer")
	public void setPrinter(MemberPrinter printer) {
		this.printer = printer;
	}

}
