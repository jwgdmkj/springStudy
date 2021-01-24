package config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

import aspect.CacheAspect;
import aspect.ExeTimeAspect;
import chap07.Calculator;
import chap07.RecCalculator;

@Configuration
@EnableAspectJAutoProxy
public class AppCtxWithCache {
	
	@Bean
	@Order(1)
	public CacheAspect cacheAspect() {
		return new CacheAspect();
	}
	
	@Bean
	@Order(2)
	public ExeTimeAspect exeTimeAspect() {
		return new ExeTimeAspect();
	}

	


	@Bean
	public Calculator calculator() {
		return new RecCalculator();
	}
	
}
