package com.recipeone.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ListRecipeDto {

	private Long id;
	
	private String title;

	private String imgUrl;

	@QueryProjection
	public ListRecipeDto(Long id, String title, String imgUrl){
		this.id = id;
		this.title = title;
		this.imgUrl = imgUrl;
	}
}
