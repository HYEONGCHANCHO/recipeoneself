package com.recipeone.service;
import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
@Service
public interface RecipeService {
    //추가
    List<String> recommendKeywords(String keyword) throws RecipeIdExistException;
    //    아래는 되는 기본코드
    static class RecipeIdExistException extends Exception{
    }
    List<Long> searched(String keyword) throws RecipeIdExistException;

    public int registRecipe(Recipe recipe);

	public int registStep(List<RecipeStep> rsList);

	//public int registTag(RecipeTag rTag);

	public int registIngredient(List<RecipeIngredient> rmList) ;
	
	//public List<RecipeComment> printRecipeCommentList(int recipeNo, int currentPage, int boardLimit);

	public int checkRecommand(int recipeNo, String memberEmail);

	public int allRecipeCommentList(int page, int limit, int recipeNo);

	//public int setRecommand(Recommandation recommand) ;

	//public int removeRecommand(Recommandation recommand) ;

	public int modifyOneRecipe(Recipe recipe) ;

	public int modifyOneRecipeStep(List<RecipeStep> rsList) ;

	public int  modifyOneRecipeIngredient(List<RecipeIngredient> rmList);

	//public int modifyOneRecipeTag(RecipeTag rTag);

	public int removeOneRecipe(int recipeNo);

	public List<Recipe> printRecipeList(int currentPage,int limit);
	
	public Recipe printOneRecipe(int recipeNo) ;
	public List<RecipeStep> printOneRecipeStep(int recipeNo) ;
	
	public List<RecipeIngredient> printOneRecipeIngredient(int recipeNo) ;
	
	
	//public RecipeTag printOneRecipeTag(int recipeNo)  ;

	public int removeOneImg(String picName);

	public List<Recipe> recomadRecipe(String recipeCategory);

	public int getTotalCount(int recipeNo);

	public String printMemberName(String memberEmail);

	public int checkMyRecipe(int recipeNo, String memberEmail);

	public String getMemberEmial(int recipeNo);
	
	
    List<String> recommendKeywords(String keyword) throws RecipeIdExistException;

    static class RecipeIdExistException extends Exception { }

    static class RecipeExistException extends Exception { }

    List<Long> searched(String keyword) throws RecipeIdExistException;

    public Long saveRecipe(RecipeFormDto recipeFormDto, List<MultipartFile> recipeImgFileList) throws Exception;

    public void addIngredientToRecipe(Long recipeId, RecipeIngredientDto recipeIngredientDto);

    public Recipe getRecipeById(Long recipeId);

    public List<Long> filterSearchedId(List<String> recommendedKeywords, String rctype, String rcsituation, String rcmeans, String rcingredient) throws RecipeIdExistException;

    public List<Recipe> filterSearchedRecipe(List<String> recommendedKeywords, String rctype, String rcsituation, String rcmeans, String rcingredient,  Model model) throws RecipeExistException;

}
