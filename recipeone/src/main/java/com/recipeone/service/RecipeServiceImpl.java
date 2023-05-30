package com.recipeone.service;

import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;

import com.recipeone.repository.RecipeRepository;
import com.recipeone.repository.RecipeStore;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.recipeone.dto.ListRecipeDto;
import com.recipeone.dto.RecipeFormDto;
import com.recipeone.dto.RecipeIngredientDto;
import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeImg;
import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;
import com.recipeone.repository.RecipeImgRepository;
import com.recipeone.repository.RecipeRepository;
import com.recipeone.repository.RecipeStepRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Log4j2
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final ModelMapper modelMapper;
    //    private final RecipeSampleRepository recipeSampleRepository;
    private final RecipeRepository recipeRepository;
	@Autowired
	private RecipeStore rStore;

	@Autowired
	private SqlSessionTemplate session;
    private final RecipeRepository recipeRepository;
    private final RecipeImgService recipeImgService;
    private final RecipeImgRepository recipeImgRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeStepService recipeStepService;
    private RecipeIngredientService recipeIngredientService;

    @Override
    public List<Long> searched(String keyword) throws RecipeIdExistException {
        List<String> recommendedKeywords = recommendKeywords(keyword);
        log.info("recommendedKeywords======" + recommendedKeywords);
        List<Long> recipeIds = recipeRepository.findRecipeIdByrecommendedKeywords(recommendedKeywords);
        if (recipeIds.isEmpty()) {
            throw new RecipeIdExistException();
        }
        return recipeIds;
    }

    @Override
    public List<String> recommendKeywords(String keyword) throws RecipeIdExistException {
        List<String> titleList = recipeRepository.findtitlelist();
//        List<String> taglist = recipeRepository.findtaglist();
        List<String> taglist = new ArrayList<>(Arrays.asList("a", "b")); // 레시피 등록 할 때 태그 입력시 삭제
    public List<Long> filterSearchedId(List<String> recommendedKeywords, String rctype, String rcsituation, String rcmeans, String rcingredient) throws RecipeIdExistException {
      List<Long> recipeIds = recipeRepository.findRecipeIdByfilterSearched(recommendedKeywords,rctype,rcsituation,rcmeans,rcingredient);
        return recipeIds;
    }

    @Override
    public List<Recipe> filterSearchedRecipe(List<String> recommendedKeywords, String rctype, String rcsituation, String rcmeans, String rcingredient,  Model model) throws RecipeExistException {
        List<Recipe> recipecont = recipeRepository.findRecipesByFilterSearched(recommendedKeywords,rctype,rcsituation,rcmeans,rcingredient);
        List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipecont) {
            ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getId(), recipe.getTitle(), recipe.getImgUrl(),recipe.getTag(),recipe.getWriter());
            listRecipeDtoList.add(listRecipeDto);
        }
        model.addAttribute("recipe", listRecipeDtoList);
        return recipecont;
    }

    @Override
    public List<String> recommendKeywords(String keyword) throws RecipeIdExistException {

        List<String> titleList = recipeRepository.findtitlelist();
//        List<String> taglist = recipeRepository.findtaglist();
        List<String> taglist = new ArrayList<>(Arrays.asList("a", "b")); //레시피 등록할 때 태그 들어가면 삭제
        double similarityRatio = 0.5;
        log.info("titleList======" + titleList);
        log.info("taglist======" + taglist);
        // 모든 키워드
        List<String> allKeywords = Stream.concat(titleList.stream(), taglist.stream())
                .distinct()
                .collect(Collectors.toList());

        // 추천 키워드 리스트 초기화
        List<String> recommendedKeywords = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            recommendedKeywords.addAll(allKeywords);
        }

        // JaroWinklerDistance 객체 생성
        JaroWinklerDistance distance = new JaroWinklerDistance();

        // 모든 키워드와 입력한 키워드 비교하여 유사비율(similarityRatio) 이상인 경우 추천 키워드 리스트에 추가
        for (String k : allKeywords) {
            if (distance.apply(keyword, k) > similarityRatio) {
                recommendedKeywords.add(k);
            }
        }
        // 추천 키워드 리스트가 비어있는 경우 RecipeIdExistException 예외 처리
        if (recommendedKeywords.isEmpty()) {
            throw new RecipeIdExistException();
        }
        // 추천 키워드 리스트 반환
        log.info("recommendedKeywords======" + recommendedKeywords);
        return recommendedKeywords;
    }

	@Override
	public int registRecipe(Recipe recipe) {
		int result = rStore.insertRecipe(recipe, session);
		log.info("registRecipe result======" + result);
		return result;
	}

	@Override
	public int registStep(List<RecipeStep> rsList) {
		int result = rStore.insertStep(rsList, session);
		log.info("registStep result======" + result);
		return result;
	}

	@Override
	public int registIngredient(List<RecipeIngredient> rmList) {
		int result = rStore.insertIngredient(rmList, session);
		log.info("registIngredient result======" + result);
		return result;
	}

	@Override
	public int checkRecommand(int recipeNo, String memberEmail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int allRecipeCommentList(int page, int limit, int recipeNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyOneRecipe(Recipe recipe) {
		int result = rStore.updateOneRecipe(session, recipe);
		return result;
	}

	@Override
	public int modifyOneRecipeStep(List<RecipeStep> rsList) {
		int result= rStore.updateOneRecipeStep(session, rsList);
		return result;
	}

	@Override
	public int modifyOneRecipeIngredient(List<RecipeIngredient> rmList) {
		int result = rStore.updateOneRecipeIngredient(session, rmList);
		return result;
	}

	@Override
	public int removeOneRecipe(int recipeNo) {
		int result = rStore.deleteOneRecipe(session, recipeNo);
		return result;
	}

	@Override
	public List<Recipe> printRecipeList(int currentPage, int limit) {
		List<Recipe> rList = rStore.selectAllRecipe(currentPage, limit, session);
		return rList;
	}

	@Override
	public Recipe printOneRecipe(int recipeNo) {
		Recipe recipe = rStore.selectOneRecipe(recipeNo, session);
		return recipe;
	}

	@Override
	public List<RecipeStep> printOneRecipeStep(int recipeNo) {
		List<RecipeStep>  rsList = rStore.selectOneRecipeDetail(recipeNo, session);
		return rsList;
	}

	@Override
	public List<RecipeIngredient> printOneRecipeIngredient(int recipeNo) {
		List<RecipeIngredient> rmList = rStore.selectOneRecipeIngredient(recipeNo, session);
		return rmList;
	}

	@Override
	public int removeOneImg(String picName) {
		int result = rStore.deleteOneImg(session, picName);
		return result;
	}

	@Override
	public List<Recipe> recomadRecipe(String recipeCategory) {
		return null;
	}

	@Override
	public int getTotalCount(int recipeNo) {
		int count = rStore.selectTotalCount(session,recipeNo);
		return count;
	}

	@Override
	public String printMemberName(String memberEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int checkMyRecipe(int recipeNo, String memberEmail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMemberEmial(int recipeNo) {
		// TODO Auto-generated method stub
		return null;
	}

}


}
