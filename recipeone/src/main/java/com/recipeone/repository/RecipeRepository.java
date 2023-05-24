package com.recipeone.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.recipeone.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	Optional<Recipe> findById(Long id);

	@Query("SELECT DISTINCT rs.title FROM Recipe rs")
	List<String> findtitlelist();
	@Query("SELECT DISTINCT rs.tag FROM Recipe rs")
	List<String> findtaglist();

	@Query("select r.id from Recipe r where r.title IN :recommendedKeywords or r.tag IN :recommendedKeywords")
	List<Long> findRecipeIdByrecommendedKeywords(@Param("recommendedKeywords") List<String> recommendedKeywords);

//	진행중

	@Query("SELECT r FROM Recipe r " +
			"WHERE (r.title IN :recommendedKeywords OR r.tag IN :recommendedKeywords) " +
			"AND (:rctype IS NULL OR r.rctype = :rctype) " +
			"AND (:rcsituation IS NULL OR r.rcsituation = :rcsituation) " +
			"AND (:rcmeans IS NULL OR r.rcmeans = :rcmeans) " +
			"AND (:rcingredient IS NULL OR r.rcingredient = :rcingredient)")
	List<Recipe> findRecipesByFilterSearched(@Param("recommendedKeywords") List<String> recommendedKeywords,
											 @Param("rctype") String rctype,
											 @Param("rcsituation") String rcsituation,
											 @Param("rcmeans") String rcmeans,
											 @Param("rcingredient") String rcingredient);

	@Query("SELECT r.id FROM Recipe r " +
			"WHERE (r.title IN :recommendedKeywords OR r.tag IN :recommendedKeywords) " +
			"AND (:rctype IS NULL OR r.rctype = :rctype) " +
			"AND (:rcsituation IS NULL OR r.rcsituation = :rcsituation) " +
			"AND (:rcmeans IS NULL OR r.rcmeans = :rcmeans) " +
			"AND (:rcingredient IS NULL OR r.rcingredient = :rcingredient)")
	List<Long> findRecipeIdByfilterSearched(@Param("recommendedKeywords") List<String> recommendedKeywords,
											@Param("rctype") String rctype,
											@Param("rcsituation") String rcsituation,
											@Param("rcmeans") String rcmeans,
											@Param("rcingredient") String rcingredient);


}
