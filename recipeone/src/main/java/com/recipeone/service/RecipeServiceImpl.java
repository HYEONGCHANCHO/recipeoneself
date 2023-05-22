package com.recipeone.service;

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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.web.multipart.MultipartFile;


@Log4j2
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final ModelMapper modelMapper;
    //    private final RecipeSampleRepository recipeSampleRepository;
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

//진행중
  @Override
    public List<Long> filterSearched(List<String> recommendedKeywords, String rcType, String rcSituation, String rcMeans, String rcIngredient) throws RecipeIdExistException {
        log.info("recommendedKeywords======" + recommendedKeywords);
        List<Long> recipeIds = recipeRepository.findRecipeIdByfilterSearched(recommendedKeywords,rcType,rcSituation,rcMeans,rcIngredient);
        if (recipeIds.isEmpty()) {
            throw new RecipeIdExistException();
        }
        return recipeIds;
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

    public Long saveRecipe(RecipeFormDto recipeFormDto, List<MultipartFile> recipeImgFileList) throws Exception {

        // 레시피 등록
        Recipe recipe = recipeFormDto.createRecipe();
        recipeRepository.save(recipe);

        // 이미지 등록
        for (int i = 0; i < recipeImgFileList.size(); i++) {
            RecipeImg recipeImg = new RecipeImg();
            recipeImg.setRecipe(recipe);

            // 대표이미지 설정
            if (i == 0) {
                recipeImg.setRepimgYn("Y");
            } else {
                recipeImg.setRepimgYn("N");
            }
            recipeImgService.saveRecipeImg(recipeImg, recipeImgFileList.get(i));
        }

        // 요리순서 등록
        List<RecipeStep> recipeSteps = new ArrayList<>();
        for (int j = 0; j < recipeImgFileList.size(); j++) {
            RecipeStep recipeStep = new RecipeStep();
            recipeStep.setRecipe(recipe);
            recipeStep.setSteptext("왜 값이 안들어갈까" + j);
            //Map<String, Object> steptext = new HashMap<String, Object>();
            //steptext.put(recipeStep.getSteptext(), j);

            recipeSteps.add(recipeStep);
            //recipeStepService.saveRecipeStep(recipeStep);
        }
        recipeStepRepository.saveAll(recipeSteps);

        return recipe.getId();
    }

    public void addIngredientToRecipe(Long recipeId, RecipeIngredientDto recipeIngredientDto) {
        Recipe recipe = getRecipeById(recipeId);

        // RecipeIngredient 생성 및 설정
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.createRecipeIngredient(recipeIngredientDto);
        recipeIngredient.setRecipe(recipe);

        // RecipeIngredient 저장
        recipeIngredientService.saveRecipeIngredient(recipeIngredient);

        // Recipe의 ingredients 리스트에 추가
        recipe.getRecipeIngredients().add(recipeIngredient);

        // Recipe 저장
        recipeRepository.save(recipe);

    }

    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NoSuchElementException("Recipe with ID " + recipeId + " not found"));
    }

}
