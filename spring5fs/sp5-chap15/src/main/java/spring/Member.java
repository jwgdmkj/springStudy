package spring;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Member {

	private Long id;
	private String email;
	/* RestMemberController을 통해, 유저 목록을 JSON을 통해 볼 수 있다. 그러나
	 * password도 보이게 하는데, 이를 응답결과에서 제외시켜야 한다.
	 * @JsonIgnore을 통해, JSON응답대상에 포함시키지 않게 할 수 있다.
	 */
	@JsonIgnore
	private String password;
	private String name;
	/* Member클래스의 registerDateTime은 LocalDateTime이지만, 이를 JSON으로 변환시
	 * 배열이 된다(2018, 3, 1,..) 이를 특정한 형식으로 바꾸기 위해선,
	 * @JsonFormat을 통해, ISO-8061방식으로 변환하려면 shape속성으로 shape.STRING을 갖게 
	 * 하면 된다.
	 * 만일 원하는 형식으로 표현하고 싶다면, @JsonFormat&Pattern을 쓰면 된다.
	 * @JsonFormat(pattern="yyyyMMddHHmmss")
	 */
	@JsonFormat(shape=Shape.STRING)
	private LocalDateTime registerDateTime;

	public Member(String email, String password, 
			String name, LocalDateTime regDateTime) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.registerDateTime = regDateTime;
	}

	void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getRegisterDateTime() {
		return registerDateTime;
	}

	public void changePassword(String oldPassword, String newPassword) {
		if (!password.equals(oldPassword))
			throw new WrongIdPasswordException();
		this.password = newPassword;
	}
	
	public boolean matchPassword(String password) {
		return this.password.equals(password);
	}
}
