package com.recipeone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.recipeone.dto.RecipeFormDto;
import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeImg;
import com.recipeone.repository.RecipeImgRepository;
import com.recipeone.repository.RecipeRepository;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class RecipeServiceTest {
	
	@Autowired
    RecipeService recipeService;
	
	@Autowired
	RecipeRepository recipeRepository;
	
	@Autowired
	RecipeImgRepository recipeImgRepository;
	
	List<MultipartFile> createMultipartFiles() throws Exception {
		
		List<MultipartFile> multipartFileList = new ArrayList<>();
		
		for(int i=0; i<5; i++) {
			String path = "c:/recipe/item";
			String imageName = "image" + i + ".jpg";
			MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
			multipartFileList.add(multipartFile);
		}
		return multipartFileList;
	}
	
	@Test
	@DisplayName("레시피 등록 테스트")
	@WithMockUser(username = "admin", roles = "ADMIN")
	void saveRecipe() throws Exception {
		RecipeFormDto recipeFormDto = new RecipeFormDto();
		recipeFormDto.setTitle("테스트 레시피 제목");
		
		
		List<MultipartFile> multipartFileList = createMultipartFiles();
		Long recipeId = recipeService.saveRecipe(recipeFormDto, multipartFileList);
		
		List<RecipeImg> recipeImgList = recipeImgRepository.findByRecipeIdOrderByIdAsc(recipeId);
		Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(EntityExistsException::new);
		
		assertEquals(recipeFormDto.getTitle(), recipe.getTitle());
		assertEquals(multipartFileList.get(0).getOriginalFilename(), recipeImgList.get(0).getOriImgName());
		
	}

}
