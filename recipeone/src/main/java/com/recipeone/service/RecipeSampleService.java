package com.recipeone.service;

import java.util.List;
public interface RecipeSampleService {
//추가
List<String> recommendKeywords(String keyword) throws RecipeIdExistException;


    //    아래는 되는 기본코드
    static class RecipeIdExistException extends Exception{
    }
    List<Long> searched(String keyword) throws RecipeIdExistException;

}
