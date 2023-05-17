package com.recipeone.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeFormDto {
	
	private Long id; // 레시피 아이디

	private String writer; // 사용자 아이디
	
	//private Integer rc_num; // 레시피 게시글 번호
	
	@NotBlank(message = "제목은 필수!")
	private String title; // 레시피 제목
	
	private String cooktime; // 조리시간
	private String nop; // @인분
	//private String rc_thumbnail; // 썸네일 이미지
	
	private String tag; // 태그
	private String rcType; // 카테고리 종류별
	private String rcSituation; // 카테고리 상황별
	private String rcIngredient; // 카테고리 재료별
	private String rcMeans; // 카테고리 방법별
	private String rcTheme; // 카테고리 테마별
	
	private List<RecipeStep> recipeStep = new ArrayList<>(); // 레시피 내용
	private List<RecipeIngredient> recipeIngredients = new ArrayList<>(); // 레시피 재료
	private List<RecipeImgDto> recipeImgDtoList = new ArrayList<>();
	
	private List<Long> itemImgIds = new ArrayList<Long>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Recipe createRecipe() {
		return modelMapper.map(this, Recipe.class);
	}
	
	public static RecipeFormDto of(Recipe recipe) {
		return modelMapper.map(recipe, RecipeFormDto.class);
	}
	
	public List<RecipeStep> getRecipeStep() {
	    return this.recipeStep;
	}

}
