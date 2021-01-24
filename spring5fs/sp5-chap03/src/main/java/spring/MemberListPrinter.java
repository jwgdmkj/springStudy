package spring;
import java.util.Collection;

//생성자로 두 개의 파라미터를 전달받는 클래스. DAO와 PRINTER객체를 받음
public class MemberListPrinter {
	private MemberDao memberDao;
	private MemberPrinter printer;
	
	public MemberListPrinter(MemberDao memberDao, MemberPrinter printer) {
		this.memberDao= memberDao;
		this.printer = printer;
	}
	
	public void printAll() {
		Collection<Member> members = memberDao.selectAll();
		members.forEach(m->printer.print(m));
	}
}
