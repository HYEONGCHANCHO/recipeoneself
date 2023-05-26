package com.recipeone.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.recipeone.dto.ListRecipeDto;
import com.recipeone.entity.Recipe;
import com.recipeone.repository.RecipeRepository;
import com.recipeone.service.RecipeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.recipeone.dto.RecipeFormDto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recipe")
@Log4j2
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_CHEF')")
    @GetMapping(value = "/recipeForm")
    public String recipeForm(Model model) {
        model.addAttribute("recipeFormDto", new RecipeFormDto());
        return "recipe/recipeForm";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_CHEF')")
    @PostMapping("/recipeForm")
    public String saveRecipe(@Valid RecipeFormDto recipeFormDto, BindingResult bindingResult, Model model,
                             @RequestParam(value = "recipeImgFile") List<MultipartFile> recipeImgFileList) {

        if (bindingResult.hasErrors()) {
            return "recipe/recipeForm";
        }

        if (recipeImgFileList.get(0).isEmpty() && recipeFormDto.getId() == null) {
            model.addAttribute("errorMessage", "레시피 썸네일 이미지는 필수 입력 값 입니다.");
            return "recipe/recipeForm";
        }

        try {
            recipeService.saveRecipe(recipeFormDto, recipeImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "recipe/recipeForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/recipeList")
    public String recipeList(@RequestParam Map<String, String> param, Model model) {
        List<Recipe> recipeList = recipeRepository.findAll(); // DB에서 레시피 목록 조회

        List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipeList) {
            ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getId(), recipe.getTitle(), recipe.getImgUrl());
            listRecipeDtoList.add(listRecipeDto);
        }

        model.addAttribute("recipe", listRecipeDtoList);
        return "recipe/recipeList";
    }

    @PreAuthorize("principal.username==#boardDTO.writer") //작성자와 동일한 user만 수정 페이지 가능
    @GetMapping("/modify")
    public void modify() {
    }

    @RequestMapping(value = "/filtersearch", method = RequestMethod.POST)
    @ResponseBody
    public List<Long> goSearchRecipe2(@RequestParam(value = "rctype", required = false) String rctype,
                                @RequestParam(value = "rcsituation", required = false) String rcsituation,
                                @RequestParam(value = "rcingredient", required = false) String rcingredient,
                                @RequestParam(value = "rcmeans", required = false) String rcmeans,
                                HttpSession session, Model model) {
        log.info(rctype + "rctype");
        log.info(rcsituation + "rcsituation");
        log.info(rcingredient + "rcingredient");
        log.info(rcmeans + "rcmeans");

        List<String> recommendedKeywords = (List<String>) session.getAttribute("recommendedKeywords");
        // recommendedKeywords 값을 사용하여 원하는 작업 수행
        log.info(recommendedKeywords + "recommendedKeywords이거이거");

        List<Long> recipeIds = null;
//        List<Recipe> recipecont = null;

        try {
            recipeIds = recipeService.filterSearched(recommendedKeywords, rctype, rcsituation, rcmeans, rcingredient);
//
        } catch (RecipeService.RecipeIdExistException e) {
            // 예외 처리 코드
        }

        return recipeIds;
    }
//    @GetMapping("/recipelista")

    @RequestMapping(value = "/recipelista")
//    @ResponseBody
    public String handleRecipelistaRequest(@RequestParam Map<String, String> param, RedirectAttributes redirectAttributes) {
        log.info("rctype: {}");
        return "recipe/recipeList";
    }
    @PostMapping("/recipelista")
    public String handleRecipelistaRequest2(@RequestParam Map<String, String> param, RedirectAttributes redirectAttributes) {
        log.info("rctype: {}22");
        return "recipe/recipeList";
    }

    // POST 요청 처리
    @PostMapping("/sendData")
    public String handleFormData(@RequestParam(value = "rctype", required = false) String rctype,
                                 @RequestParam(value = "rcsituation", required = false) String rcsituation,
                                 @RequestParam(value = "rcingredient", required = false) String rcingredient,
                                 @RequestParam(value = "rcmeans", required = false) String rcmeans,HttpSession session, RedirectAttributes redirectAttributes) {


        if (rctype != null) {
            session.setAttribute("rctype", rctype);
        } else {
            rctype = (String) session.getAttribute("rctype");
        }

        if (rcsituation != null) {
            session.setAttribute("rcsituation", rcsituation);
        } else {
            rcsituation = (String) session.getAttribute("rcsituation");
        }

        if (rcingredient != null) {
            session.setAttribute("rcingredient", rcingredient);
        } else {
            rcingredient = (String) session.getAttribute("rcingredient");
        }

        if (rcmeans != null) {
            session.setAttribute("rcmeans", rcmeans);
        } else {
            rcmeans = (String) session.getAttribute("rcmeans");
        }

        log.info("aaaddd");
        List<String> recommendedKeywords = (List<String>) session.getAttribute("recommendedKeywords");
   List<Recipe> recipeList = recipeRepository.findRecipesByFilterSearched(recommendedKeywords,rctype,rcsituation,rcingredient,rcmeans); // DB에서 레시피 목록 조회
        List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipeList) {
            ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getId(), recipe.getTitle(), recipe.getImgUrl());
            listRecipeDtoList.add(listRecipeDto);
        }
        redirectAttributes.addFlashAttribute("recipe2", listRecipeDtoList);
        log.info("aaaddd2");
        return "redirect:/recipe/recipeList"; // 원래 페이지로 리다이렉트
    }
// @GetMapping("/recipelista")
//    public String handleRecipelistaRequest(@RequestParam(value = "rctype", required = false) String rctype,
//                                           @RequestParam(value = "rcsituation", required = false) String rcsituation,
//                                           @RequestParam(value = "rcingredient", required = false) String rcingredient,
//                                           @RequestParam(value = "rcmeans", required = false) String rcmeans,
//                                           HttpSession session,Model model, RedirectAttributes redirectAttributes) {

//        String rcsituation = null;
//        String rcingredient = null;
//        String rcmeans = null;
//        List<String> recommendedKeywords = (List<String>) session.getAttribute("recommendedKeywords");
//        log.info("aaaaaaaaaaaaaaaaaaa1");
//   List<Recipe> recipeList = recipeRepository.findRecipesByFilterSearched(recommendedKeywords,rctype,rcsituation,rcingredient,rcmeans); // DB에서 레시피 목록 조회
//        log.info("aaaaaaaaaaaaaaaaaaa2");
//        List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
//        for (Recipe recipe : recipeList) {
//            ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getId(), recipe.getTitle(), recipe.getImgUrl());
//            listRecipeDtoList.add(listRecipeDto);
//        }
//
//        log.info(listRecipeDtoList+"aaaaaaaaaaaaaaaaaaa");
//        log.info("aaaaaaaaaaaaaaaaaaa");
//        model.addAttribute("recipe2", listRecipeDtoList);
//        return "recipe/recipeList";
//    }


    @RequestMapping(value = "/recommendKeywords", method = RequestMethod.POST)
    @ResponseBody
    public List<String> getRecommendedKeywords(@RequestParam String keyword) {
        List<String> recommendedKeywords = null;
        log.info(keyword+"qqqqqqqqq");
        try {
            recommendedKeywords = recipeService.recommendKeywords(keyword);
        } catch (RecipeService.RecipeIdExistException e) {
            // 예외 처리 코드
        }
        log.info(recommendedKeywords+"recommendedKeywords");
        return recommendedKeywords;
    }


    @PostMapping("/recipeList")
    public String recipelistPOST(@RequestParam(value = "keyword", required = false) String keyword, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            List<String> recommendedKeywords = recipeService.recommendKeywords(keyword);
            List<Long> recipeIds = recipeService.searched(keyword);
            log.info("recipeIds======" + recipeIds);
            log.info("recommendedKeywords======" + recommendedKeywords);
            redirectAttributes.addFlashAttribute("recipeIds", recipeIds);
            redirectAttributes.addFlashAttribute("result", "success");
            session.setAttribute("recommendedKeywords", recommendedKeywords); // recommendedKeywords를 세션에 저장
//            session.setAttribute("recipeIds", recipeIds); // recommendedKeywords를 세션에 저장
        } catch (RecipeService.RecipeIdExistException e) {
            redirectAttributes.addFlashAttribute("error", "id");
        }
        return "redirect:/recipe/recipeList";
    }



}




