package com.recipeone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipeone.entity.RecipeImg;

public interface RecipeImgRepository extends JpaRepository<RecipeImg, Long> {
	
	List<RecipeImg> findByRecipeIdOrderByIdAsc(Long recipeId);
	
	RecipeImg findByRecipeIdAndRepimgYn(Long recipeId, String repimgYn);
}
