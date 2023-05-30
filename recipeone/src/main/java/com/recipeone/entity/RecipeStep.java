package com.recipeone.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RecipeStep {
	
	public RecipeStep() {
		
	}
	
	private int recipeNo; // 레시피 번호
	private int recipeOrder; // 레시피 순서 번호
	private String recipeDescription; // 레시피 설명
	private String recipePic; // 레시피 사진
	private String recipePicRename; // 레시피 사진 재업로드시 이름

	
}
