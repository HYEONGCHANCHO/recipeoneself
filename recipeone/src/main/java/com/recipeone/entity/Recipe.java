package com.recipeone.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Recipe {
	
	@Id
	private int recipeNo; // 레시피 아이디
	//private String writer; // 사용자 아이디
	private String title; // 레시피 제목
	private String cooktime; // 조리시간 (요리정보)
	private String nop; // @인분 (요리정보)
	private String mainPic; // 썸네일 이미지
	private String mainPicRename; // 썸네일 이미지 수정 후 이름
	private String tag; // 태그
	private String rcType; // 카테고리 종류별
	private String rcSituation; // 카테고리 상황별
	private String rcIngredient; // 카테고리 재료별
	private String rcMeans; // 카테고리 방법별
	//private String rcTheme; // 카테고리 테마별
	private Date insertDate; // 레시피 최초 입력 시간
	private Date updateDate; // 레시피 업데이트 시간
	private int rcCount; // 레시피 조회수
	private String recipeStatus;
	
	public Recipe() {
	}

	public Recipe(int recipeNo, String title, String cooktime, String nop, String mainPic,
			String mainPicRename, String tag, String rcType, String rcSituation, String rcIngredient,
			String rcMeans, Date insertDate, Date updateDate, int rcCount, String recipeStatus) {
		this.recipeNo = recipeNo;
		//this.writer = writer;
		this.title = title; 
		this.cooktime =cooktime; 
		this.nop = nop;
		this.mainPic = mainPic;
		this.mainPicRename = mainPicRename;
		this.tag = tag;
		this.rcType = rcType; 
		this.rcSituation = rcSituation; 
		this.rcIngredient = rcIngredient; 
		this.rcMeans = rcMeans;
		this.insertDate = insertDate;
		this.updateDate = updateDate;
		this.rcCount = rcCount;
		this.recipeStatus = recipeStatus;
	 }
}
