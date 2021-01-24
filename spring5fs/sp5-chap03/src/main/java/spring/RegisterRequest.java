package spring;

public class RegisterRequest {
	private String password;
	private String email;
	private String name;
	private String comfirmPassword;
	
	public String getEmail() {
		return email;
	}
	public String getPw() {
		return password;
	}
	public String getcomfimrpassword() {
		return comfirmPassword;
	}
	public String getname() {
		return name;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	public void setPw(String pw) {
		this.password=pw;
	}
	public void setcomfirmpw(String comfirm) {
		this.comfirmPassword=comfirm;
	}
	public void setName(String name) {
		this.name=name;
	}
	
	public boolean isPasswrodEqualtoComfirmpassword() {
		return password.equals(comfirmPassword);
	}
}
