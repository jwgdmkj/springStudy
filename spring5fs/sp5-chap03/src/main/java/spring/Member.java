package spring;

import java.time.LocalDateTime;
public class Member {
	private Long id;
	private String password;
	private String email;
	private String name;
	
	private LocalDateTime registerDateTime;
	
	public Member(String password, String email,
	String name, LocalDateTime registerDateTime) {
		this.email = email;
		this.name=name;
		this.password=password;
		this.registerDateTime=registerDateTime;
	}
	
	void SetID(Long id) {
		this.id=id;
	}
	public Long getID() {
		return id;
	}
	
	public String getPw() {
		return password;
	}
	public String getEM() {
		return email;
	}
	public String getName() {
		return name;
	}
	public LocalDateTime getDate() {
		return registerDateTime;
	}
	
	public void changePassword(String oldpw, String newpw) {
		if(!password.equals(oldpw)) {
			throw new WrongIdPasswrodException();
		}
		this.password=newpw;
	}
}
