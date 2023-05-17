package com.recipeone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recipeone.entity.RecipeStep;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
	
	List<RecipeStep> findByRecipeIdOrderByIdAsc(Long recipeId);

	
}
