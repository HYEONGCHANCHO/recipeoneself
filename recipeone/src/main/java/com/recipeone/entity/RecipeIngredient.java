package com.recipeone.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RecipeIngredient {


	private int recipeno; // 레시피 번호
	private int ingredientorder; // 레시피 재료 번호
	private String ingredient; // 재료명
	private String amount; // 수량
	
	public RecipeIngredient() {
		
	}
	
	public RecipeIngredient(int recipeno, int ingredientorder, String ingredient, String amount) {
		super();
		this.recipeno = recipeno;
		this.ingredientorder = ingredientorder;
		this.ingredient = ingredient;
		this.amount = amount;
	}
}
