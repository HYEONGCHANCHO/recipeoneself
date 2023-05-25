package com.recipeone.service;

import com.recipeone.dto.RecipeFormDto;
import com.recipeone.dto.RecipeIngredientDto;
import com.recipeone.entity.Recipe;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RecipeService {
    //추가
    List<String> recommendKeywords(String keyword) throws RecipeIdExistException;


    //    아래는 되는 기본코드
    static class RecipeIdExistException extends Exception {
    }

    List<Long> searched(String keyword) throws RecipeIdExistException;

    public Long saveRecipe(RecipeFormDto recipeFormDto, List<MultipartFile> recipeImgFileList) throws Exception;

    public void addIngredientToRecipe(Long recipeId, RecipeIngredientDto recipeIngredientDto);

    public Recipe getRecipeById(Long recipeId);

    //    public List<Long> filterSearched(String keyword) throws RecipeIdExistException; //진행중
    public List<Long> filterSearched(List<String> recommendedKeywords, String rctype, String rcsituation, String rcmeans, String rcingredient) throws RecipeIdExistException;
    public List<Recipe> filterSearched2(List<String> recommendedKeywords, String rctype, String rcsituation, String rcmeans, String rcingredient,  Model model) throws RecipeIdExistException;

}