package controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.AuthInfo;
import spring.ChangePasswordService;
import spring.WrongIdPasswordException;

//비밀번호 변경 요청을 처리하는 컨트롤러 클래스
@Controller
@RequestMapping("/edit/changePassword")
public class ChangePwdController {
	private ChangePasswordService changePasswordService;
	
	public void setChangePasswordService
	(ChangePasswordService changePasswordService) {
		this.changePasswordService= changePasswordService;
	}
	
	@GetMapping
	public String form(@ModelAttribute("command") ChangePwdCommand pweCmd) {
		return "edit/changePwdForm";
	}
	
	@PostMapping
	public String submit(@ModelAttribute("command") ChangePwdCommand pwdCmd,
			Errors errors, HttpSession session) {
		new ChangePwdCommandValidator().validate(pwdCmd, errors);
		if(errors.hasErrors()) {
			return "edit/changePwdForm";
		}
		
		AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
		try {
			changePasswordService.changePassword(authInfo.getEmail(),
					pwdCmd.getCurrentPassword(), pwdCmd.getNewPassword());
			
			return "edit/changePwd";
		} catch(WrongIdPasswordException e) {
			errors.rejectValue("currentPassword", "notMatching");
			return "edit/changePwdForm";
		}
	}
}
