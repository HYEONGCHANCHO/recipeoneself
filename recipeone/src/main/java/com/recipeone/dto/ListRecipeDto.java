package com.recipeone.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter @Setter
public class ListRecipeDto {


	private int recipeno;
	
	private String title;

	private String mainpicrename;
	private String tag;
	private String writer;

	@QueryProjection
	public ListRecipeDto(int recipeno, String title, String mainpicrename, String tag, String writer){
		this.recipeno = recipeno;
		this.title = title;
		this.mainpicrename = mainpicrename;
		this.tag = tag;
		this.writer = writer;
	}
}
