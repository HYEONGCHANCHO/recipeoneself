package com.recipeone.service;

import com.recipeone.repository.RecipeSampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.similarity.JaroWinklerDistance;


@Log4j2
@Service
@RequiredArgsConstructor
public class RecipeSampleServiceImpl implements RecipeSampleService {
    private final ModelMapper modelMapper;
    private final RecipeSampleRepository recipeSampleRepository;

    @Override
    public List<Long> searched(String keyword) throws RecipeIdExistException {
        List<String> recommendedKeywords = recommendKeywords(keyword);
        log.info("recommendedKeywords======"+recommendedKeywords);
        List<Long> recipeIds = recipeSampleRepository.findRecipeIdByrecommendedKeywords(recommendedKeywords);
        if (recipeIds.isEmpty()) {
            throw new RecipeIdExistException();
        }
        return recipeIds;
    }

    @Override
    public List<String> recommendKeywords(String keyword) throws RecipeIdExistException {

        List<String> titleList = recipeSampleRepository.findtitlelist();
        List<String> taglist = recipeSampleRepository.findtaglist();
        double similarityRatio = 0.5;
        log.info("titleList======"+titleList);
        log.info("taglist======"+taglist);
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
        log.info("recommendedKeywords======"+recommendedKeywords);
        return recommendedKeywords;
    }


    //    아래는 되는 기본 코드
//    private final ModelMapper modelMapper;
//    private final RecipeSampleRepository recipeSampleRepository;
//
//    @Override
//    public List<Long> searched(String keyword) throws RecipeIdExistException {
//        List<Long> recipeIds = recipeSampleRepository.findRecipeIdByKeyword(keyword);
//        if (recipeIds.isEmpty()) {
//            throw new RecipeIdExistException();
//        }
//        return recipeIds;
//    }
}
