package com.recipeone.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


//    진행중
@PostMapping("/testa")
public String testa() {
    System.out.println("----------testa------------");
    return "ok";
}

    @GetMapping("/test")
    public void test() {
    }
    @PostMapping("/test")
    public String tests() {
        return "redirect:/recipe/test";
}

    @RequestMapping(value = "/filtersearch", method = RequestMethod.POST)
    @ResponseBody
    public List<Long> goSearchRecipe2(@RequestParam(value = "rctype", required = false) String rctype,
                                @RequestParam(value = "rcsituation", required = false) String rcsituation,
                                @RequestParam(value = "rcingredient", required = false) String rcingredient,
                                @RequestParam(value = "rcmeans", required = false) String rcmeans,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        log.info(rctype + "rctype");
        log.info(rcsituation + "rcsituation");
        log.info(rcingredient + "rcingredient");
        log.info(rcmeans + "rcmeans");

        List<String> recommendedKeywords = (List<String>) session.getAttribute("recommendedKeywords");
        // recommendedKeywords 값을 사용하여 원하는 작업 수행
        log.info(recommendedKeywords + "recommendedKeywords이거이거");

        List<Long> recipeIds = null;

        try {
            recipeIds = recipeService.filterSearched(recommendedKeywords, rctype, rcsituation, rcmeans, rcingredient);
//            redirectAttributes.addFlashAttribute("recipeIds", recipeIds);
//            redirectAttributes.addFlashAttribute("result", "success");
            log.info(recipeIds + "recipeIds이거이거22");
//            session.setAttribute("recipeIds", recipeIds);
        } catch (RecipeService.RecipeIdExistException e) {
            // 예외 처리 코드
        }
        log.info(recipeIds + "recipeIds이거이거");

        return recipeIds;
    }


//    @RequestMapping(value = "/filtersearch", method = RequestMethod.POST)
//    @ResponseBody
//    public void goSearchRecipe2(@RequestParam(value = "rctype", required = false) String rctype,
//                                                @RequestParam(value = "rcsituation", required = false) String rcsituation,
//                                                @RequestParam(value = "rcingredient", required = false) String rcingredient,
//                                                @RequestParam(value = "rcmeans", required = false) String rcmeans) {
//    log.info(rctype+"aaaaaaaa");
//    log.info(rcsituation+"aaaaaaaa");
//    log.info(rcingredient+"aaaaaaaa");
//    log.info(rcmeans+"aaaaaaaa");
//
//    }

//    } @RequestMapping(value = "/filtersearch", method = RequestMethod.POST)
//    @ResponseBody
//    public List<Long> goSearchRecipe2(@RequestParam String rctype, @RequestParam String rcsituation, @RequestParam String rcmeans, @RequestParam String rcingredient, @RequestParam(required = false, defaultValue = "") String keyword) {
//       log.info(rctype);
//       log.info(rcsituation);
//       log.info(rcmeans);
//       log.info(rcingredient);
//       log.info(keyword);
//       log.info("----------------------");
//
//        List<Long> recipeIds = null;
//
//        try {
//            List<String> recommendedKeywords = recipeService.recommendKeywords(keyword);
////            List<String> recommendedKeywords = recipeService.recommendKeywords(keyword);
//            recipeIds = recipeService.filterSearched(recommendedKeywords, rctype, rcsituation, rcmeans, rcingredient);
//        } catch (RecipeService.RecipeIdExistException e) {
//            // 예외 처리 코드
//        }
//        return recipeIds;
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

//    @PostMapping("/recipeList")
//    public String recipelistPOST(@RequestParam(value = "keyword", required = false) String keyword, RedirectAttributes redirectAttributes, Model model) {
//        try {
//            List<String> recommendedKeywords = recipeService.recommendKeywords(keyword);
//            List<Long> recipeIds = recipeService.searched(keyword);
//            log.info("recipeIds======" + recipeIds);
//            log.info("recommendedKeywords======" + recommendedKeywords);
//            redirectAttributes.addFlashAttribute("recommendedKeywords", recommendedKeywords);
//            redirectAttributes.addFlashAttribute("recipeIds", recipeIds);
//            redirectAttributes.addFlashAttribute("result", "success");
//        } catch (RecipeService.RecipeIdExistException e) {
//            redirectAttributes.addFlashAttribute("error", "id");
//        }
//        return "redirect:/recipe/recipeList";
//    }
}




