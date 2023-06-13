package com.recipeone.entity;

import lombok.Data;

@Data
public class Pagination {

    private int startIndex; // 페이지 번호
    private int limit; // 페이지 출력 갯수
    private int totalPage; // 총 페이지 수
    private int blockSize = 10; // 페이지 번호 최대 출력 갯수

    public Pagination(int startIndex, int limit, int totalPage) {
        this.startIndex = startIndex;
        this.limit = limit;
        this.totalPage = totalPage;
    }
}
