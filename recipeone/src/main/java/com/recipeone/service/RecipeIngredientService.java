package com.recipeone.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.recipeone.entity.RecipeIngredient;
import com.recipeone.repository.RecipeIngredientRepository;

@Service
public class RecipeIngredientService {

	private RecipeIngredientRepository recipeIngredientRepository;

	public RecipeIngredient saveRecipeIngredient(RecipeIngredient recipeIngredient) {
        return recipeIngredientRepository.save(recipeIngredient);
    }

    public void deleteRecipeIngredientById(Long id) {
        recipeIngredientRepository.deleteById(id);
    }

    public RecipeIngredient updateRecipeIngredient(Long id, String gubun, String igdName, String count) {
        RecipeIngredient recipeIngredient = getRecipeIngredientById(id);
        recipeIngredient.setGubun(gubun);
        recipeIngredient.setIgdName(igdName);
        recipeIngredient.setCount(count);
        return recipeIngredientRepository.save(recipeIngredient);
    }

    public RecipeIngredient getRecipeIngredientById(Long id) {
        return recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("RecipeIngredient with ID " + id + " not found"));
    }
}
