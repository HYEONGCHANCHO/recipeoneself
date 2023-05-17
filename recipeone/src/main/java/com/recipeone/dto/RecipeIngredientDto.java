package com.recipeone.dto;

import org.modelmapper.ModelMapper;

import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeIngredient;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeIngredientDto {

	private Long id;
	//private Recipe recipe; // 재료번호
	private String gubun; // 큰 구분
	private String igdName; // 재료이름
	private String count; // 중량, 수량
	
	public static ModelMapper modelMapper = new ModelMapper();
	
	public static RecipeIngredientDto of(RecipeIngredient recipeIngredient) {
		return modelMapper.map(recipeIngredient, RecipeIngredientDto.class);
	}
}
