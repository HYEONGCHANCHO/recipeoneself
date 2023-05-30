package com.recipeone.repository;

import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

@Mapper
public interface RecipeStore {

    public int insertRecipe(Recipe recipe, SqlSessionTemplate session);

    public int insertStep(List<RecipeStep> rmList, SqlSessionTemplate session);

//    public int  insertTag(RecipeTag rTag,SqlSessionTemplate session) ;

    public int insertIngredient(List<RecipeIngredient> rmList, SqlSessionTemplate session);


    public List<Recipe> selectAllRecipe(int currentPage, int limit,SqlSessionTemplate session);

    public Recipe selectOneRecipe(int recipeNo, SqlSessionTemplate session);

    public List<RecipeStep> selectOneRecipeDetail(int recipeNo, SqlSessionTemplate session);

    public List<RecipeIngredient> selectOneRecipeIngredient(int recipeNo,SqlSessionTemplate session);

    /*public RecipeTag selectOneRecipeTag(int recipeNo,SqlSessionTemplate session);

    public List<RecipeComment> selectRecipeCommentList(int recipeNo, SqlSessionTemplate session, int currentPage,int limit);

    public int selectRecommand(SqlSessionTemplate session,int recipeNo,String memberEmail);



    public int insertRecommand(Recommandation recommand,SqlSessionTemplate session);

    public int deleteRecommand(Recommandation recommand,SqlSessionTemplate session);*/


    public int updateOneRecipe(SqlSessionTemplate session,Recipe recipe) ;

    public int updateOneRecipeStep(SqlSessionTemplate session, List<RecipeStep> rsList);

    public int updateOneRecipeIngredient(SqlSessionTemplate session,List<RecipeIngredient>rmList);

//    public int  updateOneRecipeTag(SqlSessionTemplate session,RecipeTag rTag) ;

    public int deleteOneRecipe(SqlSessionTemplate session,int redipeNo);

    public int deleteOneImg(SqlSessionTemplate session, String picName);

    public List<Recipe> selectRecomandRecipe(SqlSessionTemplate session, String recipeCategory);

    public int selectTotalCount(SqlSessionTemplate session, int recipeNo);

    public String selectMemberName(String memberEmail, SqlSessionTemplate session);

    public int selectMyRecipe(SqlSessionTemplate session, int recipeNo, String memberEmail);

    public String selectMemberEmail(SqlSessionTemplate session, int recipeNo);
}
