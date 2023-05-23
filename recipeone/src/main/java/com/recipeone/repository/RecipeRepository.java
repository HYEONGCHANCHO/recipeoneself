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
//	@Query("select r.id from Recipe r where (r.title IN :recommendedKeywords or r.tag IN :recommendedKeywords)" +
//			"and (r.rctype = :rctype) " +
//			"and (r.rcsituation = :rcsituation) " +
//			"and ( r.rcmeans = :rcmeans) " +
//			"and ( r.rcingredient = :rcingredient)")
//	List<Long> findRecipeIdByfilterSearched(@Param("recommendedKeywords") List<String> recommendedKeywords,
//											@Param("rctype") String rctype,
//											@Param("rcsituation") String rcsituation,
//											@Param("rcmeans") String rcmeans,
//											@Param("rcingredient") String rcingredient);
@Query("select r.id from Recipe r where (r.title IN :recommendedKeywords or r.tag IN :recommendedKeywords)" +
			"and (:rctype = '' or r.rctype = :rctype) " +
			"and (:rcsituation = '' or r.rcsituation = :rcsituation) " +
			"and (:rcmeans = '' or r.rcmeans = :rcmeans) " +
			"and (:rcingredient = '' or r.rcingredient = :rcingredient)")
	List<Long> findRecipeIdByfilterSearched(@Param("recommendedKeywords") List<String> recommendedKeywords,
											@Param("rctype") String rctype,
											@Param("rcsituation") String rcsituation,
											@Param("rcmeans") String rcmeans,
											@Param("rcingredient") String rcingredient);


}
