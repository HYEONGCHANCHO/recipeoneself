package com.recipeone.config;

import com.recipeone.repository.MemberPageRepository;
import com.recipeone.security.handler.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebSecurity
public class MvcConfiguration implements WebMvcConfigurer {
	@Autowired
	private MemberPageRepository memberPageRepository;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SessionInterceptor(memberPageRepository))
				.addPathPatterns("/**")
				.excludePathPatterns("/**/*.js")
				.excludePathPatterns("/**/*.css")
				.excludePathPatterns("/**/*.jpg")
				.excludePathPatterns("/**/*.png")
				.excludePathPatterns("/**/error")
				.excludePathPatterns("/**/null");
	}
	@Value("${uploadPath}")
	String uploadPath;
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addResourceHandler("/images/**").addResourceLocations(uploadPath);
	}
}
