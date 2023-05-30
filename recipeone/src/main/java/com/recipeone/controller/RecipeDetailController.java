package com.recipeone.controller;

import com.recipeone.entity.Recipe;
import com.recipeone.repository.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

//@Controller
public class RecipeDetailController {

/*    private final RecipeRepository recipeRepository;

    public RecipeDetailController(RecipeRepository recipeRepository){
        this.recipeRepository = recipeRepository;
    }

    @GetMapping(value = "/recipe/recipeDetail/{recipeNo}")
    public String recipeDetail(@PathVariable Long id, Model model) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            model.addAttribute("recipe", recipe);
            return "recipe/recipeDetail";
        } else {
            // 레시피를 찾지 못한 경우에 대한 처리
            return "error";
        }
    }*/
}
