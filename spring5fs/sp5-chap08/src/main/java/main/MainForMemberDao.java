package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.AppCtx;
import spring.MemberDao;
import spring.Member;

public class MainForMemberDao {
	private static MemberDao memberDao;
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppCtx.class);
		
		//컨테이너로부터 memberdao 빈을 구해, 정적필드인 memberDao필드에 할당
		memberDao = ctx.getBean(MemberDao.class);
		
		selectAll();	//전체 행의 개수 구함.(by memberDao.clunt())
		updateMember();	
		insertMember();
		ctx.close();
	}
	
	private static void selectAll() {
		System.out.println("----- selectAll");
		int total=memberDao.count();
		System.out.println("전체 데이터: " + total);
		
		//memberDao.selectAll()을 통해, 전체 member데이터 구한뒤 콘솔에 차례로 출력
		List<Member> members= memberDao.selectAll();
		for(Member m: members) {
			System.out.println(m.getId()+":"+m.getEmail()+":"+m.getName());
		}
	}
	
	private static void updateMember() {
		System.out.println("----- updateMember");
		//EMAIL칼럼값이 madvirus@...인 Member객체를 구하고, 임의의 비번 생성한 다음 이를 새 비번으로 함
		Member member= memberDao.selectByEmail("madviurs@madvirus.net");
		String oldPw= member.getPassword();
		String newPw= Double.toHexString(Math.random());
		member.changePassword(oldPw, newPw);
		
		//마지막으로, 이 변경된 내역의 member을 mebberDao필드에 넣어 업데이트
		memberDao.update(member);
		System.out.println("암호 변경: " + oldPw + " > " + newPw);
	}
	
	private static DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("MMddHHmmss");
	
	private static void insertMember() {
		System.out.println("----- insertMember");
		String prefix= formatter.format(LocalDateTime.now());
		
		//새로 추가할 member객체 생성, 기존과 신규데이터 구분을 위해 현재 시간을 MMddHHmm형태로 변환한 문자열을
		//이메일, 암호, 이름에 사용, prefix변수에 할당.
		//생성후엔 insert를 이용해 db에 새로운 데이터 추가. getId실행해 새로 생성된 키값을 출력
		Member member = new Member(prefix + "@test.com", prefix, prefix, LocalDateTime.now());
		
		memberDao.insert(member);
		System.out.println(member.getId() + " 데이터 추가");
	}
}
