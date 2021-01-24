package spring;

public class ChangePasswordService {
	private MemberDao memberDao;
	
	public void changePassword(String email, String oldpw, String newpw) {
		Member member = memberDao.selectByEmail(email);
		if(member==null) {
			throw new MemberNotFoundException();
		}
		
		member.changePassword(oldpw, newpw);
		memberDao.update(member);
	}
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao=memberDao;
	}
}
