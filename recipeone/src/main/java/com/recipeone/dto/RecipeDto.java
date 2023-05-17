package com.recipeone.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RecipeDto {
	
	private Long id;
	private String title;
	private String writer; // 사용자 아이디
	private List<RecipeStep> recipeSteps = new ArrayList<>();
	private String cooktime;
	private String nop; // @인분
	//private String rc_thumbnail; // 썸네일 이미지
	private List<RecipeIngredient> recipeIngredients = new ArrayList<>(); // 레시피 재료
	private String tag; // 태그
	private String rcType; // 카테고리 종류별
	private String rcSituation; // 카테고리 상황별
	private String rcIngredient; // 카테고리 재료별
	private String rcMeans; // 카테고리 방법별
	private String rcTheme; // 카테고리 테마별
	
	private LocalDateTime regTime;
    private LocalDateTime updateTime;

}
