package com.recipeone.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.recipeone.dto.ListRecipeDto;
import com.recipeone.entity.Recipe;
import com.recipeone.repository.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.recipeone.dto.RecipeFormDto;
import com.recipeone.service.RecipeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recipe")
@Log4j2
@RequiredArgsConstructor
public class RecipeController {

	private final RecipeService recipeService;
	private final RecipeRepository recipeRepository;

	@GetMapping(value = "/recipeForm")
	public String recipeForm(Model model) {
		model.addAttribute("recipeFormDto", new RecipeFormDto());
		return "recipe/recipeForm";
	}

	@PostMapping("/recipeForm")
	public String saveRecipe(@Valid RecipeFormDto recipeFormDto, BindingResult bindingResult, Model model,
							 @RequestParam(value="recipeImgFile") List<MultipartFile> recipeImgFileList) {

		if(bindingResult.hasErrors()) {
			return "recipe/recipeForm";
		}

		if(recipeImgFileList.get(0).isEmpty() && recipeFormDto.getId() == null) {
			model.addAttribute("errorMessage", "레시피 썸네일 이미지는 필수 입력 값 입니다.");
			return "recipe/recipeForm";
		}

		try {
			recipeService.saveRecipe(recipeFormDto, recipeImgFileList);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
			return "recipe/recipeForm";
		}
		return "redirect:/";
	}

	@GetMapping(value = "/recipeList")
	public String recipeList(@RequestParam Map<String, String> param, Model model) {
		List<Recipe> recipeList = recipeRepository.findAll(); // DB에서 레시피 목록 조회

		List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
		for (Recipe recipe : recipeList) {
			ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getId(), recipe.getTitle(), recipe.getImgUrl());
			listRecipeDtoList.add(listRecipeDto);
		}

		model.addAttribute("recipe", listRecipeDtoList);
		return "recipe/recipeList";

    private final RecipeSampleService recipeSampleService;

    private final RecipeSampleRepository recipeSampleRepository;

    @PreAuthorize("principal.username==#boardDTO.writer") //작성자와 동일한 user만 수정 페이지 가능
    @GetMapping("/modify")
    public void modify() {
    }

    @RequestMapping(value="/recommendKeywords", method=RequestMethod.POST)
    @ResponseBody
    public List<String> getRecommendedKeywords(@RequestParam String keyword) {
        List<String> recommendedKeywords = null;
        try {
            recommendedKeywords = recipeSampleService.recommendKeywords(keyword);
        } catch (RecipeSampleService.RecipeIdExistException e) {
            // 예외 처리 코드
        }
        return recommendedKeywords;
    }

    @PostMapping("/recipelist")
    public String recipelistPOST(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes, Model model) {
        try {
            List<Long> recipeIds = recipeSampleService.searched(keyword);
            log.info("recupeIds는======"+recipeIds);
            redirectAttributes.addFlashAttribute("recipeIds", recipeIds);
            redirectAttributes.addFlashAttribute("result", "success");
        } catch (RecipeSampleService.RecipeIdExistException e) {
            redirectAttributes.addFlashAttribute("error", "id");
        }
        return "redirect:/recipe/recipeList";
    }
	}}




