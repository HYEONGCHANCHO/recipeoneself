package com.recipeone.repository;

import com.recipeone.entity.RecipeSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeSampleRepository extends JpaRepository<RecipeSample,String> {

    @Query("SELECT DISTINCT rs.title FROM RecipeSample rs")
    List<String> findtitlelist();
    @Query("SELECT DISTINCT rs.tag FROM RecipeSample rs")
    List<String> findtaglist();

    @Query("select r.id from RecipeSample r where r.title IN :recommendedKeywords or r.tag IN :recommendedKeywords")
    List<Long> findRecipeIdByrecommendedKeywords(@Param("recommendedKeywords") List<String> recommendedKeywords);

}


