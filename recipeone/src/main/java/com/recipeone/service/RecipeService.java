package com.recipeone.service;

import com.recipeone.dto.RecipeFormDto;
import com.recipeone.dto.RecipeIngredientDto;
import com.recipeone.entity.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface RecipeSampleService {
//추가
List<String> recommendKeywords(String keyword) throws RecipeIdExistException;


    //    아래는 되는 기본코드
    static class RecipeIdExistException extends Exception{
    }
    List<Long> searched(String keyword) throws RecipeIdExistException;
    public Long saveRecipe(RecipeFormDto recipeFormDto, List<MultipartFile> recipeImgFileList) throws Exception;
    public void addIngredientToRecipe(Long recipeId, RecipeIngredientDto recipeIngredientDto);
    public Recipe getRecipeById(Long recipeId);
}
