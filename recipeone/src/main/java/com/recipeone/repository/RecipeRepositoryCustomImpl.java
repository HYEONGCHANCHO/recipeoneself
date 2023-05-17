package com.recipeone.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipeone.dto.RecipeSearchDto;
import com.recipeone.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public RecipeRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(){
        return null;
    }

    @Override
    public Page<Recipe> getAdminRecipePage(RecipeSearchDto recipeSearchDto, Pageable pageable) {
        return null;
    }
}
