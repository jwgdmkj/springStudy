package chap09;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller	//이걸 붙이면 해당 클래스는 스프링mvc에서 컨트롤러로 사용
public class HelloController {

	//getmapping은 메서드가 처리할 요청경로를 지정. 즉, /hello 경로로 들어온 요청을 hello()메서드를 통해 처리.
	//Model파라미터는 컨트롤러의 처리결과를 뷰에 전달할 때 사용
	//@RequestParam은 http요청 파라미터 값을 메서드의 파라미터로 전달할 때 사용. 이 코드에선, name요청 파라미터 값을 name파라미터에 전달
	@GetMapping("/hello")
	public String hello(Model model,
			@RequestParam(value = "name", required = false) String name) {
		model.addAttribute("greeting", "안녕하세요, " + name);
		//greeting이란 모델속성에 값을 설정. 값으론 '안녕하세요', name파라미터의 밗을 연결한 문자열을 사용. 뒤에서 작성할 jsp코드는 이 속성을 이용해 값을 출력
		return "hello"; //컨트롤러의 처리결과를 보여줄 뷰 이름으로 'hello'를 사용
	}
}

/*
 * controller: 웹요청을 처리, 그 결과를 뷰에 전달하는 스프링빈객체. 
 * 스프링컨트롤러로 쓸 클래스는 이를 붙여야하고, getMapping이나 postMapping같은 요청매핑애노테이션을 통해 처리할 경로를 지정해야함
 * GetMapping과 요청 URL, RequestParma의 관계는
 * http://host:port/sp5-chap09/hello?name=bk에서
 * hello: GetMapping("/hello") & name은 @RequestParam(value="name"....)
 * 
 * GetMapping 애노테이션 값은 서블릿 컨텍스트 경로(또는 웹 어플 경로)를 기준으로 한다. 가령 톰캣의 경우,
 * webapps\sp5-chap09폴더는 웹브라우저에서 http://host/sp5-chap09경로에 해당하며, 이떄 sp-chap09가 컨텍스트경로
 * 즉, http://host/sp5-chap09/main/list처리를 위해선 컨트롤러는 @GetMapping("/main/list")를 사용해야함
 * 여기서 /hello이니, http://host/sp5-chap09/hello
 * 
 * @RequestParam은 HTTP요청 파라미터를 메서드의 파라미터로 전달받을 수 있게 함. RequestParma애노테이션의 value속성은
 * HTTP요청 파라미터의 이름을 지정하고, required속성은 필수여부를 지정.
 * name요청 파라미터값인 bk가 hello()메서드의 name파라미터로 전달됨.
 * 
 * 파라미터로 전달받은 Model객체의 addAttribute()실행은, 뷰에 전달할 데이터를 지정하기 위해 쓰임
 * 첫번째 파라미터 greeting은 데이터의 식별에 쓰이는 속성이름&두번째는 속성이름에 해당하는 값
 * 뷰 코드는 이 속성이름을 사용해 컨트롤러가 전달한 데이터에 접근
 * 
 * 마지막으로 GetMapping이 붙은 메서드는 컨트롤러의 실행결과를 보여줄 뷰 이름을 리턴.즉, 예제에선 hello를 뷰 이름으로 리턴
 * 뷰 이름은 논리적이며, 실제로 뷰 이름에 해당하는 뷰 구현을 찾아주는 건 ViewResolver
 */