package spring;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class RegisterRequest {

	/* Validator을 말고 다른 애노테이션을 통해 커맨드 객체의 값을 검증해보자!
	 * 우선 아래처럼 Bean Validation 애노테이션을 적용 후, 이를 적용한 커맨드 객체를 검증할 수 있는
	 * OptinalValidatorFactoryBean 클래스를 빈으로 등록해야 한다.
	 * 
	 * 단, @EnableWebMvc을 적용하면 OptimalValidationFactoryBean을 글로벌범위 Validator
	 * 로 등록하므로, 이를 하면 더 할 필요는 없다.
	 * 
	 * 다음으론, 커맨드객체에 Valid를 붙인다(in handleStep3())
	 * 
	 * 글로벌범위 Validator을 적용한 상태라면, OptionalValidatorFactoryBean이 글로벌범위
	 * Validator로 사용되지 않으므로, 이는 삭제해야 함
	 */
	
	@NotBlank
	@Email
	private String email;
	@Size(min=6)
	private String password;
	@NotEmpty
	private String confirmPassword;
	@NotEmpty
	private String name;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPasswordEqualToConfirmPassword() {
		return password.equals(confirmPassword);
	}
}

/* registerReques : 커맨드 객체에 접근할 때 사용한 속성 이름.
스프링 mvc는 커맨드 객체의 첫 글자를 소문자로 바꾼 클래스이름과 동일한 속성 이름을 사용해
커맨드 객체를 뷰에 전달. 즉 클래스명이 RegisterRequest이므로, registerRequest
라는 이름을 이용해 객체 접근이 가능
@PostMapping("/register/step3")
public String handleStep3(RegisterRequest regReq) { }
에서, RegisterRequest가 커맨드객체이므로, 뷰 코드는 이름을 사용해 registerRequest.name으로 접근
*/