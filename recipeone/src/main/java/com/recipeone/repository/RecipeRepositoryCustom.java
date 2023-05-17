package com.recipeone.repository;

import com.recipeone.dto.RecipeSearchDto;
import com.recipeone.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeRepositoryCustom {
    Page<Recipe> getAdminRecipePage(RecipeSearchDto recipeSearchDto, Pageable pageable);
}
