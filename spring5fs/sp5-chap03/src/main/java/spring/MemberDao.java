package spring;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//아직 db 안배웠으니, HASHMAP 연동
public class MemberDao {
	private static long nextId=0;
	
	private Map<String, Member> map = new HashMap<>();
	
	public Member selectByEmail(String email) {
		return map.get(email);	
		}
	public void insert(Member member) {
		member.SetID(++nextId);
		map.put(member.getEM(), member);
	}
	public void update(Member member) {
		map.put(member.getEM(), member);
	}
	public Collection<Member> selectAll() {
		return map.values();
	}
}
