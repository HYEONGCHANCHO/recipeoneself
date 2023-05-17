package com.recipeone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecipeSearchDto {

    private String searchDateType;

    private String searchBy;

    private String searchQuery = "";
}
