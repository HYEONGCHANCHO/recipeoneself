package com.recipeone.repository;

import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;
import com.recipeone.repository.RecipeStore;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Primary
public class RecipeStoreImpl implements RecipeStore {
    @Override
    public int insertRecipe(Recipe recipe, SqlSessionTemplate session) {
        int result = session.insert("com.recipeone.repository.RecipeStore.insertRecipe", recipe);
        /*result += session.insert("RecipeMapper.insertPoint",recipe.getMemberEmail());
        result+= session.update("RecipeMapper.updatePiont",recipe.getMemberEmail());*/
        return result;
    }

    @Override
    public int insertStep(List<RecipeStep> rmList, SqlSessionTemplate session) {
        int result = 0;
        for (int i = 0; i <rmList.size(); i++) {
            result = session.insert("com.recipeone.repository.RecipeStore.insertRecipeStep", rmList.get(i));
        }
        return result;
    }

    @Override
    public int insertIngredient(List<RecipeIngredient> rmList, SqlSessionTemplate session) {
        int result = 0;
        for (int i = 0; i < rmList.size(); i++) {
            RecipeIngredient rIngredient = rmList.get(i);
            result += session.insert("com.recipeone.repository.RecipeStore.insertRecipeIngredient", rIngredient);
        }
        return result;
    }

    @Override
    public List<Recipe> selectAllRecipe(int currentPage, int limit, SqlSessionTemplate session) {
        List<Recipe> rList = session.selectList("com.recipeone.repository.RecipeStore.selectAllRecipe");
        return rList;
    }

    @Override
    public Recipe selectOneRecipe(int recipeno, SqlSessionTemplate session) {
        int result = session.update("com.recipeone.repository.RecipeStore.countPlus", recipeno);
        Recipe recipe = session.selectOne("com.recipeone.repository.RecipeStore.selectOneRecipe", recipeno);
        return recipe;
    }

    @Override
    public List<RecipeStep> selectOneRecipeDetail(int recipeno, SqlSessionTemplate session) {
        List<RecipeStep> rsList = session.selectList("com.recipeone.repository.RecipeStore.selectOneRStep", recipeno);
        return rsList;
    }

    @Override
    public List<RecipeIngredient> selectOneRecipeIngredient(int recipeno, SqlSessionTemplate session) {
        List<RecipeIngredient> rmList = session.selectList("com.recipeone.repository.RecipeStore.selectOneRIngredient", recipeno);
        return rmList;
    }

    @Override
    public int updateOneRecipe(SqlSessionTemplate session, Recipe recipe) {
        int result = session.update("com.recipeone.repository.RecipeStore.updateOneRecipe", recipe);
        return result;
    }

    @Override
    public int updateOneRecipeStep(SqlSessionTemplate session, List<RecipeStep> rsList) {
        int result = 0;
        for (int i = 0; i < rsList.size(); i++) {
            result += session.update("com.recipeone.repository.RecipeStore.updateOneStep", rsList.get(i));
        }

        if(result<rsList.size()) {
            for(int i=result; i<rsList.size();i++) {
                result+= session.insert("com.recipeone.repository.RecipeStore.insertRstepPlus",rsList.get(i));
            }
        }
        return result;
    }

    @Override
    public int updateOneRecipeIngredient(SqlSessionTemplate session, List<RecipeIngredient> rmList) {
        int result = 0;
        for (int i = 0; i < rmList.size(); i++) {
            result += session.update("com.recipeone.repository.RecipeStore.updateOneIngredient", rmList.get(i));
        }
        if(result<rmList.size()) {
            //null방지코드
            for(int i=result; i<rmList.size(); i++) {
                if(rmList.get(i).getAmount() == null) {
                    rmList.get(i).setAmount("");
                }
                if(rmList.get(i).getIngredient() == null) {
                    rmList.get(i).setIngredient("");
                }
                session.insert("com.recipeone.repository.RecipeStore.insertRecipeIngredientPlus", rmList.get(i));
            }
        }

        int count =session.selectOne("com.recipeone.repository.RecipeStore.countIngredient",rmList.get(0).getRecipeno());
        if(count>rmList.size()) {
            for(int i =rmList.size(); i<count; i++) {
                HashMap<String, Integer> paraMap = new HashMap<String, Integer>();
                paraMap.put("recipeno", rmList.get(0).getRecipeno());
                paraMap.put("ingredientOrder", i);
                result = session.delete("com.recipeone.repository.RecipeStore.deleteOneIngredient",paraMap);
            }
        }
        return result;
    }

    @Override
    public int deleteOneRecipe(SqlSessionTemplate session, int redipeNo) {
        int result = session.update("com.recipeone.repository.RecipeStore.deleteRecipe", redipeNo);
        return result;
    }

    @Override
    public int deleteOneImg(SqlSessionTemplate session, String picName) {
        int result = session.update("com.recipeone.repository.RecipeStore.deleteOneImg", picName);
        return result;
    }

    @Override
    public List<Recipe> selectRecomandRecipe(SqlSessionTemplate session, String recipeCategory) {
        return null;
    }

    // 댓글 갯수 가져오기
    @Override
    public int selectTotalCount(SqlSessionTemplate session, int recipeno) {
        int count = session.selectOne("com.recipeone.repository.RecipeStore.selectCommentCount",recipeno);
        return count;
    }

    @Override
    public String selectMemberName(String memberEmail, SqlSessionTemplate session) {
        return null;
    }

    @Override
    public int selectMyRecipe(SqlSessionTemplate session, int recipeno, String memberEmail) {
        return 0;
    }

    @Override
    public String selectMemberEmail(SqlSessionTemplate session, int recipeno) {
        return null;
    }
}

