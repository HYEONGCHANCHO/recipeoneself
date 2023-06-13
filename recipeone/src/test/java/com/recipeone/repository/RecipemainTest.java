package com.recipeone.repository;

import com.recipeone.entity.Recipe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RecipemainTest {

	@Autowired
	private RecipeRepository recipeRepository;

	@Test
	@DisplayName("레시피 데이터 생성 테스트")
	public void createRecipeDataTest() {
		Random random = new Random();
		Date date = new Date(System.currentTimeMillis());

		String[] categoryTypes = {"56", "54", "55", "60", "53", "52", "61", "57", "58", "65", "64", "68", "66", "69", "59", "62", "63"};
		String[] categorySituations = {"12", "18", "13", "19", "21", "15", "43", "17", "45", "20", "46", "44", "14", "22"};
		String[] categoryIngredients = {"70", "71", "72", "23", "28", "24", "50", "33", "47", "32", "25", "31", "48", "27", "26", "34"};
		String[] categoryMethods = {"6", "1", "7", "36", "41", "42", "8", "10", "9", "38", "67", "39", "37", "11"};


			Recipe recipe = new Recipe();
			recipe.setRecipeno(1);
			recipe.setWriter("조형찬");
			recipe.setTitle("피자");
			recipe.setCooktime(random.nextInt(30) + " minutes");
			recipe.setNop(2 + " servings");
			recipe.setMainpic("피자" + ".jpg");
			recipe.setMainpicrename("피자");
			recipe.setMainpicurl("/images/item/ef24a03d-68d0-4b95-bc8d-0a74b88e06d9.jpg");
			recipe.setRccount(0);
			recipe.setRecipestatus("Y");
			recipe.setTag("#피자#양식");
			recipe.setInsertdate(date);
			recipe.setUpdatedate(date);
			recipe.setRctype(categoryTypes[random.nextInt(categoryTypes.length)]);
			recipe.setRcsituation(categorySituations[random.nextInt(categorySituations.length)]);
			recipe.setRcingredient(categoryIngredients[random.nextInt(categoryIngredients.length)]);
			recipe.setRcmeans(categoryMethods[random.nextInt(categoryMethods.length)]);
		recipeRepository.save(recipe);

		recipe = new Recipe();
		recipe.setRecipeno(2);
		recipe.setWriter("조형찬");
		recipe.setTitle("부타동");
		recipe.setCooktime(random.nextInt(30) + " minutes");
		recipe.setNop(2 + " servings");
		recipe.setMainpic("부타동" + ".jpg");
		recipe.setMainpicrename("부타동");
		recipe.setMainpicurl("/images/item/14176136-5893-4332-ae47-e561788030cb.jpg");
		recipe.setRccount(0);
		recipe.setRecipestatus("Y");
		recipe.setTag("#부타동#일식");
		recipe.setInsertdate(date);
		recipe.setUpdatedate(date);
		recipe.setRctype(categoryTypes[random.nextInt(categoryTypes.length)]);
		recipe.setRcsituation(categorySituations[random.nextInt(categorySituations.length)]);
		recipe.setRcingredient(categoryIngredients[random.nextInt(categoryIngredients.length)]);
		recipe.setRcmeans(categoryMethods[random.nextInt(categoryMethods.length)]);
		recipeRepository.save(recipe);

		recipe = new Recipe();
		recipe.setRecipeno(3);
		recipe.setWriter("조형찬");
		recipe.setTitle("짜장면");
		recipe.setCooktime(random.nextInt(30) + " minutes");
		recipe.setNop(2 + " servings");
		recipe.setMainpic("짜장면" + ".jpg");
		recipe.setMainpicrename("짜장면");
		recipe.setMainpicurl("/images/item/68dd298f-6fd1-4468-a92a-9e6b18932c22.jpg");
		recipe.setRccount(0);
		recipe.setRecipestatus("Y");
		recipe.setTag("#중식#짜장면");
		recipe.setInsertdate(date);
		recipe.setUpdatedate(date);
		recipe.setRctype(categoryTypes[random.nextInt(categoryTypes.length)]);
		recipe.setRcsituation(categorySituations[random.nextInt(categorySituations.length)]);
		recipe.setRcingredient(categoryIngredients[random.nextInt(categoryIngredients.length)]);
		recipe.setRcmeans(categoryMethods[random.nextInt(categoryMethods.length)]);
		recipeRepository.save(recipe);

		recipe = new Recipe();
		recipe.setRecipeno(4);
		recipe.setWriter("조형찬");
		recipe.setTitle("비빔밥");
		recipe.setCooktime(random.nextInt(30) + " minutes");
		recipe.setNop(2 + " servings");
		recipe.setMainpic("비빔밥" + ".jpg");
		recipe.setMainpicrename("비빔밥");
		recipe.setMainpicurl("/images/item/f5696188-6cfd-42b8-940c-48c726dd4b8e.jpg");
		recipe.setRccount(0);
		recipe.setRecipestatus("Y");
		recipe.setTag("#비빔밥#한국");
		recipe.setInsertdate(date);
		recipe.setUpdatedate(date);
		recipe.setRctype(categoryTypes[random.nextInt(categoryTypes.length)]);
		recipe.setRcsituation(categorySituations[random.nextInt(categorySituations.length)]);
		recipe.setRcingredient(categoryIngredients[random.nextInt(categoryIngredients.length)]);
		recipe.setRcmeans(categoryMethods[random.nextInt(categoryMethods.length)]);
			recipeRepository.save(recipe);
		}
	}
