package com.recipeone.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.recipeone.dto.RecipeFormDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "recipe")
public class Recipe extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // 레시피 아이디

	private String writer; // 사용자 아이디
	
	//@GeneratedValue(strategy = GenerationType.AUTO)
	//private Integer num; // 레시피 게시글 번호
	
	@Column(nullable = false, length = 50)
	private String title; // 레시피 제목
	
	@OneToMany(mappedBy = "recipe", cascade=CascadeType.PERSIST)
	private List<RecipeStep> recipeSteps = new ArrayList<>(); // 레시피 내용(요리순서)
	
	private String cooktime; // 조리시간 (요리정보)
	private String nop; // @인분 (요리정보)
	//private String rc_thumbnail; // 썸네일 이미지
	
	@OneToMany(mappedBy = "recipe", cascade=CascadeType.PERSIST, orphanRemoval = true)
	private List<RecipeIngredient> recipeIngredients = new ArrayList<>(); // 레시피 재료
	private String tag; // 태그
	private String rcType; // 카테고리 종류별
	private String rcSituation; // 카테고리 상황별
	private String rcIngredient; // 카테고리 재료별
	private String rcMeans; // 카테고리 방법별
	private String rcTheme; // 카테고리 테마별
	private String rcDate; // 레시피 입력 날짜, 시간
	private String rcCount; // 레시피 조회수

	public void createRecipe(RecipeFormDto recipeFormDto) { 
		this.title = recipeFormDto.getTitle(); 
		this.cooktime = recipeFormDto.getCooktime(); 
		this.nop = recipeFormDto.getNop();
		//this.rc_thumbnail = recipeFormDto.getRc_thumbnail(); 
		this.rcType = recipeFormDto.getRcType(); 
		this.rcSituation = recipeFormDto.getRcSituation(); 
		this.rcIngredient = recipeFormDto.getRcIngredient(); 
		this.rcMeans = recipeFormDto.getRcMeans(); 
		//this.recipeSteps = recipeFormDto.getRecipeSteps(); 
		//this.recipeIngredients = recipeFormDto.getRecipeIngredients();
	 }
	public void addIngredient(RecipeIngredient ingredient) {
		recipeIngredients.add(ingredient);
		ingredient.setRecipe(this);
    }

    public void removeIngredient(RecipeIngredient ingredient) {
    	recipeIngredients.remove(ingredient);
        ingredient.setRecipe(null);
    }

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RecipeImg> recipeImgs = new ArrayList<>();

	public String getImgUrl() {
		if (!recipeImgs.isEmpty()) {
			return recipeImgs.get(0).getImgUrl();
		}
		return null;
	}

}
