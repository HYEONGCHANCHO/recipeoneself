package com.recipeone.config;

import com.recipeone.security.handler.SessionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebSecurity
public class MvcConfiguration implements WebMvcConfigurer {

	/*
	 * @Bean public CommonsMultipartResolver multipartResolver() {
	 * CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	 * multipartResolver.setDefaultEncoding("UTF-8"); // 파일 인코딩 설정
	 * multipartResolver.setMaxUploadSizePerFile(10 * 1024 * 1024); // 파일당 업로드 크기 제한
	 * (5MB) return multipartResolver; }
	 */

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SessionInterceptor())
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
		registry.addResourceHandler("/static/recipeImg/**").addResourceLocations("D:\\choonsik\\workspace\\bootspring\\230530_병합_recipeone\\src\\main\\resources\\recipeImg\\");
						/*.
				addResourceLocations(uploadPath);*/
	}
}
