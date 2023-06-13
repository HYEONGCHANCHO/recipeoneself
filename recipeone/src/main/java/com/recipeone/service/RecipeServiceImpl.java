package com.recipeone.service;

import com.recipeone.entity.*;

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
import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;
import com.recipeone.repository.RecipeRepository;
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
    private final RecipeRepository recipeRepository;

	@Autowired
	private RecipeStore rStore;

	@Autowired
	private SqlSessionTemplate session;

    @Override
    public List<Integer> searched(String keyword) throws RecipeIdExistException {
        List<String> recommendedKeywords = recommendKeywords(keyword);
        log.info("recommendedKeywords======" + recommendedKeywords);
        List<Integer> recipeIds = recipeRepository.findRecipeIdByrecommendedKeywords(recommendedKeywords);
        if (recipeIds.isEmpty()) {
            throw new RecipeIdExistException();
        }
        return recipeIds;
    }


    @Override
    public List<Recipe> filterSearchedRecipe(List<String> recommendedKeywords, String rctype, String rcsituation, String rcmeans, String rcingredient, Model
			model) throws RecipeExistException {
        List<Recipe> recipecont = recipeRepository.findRecipesByFilterSearched(recommendedKeywords,rctype,rcsituation,rcmeans,rcingredient);
        List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipecont) {
            ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getRecipeno(), recipe.getTitle(), recipe.getMainpicrename(),recipe.getTag(),recipe.getWriter(), recipe.getRecipestatus());
            listRecipeDtoList.add(listRecipeDto);
        }
        model.addAttribute("recipe", listRecipeDtoList);
        return recipecont;
    }

	@Override
    public List<String> recommendKeywords(String keyword) throws RecipeIdExistException {

        List<String> titleList = recipeRepository.findtitlelist();
        List<String> taglist = recipeRepository.findtaglist();
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
	public int checkRecommand(int recipeno, String memberEmail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int allRecipeCommentList(int page, int limit, int recipeno) {
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
	public int removeOneRecipe(int recipeno) {
		int result = rStore.deleteOneRecipe(session, recipeno);
		return result;
	}

	@Override
	public List<Recipe> printRecipeList(Pagination pagination) {
		List<Recipe> recipeList = rStore.selectAllRecipe(pagination);
		return recipeList;

	}

	@Override
	public Recipe printOneRecipe(int recipeno) {
		Recipe recipe = rStore.selectOneRecipe(recipeno, session);
		return recipe;
	}

	@Override
	public List<RecipeStep> printOneRecipeStep(int recipeno) {
		List<RecipeStep>  rsList = rStore.selectOneRecipeDetail(recipeno, session);
		return rsList;
	}

	@Override
	public List<RecipeIngredient> printOneRecipeIngredient(int recipeno) {
		List<RecipeIngredient> rmList = rStore.selectOneRecipeIngredient(recipeno, session);
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
	public int getTotalCount(int recipeno) {
		int count = rStore.selectTotalCount(session,recipeno);
		return count;
	}

	@Override
	public String printMemberName(String useremail) {
		String name = rStore.selectMemberName(useremail, session);
		return name;
	}

	@Override
	public int checkMyRecipe(int recipeno, String useremail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMemberEmail(int recipeno) {
		String useremail = rStore.selectMemberEmail(session, recipeno);
		return useremail;
	}

	@Override
	public int totalRecord() {
		return rStore.totalRecord(session);
	}

}



