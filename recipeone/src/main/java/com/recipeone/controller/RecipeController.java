package com.recipeone.controller;

import com.recipeone.repository.RecipeSampleRepository;
import com.recipeone.service.RecipeSampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/recipe")
@Log4j2
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeSampleService recipeSampleService;

    private final RecipeSampleRepository recipeSampleRepository;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'CHEF')")
    @GetMapping("/register")
    public String registerGET() {
        return "/recipe/register";
    }

    @PreAuthorize("principal.username==#boardDTO.writer") //작성자와 동일한 user만 수정 페이지 가능
    @GetMapping("/modify")
    public void modify() {
    }

    @GetMapping("/recipelist")
    public void recipelistGET() {

    }


    @RequestMapping(value="/recommendKeywords", method=RequestMethod.POST)
    @ResponseBody
    public List<String> getRecommendedKeywords(@RequestParam String keyword) {
        List<String> recommendedKeywords = null;
        try {
            recommendedKeywords = recipeSampleService.recommendKeywords(keyword);
        } catch (RecipeSampleService.RecipeIdExistException e) {
            // 예외 처리 코드
        }
        return recommendedKeywords;
    }

//아래는 되는 기본 코드
    @PostMapping("/recipelist")
    public String recipelistPOST(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes, Model model) {
        try {
            List<Long> recipeIds = recipeSampleService.searched(keyword);
            log.info("recupeIds는======"+recipeIds);
            redirectAttributes.addFlashAttribute("recipeIds", recipeIds);
            redirectAttributes.addFlashAttribute("result", "success");
        } catch (RecipeSampleService.RecipeIdExistException e) {
            redirectAttributes.addFlashAttribute("error", "id");
        }
        return "redirect:/recipe/recipelist";
    }
}
