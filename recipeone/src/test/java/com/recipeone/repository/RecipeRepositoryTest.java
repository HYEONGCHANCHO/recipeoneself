package com.recipeone.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.recipeone.entity.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RecipeRepositoryTest {
// 3차병합때 수정한 부분

	@Autowired
	private RecipeRepository recipeRepository;

	@Test
	@DisplayName("레시피 데이터 생성 테스트")
	public void createRecipeDataTest() {
		Random random = new Random();

		String[] categoryTypes = {"56", "54", "55", "60", "53", "52", "61", "57", "58", "65", "64", "68", "66", "69", "59", "62", "63"};
		String[] categorySituations = {"12", "18", "13", "19", "21", "15", "43", "17", "45", "20", "46", "44", "14", "22"};
		String[] categoryIngredients = {"70", "71", "72", "23", "28", "24", "50", "33", "47", "32", "25", "31", "48", "27", "26", "34"};
		String[] categoryMethods = {"6", "1", "7", "36", "41", "42", "8", "10", "9", "38", "67", "39", "37", "11"};

		for (int i = 1; i <= 7; i++) {
			Recipe recipe = new Recipe();
			recipe.setRecipeno(i);
//			recipe.setWriter("User " + random.nextInt(10));
			recipe.setWriter("조형찬");
			recipe.setTitle("Recipe " + i);
			recipe.setCooktime(random.nextInt(60) + " minutes");
			recipe.setNop(random.nextInt(4) + 1 + " servings");
			recipe.setMainpic("thumbnail_" + i + ".jpg");
			recipe.setRccount(0);
			recipe.setRecipestatus("Y");
			recipe.setTag("tag" + random.nextInt(10));
			recipe.setRctype(categoryTypes[random.nextInt(categoryTypes.length)]);
			recipe.setRcsituation(categorySituations[random.nextInt(categorySituations.length)]);
			recipe.setRcingredient(categoryIngredients[random.nextInt(categoryIngredients.length)]);
			recipe.setRcmeans(categoryMethods[random.nextInt(categoryMethods.length)]);

			recipeRepository.save(recipe);
		}

		List<Recipe> savedRecipes = recipeRepository.findAll();
		System.out.println("Total recipes saved: " + savedRecipes.size());
	}
}