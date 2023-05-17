package com.recipeone.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.recipeone.entity.RecipeImg;
import com.recipeone.repository.RecipeImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeImgService {
	

    @Value("${recipeImgLocation}")
    private String recipeImgLocation;

    private final RecipeImgRepository recipeImgRepository;

    private final FileService fileService;

    public void saveRecipeImg(RecipeImg recipeImg, MultipartFile recipeImgFile) throws Exception {
        String oriImgName = recipeImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
       

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(recipeImgLocation, oriImgName, recipeImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        // 상품 이미지 정보 저장
        recipeImg.updateRecipeImg(oriImgName, imgName, imgUrl);
        recipeImgRepository.save(recipeImg);
    }

    public void updateRecipeImg(Long recipeImgId, MultipartFile recipeImgFile) throws Exception {
        if(!recipeImgFile.isEmpty()){
        	RecipeImg savedRecipeImg = recipeImgRepository.findById(recipeImgId).orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedRecipeImg.getImgName())){
                fileService.deleteFile(recipeImgLocation+"/"+savedRecipeImg.getImgName());
            }
            String oriImgName = recipeImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(recipeImgLocation, oriImgName, recipeImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedRecipeImg.updateRecipeImg(oriImgName, imgName, imgUrl);
        }
    }
}
