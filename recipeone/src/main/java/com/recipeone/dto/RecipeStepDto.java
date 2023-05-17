package com.recipeone.dto;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeStep;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeStepDto {
	
	private Long id;
	//private Recipe recipe; // 레시피 번호
	
	
	private String steptext; // 레시피 설명 (조리 설명)
	//private String stepImg; // 레시피 이미지 (조리 이미지)

	public static ModelMapper modelMapper = new ModelMapper();
	
	public static RecipeStepDto of(RecipeStep recipeStep) {
		return modelMapper.map(recipeStep, RecipeStepDto.class);
	}
}
