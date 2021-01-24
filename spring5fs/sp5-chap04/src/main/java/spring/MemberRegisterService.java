package spring;

import java.time.LocalDateTime;

public class MemberRegisterService {
	private MemberDao memberDao;
	
	public MemberRegisterService(MemberDao memberDao) {
		this.memberDao=memberDao;
	}
	
	public Long regist(RegisterRequest req) {
		Member member=memberDao.selectByEmail(req.getEmail());
		if(member!=null) {
			throw new DuplicateMemberException("dup email" + req.getEmail());
		}
		
		Member newMember=new Member(
				req.getEmail(), req.getPw(),  req.getname(),
				LocalDateTime.now());
		memberDao.insert(newMember);
		return newMember.getID();
		}
	}

