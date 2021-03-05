package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc	//내부적으로 다양한 빈설정을 추가해, 스프링mvc 사용에 필요한 기본적 구성을 함.\
//WebMvcConfigurer은 스프링MVC의 개별설정 조절에 사용
public class MvcConfig implements WebMvcConfigurer {

	//DispatcherServlet의 매핑경로를 /로 줄때, JSP/HTML/CSS등을 올바르게 처리하기 위한설정 추가.
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	//WebMvcConfigurer인터페이스에 정의된 메서드. 가각 디폴트서블릿과 viewresolver와 관련된 설정 조절.
	//registry.jsp는 JSP를 뷰구현으로 쓸 수 있게 해주는 설정. 첫번째 인자는 JSP파일 경로 찾을 때 쓸 접두어
	//두번째는 접미사. 즉, 최종적으로 jsp파일 경로 결정. 
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/view/", ".jsp");
	}

}
/* jsp파일의 greeting은 컨트롤러 구현에서 Model에 추가한 속성의 이름과 동일. 
이렇게 컨트롤러에서 설정한 속성을 뷰 JSP코드에서 접근가능한 이유는, 스플이MVC프레임워크가 모델에 추가한 속성을 JSP에서 접근할 수 있도록
HttpServletRequest에 옮겨주기 때문

model.addAttribute("greeting"...) --> 스프링mvc프레임웤의 request.setAttribute("greeting", 값)
-->뷰에서 속성접근 (인사말:${greeting})============-======
*/