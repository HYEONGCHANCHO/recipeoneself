package com.recipeone.repository;

import com.recipeone.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

	Optional<Recipe> findById(int id);

	@Query(value = "SELECT DISTINCT rs.title FROM Recipe rs", nativeQuery = true)
	List<String> findtitlelist();
	@Query("SELECT DISTINCT rs.tag FROM Recipe rs")
	List<String> findtaglist();


	@Query(value ="select r.id from Recipe r where r.title IN :recommendedKeywords or r.tag IN :recommendedKeywords", nativeQuery = true)
	List<Integer> findRecipeIdByrecommendedKeywords(@Param("recommendedKeywords") List<String> recommendedKeywords);

	@Query(value ="SELECT r.* FROM Recipe r " +
			"WHERE (r.title IN :recommendedKeywords OR r.tag IN :recommendedKeywords) " +
			"AND (:rctype IS NULL OR r.rctype = :rctype) " +
			"AND (:rcsituation IS NULL OR r.rcsituation = :rcsituation) " +
			"AND (:rcmeans IS NULL OR r.rcmeans = :rcmeans) " +
			"AND (:rcingredient IS NULL OR r.rcingredient = :rcingredient)" +
			"AND r.recipestatus = 'Y'", nativeQuery = true)
	List<Recipe> findRecipesByFilterSearched(@Param("recommendedKeywords") List<String> recommendedKeywords,
											 @Param("rctype") String rctype,
											 @Param("rcsituation") String rcsituation,
											 @Param("rcingredient") String rcingredient,
											 @Param("rcmeans") String rcmeans);

	@Query(value ="SELECT r.id FROM Recipe r " +
			"WHERE (r.title IN :recommendedKeywords OR r.tag IN :recommendedKeywords) " +
			"AND (:rctype IS NULL OR r.rctype = :rctype) " +
			"AND (:rcsituation IS NULL OR r.rcsituation = :rcsituation) " +
			"AND (:rcmeans IS NULL OR r.rcmeans = :rcmeans) " +
			"AND (:rcingredient IS NULL OR r.rcingredient = :rcingredient)", nativeQuery = true)
	List<Integer> findRecipeIdByfilterSearched(@Param("recommendedKeywords") List<String> recommendedKeywords,
											   @Param("rctype") String rctype,
											   @Param("rcsituation") String rcsituation,
											   @Param("rcingredient") String rcingredient,
											   @Param("rcmeans") String rcmeans);

	@Modifying
	@Transactional
	@Query(value ="update Recipe r set r.writer = :usernickname where r.writer = :oldusernickname", nativeQuery = true)
	void updaterecipeusernickname(@Param("usernickname")String usernickname,@Param("oldusernickname") String oldusernickname);

	@Query(value ="SELECT * FROM Recipe r WHERE r.writer = :usernickname", nativeQuery = true)
	List<Recipe> findRecipeByUserNickname(@Param("usernickname") String usernickname);

	@Query(value ="SELECT COUNT(r.recipeno) FROM Recipe r WHERE r.writer = :usernickname", nativeQuery = true)
	int countByUserNickname(@Param("usernickname") String usernickname);

}

