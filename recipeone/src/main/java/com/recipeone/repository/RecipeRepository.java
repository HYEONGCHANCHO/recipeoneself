package com.recipeone.repository;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.recipeone.entity.Recipe;
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {


	Optional<Recipe> findById(int id);

	@Query(value = "SELECT DISTINCT rs.title FROM Recipe rs", nativeQuery = true)
	List<String> findtitlelist();
	@Query("SELECT DISTINCT rs.tag FROM Recipe rs")
	List<String> findtaglist();


	@Query(value ="select r.id from Recipe r where r.title IN :recommendedKeywords or r.tag IN :recommendedKeywords", nativeQuery = true)
	List<Integer> findRecipeIdByrecommendedKeywords(@Param("recommendedKeywords") List<String> recommendedKeywords);

//	진행중

	@Query(value ="SELECT * FROM Recipe r " +
			"WHERE (r.title IN :recommendedKeywords OR r.tag IN :recommendedKeywords) " +
			"AND (:rctype IS NULL OR r.rctype = :rctype) " +
			"AND (:rcsituation IS NULL OR r.rcsituation = :rcsituation) " +
			"AND (:rcingredient IS NULL OR r.rcingredient = :rcingredient)" +
			"AND (:rcmeans IS NULL OR r.rcmeans = :rcmeans) "  , nativeQuery = true)
	List<Recipe> findRecipesByFilterSearched(@Param("recommendedKeywords") List<String> recommendedKeywords,
											 @Param("rctype") String rctype,
											 @Param("rcsituation") String rcsituation,
											 @Param("rcingredient") String rcingredient,
											 @Param("rcmeans") String rcmeans);

	@Query(value ="SELECT r.id FROM Recipe r " +
			"WHERE (r.title IN :recommendedKeywords OR r.tag IN :recommendedKeywords) " +
			"AND (:rctype IS NULL OR r.rctype = :rctype) " +
			"AND (:rcsituation IS NULL OR r.rcsituation = :rcsituation) " +
			"AND (:rcingredient IS NULL OR r.rcingredient = :rcingredient)" +
			"AND (:rcmeans IS NULL OR r.rcmeans = :rcmeans) "  , nativeQuery = true)
	List<Integer> findRecipeIdByfilterSearched(@Param("recommendedKeywords") List<String> recommendedKeywords,
											   @Param("rctype") String rctype,
											   @Param("rcsituation") String rcsituation,
											   @Param("rcingredient") String rcingredient,
											   @Param("rcmeans") String rcmeans);


}

