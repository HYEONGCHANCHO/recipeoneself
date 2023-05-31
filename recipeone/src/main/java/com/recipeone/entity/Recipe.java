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
	private int recipeno; // 레시피 아이디
	//private String writer; // 사용자 아이디
	private String title; // 레시피 제목
	private String cooktime; // 조리시간 (요리정보)
	private String nop; // @인분 (요리정보)
	private String mainpic; // 썸네일 이미지
	private String mainpicrename; // 썸네일 이미지 수정 후 이름
	private String tag; // 태그
	private String rctype; // 카테고리 종류별
	private String rcsituation; // 카테고리 상황별
	private String rcingredient; // 카테고리 재료별
	private String rcmeans; // 카테고리 방법별
	//private String rcTheme; // 카테고리 테마별
	private Date insertdate; // 레시피 최초 입력 시간
	private Date updatedate; // 레시피 업데이트 시간
	private Integer rccount; // 레시피 조회수
	private String recipestatus;
	private String writer;

	public Recipe() {
	}

	public Recipe(int recipeno, String title, String cooktime, String nop, String mainpic,
			String mainpicrename, String tag, String rctype, String rcsituation, String rcingredient,
			String rcmeans, Date insertdate, Date updatedate, Integer rccount, String recipestatus, String writer) {
		this.recipeno = recipeno;
		//this.writer = writer;
		this.title = title; 
		this.cooktime =cooktime; 
		this.nop = nop;
		this.mainpic = mainpic;
		this.mainpicrename = mainpicrename;
		this.tag = tag;
		this.rctype = rctype;
		this.rcsituation = rcsituation;
		this.rcingredient = rcingredient;
		this.rcmeans = rcmeans;
		this.insertdate = insertdate;
		this.updatedate = updatedate;
		this.rccount = rccount;
		this.recipestatus = recipestatus;
		this.writer = writer;
	 }
}
