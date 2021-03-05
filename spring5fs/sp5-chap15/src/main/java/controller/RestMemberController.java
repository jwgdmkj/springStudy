package controller;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import spring.DuplicateMemberException;
import spring.Member;
import spring.MemberDao;
import spring.MemberNotFoundException;
import spring.MemberRegisterService;
import spring.RegisterRequest;

@RestController
public class RestMemberController {
	private MemberDao memberDao;
	private MemberRegisterService registerService;
	
	//밑의 두 클래스는, 요청매핑에노테이션 적용메서드이며, 리턴타이으로 일반객체로 쓰고있다.
	//클래스패스에 Jackson이 존재하면, JSON형식의 문자열로 변환해 응답한다.
	//가령 members메서드는 리턴타입이 LIST<Member>이며, 이경우 해당 리스트객체를 JSON형식 배열로 변환해 응답한다.
	//RestMemberController클래스를 ControllerConfig클래스에 추가한다.
	@GetMapping("/api/members")
	public List<Member> members() {
		return memberDao.selectAll();
	}
	
	/*
	 * @GetMapping("/api/members/{id}") public Member member(@PathVariable Long id,
	 * HttpServletResponse response) throws IOException { Member member=
	 * memberDao.selectById(id); if(member==null) {
	 * response.sendError(HttpServletResponse.SC_NOT_FOUND); return null; } return
	 * member; }
	 */
	
	//아래아래는 ExceptionHandler을 통해, 중복을 제거함
	/*
	 * @GetMapping("/api/members/{id}") public ResponseEntity<Object>
	 * member(@PathVariable Long id) { Member member= memberDao.selectById(id);
	 * if(member==null) { return ResponseEntity.status(HttpStatus.NOT_FOUND).
	 * body(new ErrorResponse("no member")); }
	 * 
	 * return ResponseEntity.status(HttpStatus.OK).body(member); }
	 */
	@GetMapping("/api/members/{id}")
	public Member member(@PathVariable Long id) {
		Member member= memberDao.selectById(id);
		if(member==null) {
			throw new MemberNotFoundException();
		}
		return member;
	}
	@ExceptionHandler(MemberNotFoundException.class) 
	public ResponseEntity<ErrorResponse> handleNoData() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).
				body(new ErrorResponse("no member");
	}
	
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao=memberDao;
	}
	
	public void setRegisterService(MemberRegisterService registerService) {
		this.registerService=registerService;
	}
	
	/*
	 * @PostMapping("/api/members") public void newMember(@RequestBody @Valid
	 * RegisterRequest regReq, Errors errors, HttpServletResponse response) throws
	 * IOException { try { new RegisterRequestValidator().validate(regReq, errors);
	 * if(errors.hasErrors()) {
	 * response.sendError(HttpServletResponse.SC_BAD_REQUEST); return; }
	 * 
	 * Long newMemberId= registerService.regist(regReq);
	 * response.setHeader("Location", "/api/members/"+ newMemberId);
	 * response.setStatus(HttpServletResponse.SC_CREATED); }
	 * catch(DuplicateMemberException dupEx) {
	 * response.sendError(HttpServletResponse.SC_CONFLICT); } }
	 */
	
	//밑밑은 검증실패시, HTTP가아닌 JSON으로 하는것
	/*
	 * @PostMapping("/api/members") public ResponseEntity<Object>
	 * newMember(@RequestBody @Valid RegisterRequest regReq, Errors errors,
	 * HttpServletResponse response) { try { Long
	 * newMemberId=registerService.regist(regReq); URI uri=
	 * URI.create("/api/members/"+newMemberId);
	 * 
	 * return ResponseEntity.created(uri).build(); } catch(DuplicateMemberException
	 * dupEx) { return ResponseEntity.status(HttpStatus.CONFLICT).build(); } }
	 */
	@PostMapping("/api/members") 
	public ResponseEntity<Object> newMember(@RequestBody @Valid 
			RegisterRequest regReq, Errors errors) {
		if(errors.hasErrors()) {
			String errorCodes= errors.getAllErrors() //List<ObjectError>
					.stream().map(error->error.getCodes()[0]) //error는 ObjectError
					.collect(Collectors.joining("."));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse("errorCodes = "+errorCodes));
		}
		 try { 
			 Long newMemberId=registerService.regist(regReq); 
			 URI uri= URI.create("/api/members/"+newMemberId);
		 
			 return ResponseEntity.created(uri).build(); 
		 }
		 catch(DuplicateMemberException dupEx) {
			 return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
		} 
	}
}