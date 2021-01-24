//setter method를 통해 의존 객체를 주입
package spring;

public class MemberInfoPrinter {
	private MemberDao memDao;
	private MemberPrinter printer;
	
	public void printMemberInfo(String email) {
		//이메일을 받으면, 멤버 객체에 이메일을 통해 알아낸 해당 멤버의 정보를 담음
		Member member = memDao.selectByEmail(email);
		if(member==null) {
			System.out.println("데이터 없음n");
			return;
		}
		printer.print(member);
		System.out.println();
	}
	
	//두개의 세터 메서드를 정의. 각각은 memberdao타입의 객체와 memberprinter타입의 객체에 대한 의존을 주입
	public void setMemberDao(MemberDao memberDao) {
		this.memDao=memberDao;
	}
	
	public void setPrinter(MemberPrinter printer) {
		this.printer=printer;
	}
}
