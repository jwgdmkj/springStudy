package Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/view/", ".jsp");
	}

	/* step3.jsp의 회원가입 완료 후 첫 화면으로 이동하는 링크는, 뷰 이름만 리턴하도록 구현한다
	 * 이를 위한 컨트롤러클래스를 따로 구현하는 건 성가시다. 
	 * WebMvcConfigurere 인터페이스의 addViewControllers()를 통해, 이를 편하게 구현해본다.
	 * "/main"요청경로에 대해 뷰 이름으로 main을 사용토록 설정 후, 이에 해당하는 main.jsp를 만듬.
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/main").setViewName("main");
	}
}
