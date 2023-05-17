package com.recipeone.dto;

import org.modelmapper.ModelMapper;

import com.recipeone.entity.RecipeImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeImgDto {
	
	private Long id;
	
	private String imgName; // 이미지 파일명
	
	private String oriImgName; // 원본 이미지 파일명
	
	private String imgUrl; //이미지 조회 경로
	
	private String repimgYn; // 대표 이미지 여부
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static RecipeImgDto of(RecipeImg recipeImg) {
		return modelMapper.map(recipeImg, RecipeImgDto.class);
	}
}
