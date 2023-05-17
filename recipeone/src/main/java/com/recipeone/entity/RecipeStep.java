package com.recipeone.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.recipeone.dto.RecipeImgDto;
import com.recipeone.dto.RecipeStepDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "recipeStep")
public class RecipeStep extends BaseTimeEntity {
	
	@Id
	@Column(name="recipe_step_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//@ManyToOne(targetEntity = Recipe.class, cascade = CascadeType.PERSIST)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private Recipe recipe; // 레시피 번호
	
	private String steptext; // 레시피 설명 (조리 설명)

	@ManyToOne
	@JoinColumn(name="recipe_img_id")
	private RecipeImg recipeImg; // 레시피 이미지 (조리 이미지)
	
	public void createRecipeStep(RecipeStepDto recipeStepDto) {
		//this.id = recipeStepDto.getId();
		//this.recipe = recipeStepDto.getRecipe();
		this.steptext = recipeStepDto.getSteptext();
		//this.stepImg = recipeStepDto.getStepImg();
	}

	public void setRecipeImg(RecipeImg recipeImg) {
			this.recipeImg = recipeImg;
	}
	
}
