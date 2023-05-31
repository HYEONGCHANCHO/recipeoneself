package com.recipeone.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RecipeStep {
	
	private int recipeno; // 레시피 번호
	private int recipeorder; // 레시피 순서 번호
	private String recipedescription; // 레시피 설명
	private String recipepic; // 레시피 사진
	private String recipepicrename; // 레시피 사진 재업로드시 이름

	public RecipeStep() {

	}
}
