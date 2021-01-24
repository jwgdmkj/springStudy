package config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;

import spring.MemberDao;
import spring.MemberPrinter;
import spring.MemberSummaryPrinter;
import spring.VersionPrinter;

@Configuration
@ComponentScan(basePackages = {"spring", "spring2" }, 
	excludeFilters = { 
			@Filter(type = FilterType.ANNOTATION, classes = ManualBean.class )		
	//excludeFilters = @Filter(type=FilterType.REGEX, pattern="spring\\..*Dao"))
	//는 필터 애노테이션의 type속성값으로 FilterType.REGEX를 줌으로써, 정규표현식을 사용해 제외대상을 지정
	//pattern속성은 FilterType에 적용할 값을 설정. 즉, spring.으로 시작, Dao로 끝나므로
	//spring.MemberDao 클래스를 컴포넌트 스캔 대상에서 제외
	
	//기타 사용예로 AspectJ패턴. 이는 의존대상에 aspectjweaver 모듈 추가가 필수
})
public class AppCtxWithExclude {
	@Bean
	public MemberDao memberDao() {
		return new MemberDao();
	}
	
	@Bean
	@Qualifier("printer")
	public MemberPrinter memberPrinter1() {
		return new MemberPrinter();
	}

	@Bean
	@Qualifier("summaryPrinter")
	public MemberSummaryPrinter memberPrinter2() {
		return new MemberSummaryPrinter();
	}

	@Bean
	public VersionPrinter versionPrinter() {
		VersionPrinter versionPrinter = new VersionPrinter();
		versionPrinter.setMajorVersion(5);
		versionPrinter.setMinorVersion(0);
		return versionPrinter;
	}
}
