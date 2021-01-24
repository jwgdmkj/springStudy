package spring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import config.ManualBean;

//component: 스프링이 검색해서 빈으로 등록하기 위함. component는 해당 클래스를 스캔 대상으로 표시함
@ManualBean
@Component
public class MemberDao {

	private static long nextId = 0;

	private Map<String, Member> map = new HashMap<>();

	public Member selectByEmail(String email) {
		return map.get(email);
	}

	public void insert(Member member) {
		member.setId(++nextId);
		map.put(member.getEmail(), member);
	}

	public void update(Member member) {
		map.put(member.getEmail(), member);
	}

	public Collection<Member> selectAll() {
		return map.values();
	}
}
