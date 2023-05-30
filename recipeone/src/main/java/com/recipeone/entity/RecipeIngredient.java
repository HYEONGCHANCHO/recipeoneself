package com.recipeone.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RecipeIngredient {


	private int recipeNo; // 레시피 번호
	private int ingredientOrder; // 레시피 재료 번호
	private String ingredient; // 재료명
	private String amount; // 수량
	
	public RecipeIngredient() {
		
	}
	
	public RecipeIngredient(int recipeNo, int ingredientOrder, String ingredient, String amount) {
		super();
		this.recipeNo = recipeNo;
		this.ingredientOrder = ingredientOrder;
		this.ingredient = ingredient;
		this.amount = amount;
	}
}
