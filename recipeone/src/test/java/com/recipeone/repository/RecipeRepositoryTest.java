package com.recipeone.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.recipeone.entity.Recipe;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RecipeRepositoryTest {

	@Autowired
	RecipeRepository recipeRepository;
	
	@Test
	@DisplayName("레시피 저장 테스트")
	public void createRecipeTest() {
		Recipe recipe = new Recipe();
		recipe.setTitle("테스트 레시피");
		recipe.setCooktime("10분");
		recipe.setNop("1인분");
		Recipe savedRecipe = recipeRepository.save(recipe);
		System.out.println(savedRecipe.toString());
	}
}
