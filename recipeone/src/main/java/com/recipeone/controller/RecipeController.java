package com.recipeone.controller;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import com.recipeone.entity.Recipe;
import com.recipeone.entity.RecipeIngredient;
import com.recipeone.entity.RecipeStep;
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

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/recipe")
@Log4j2
@RequiredArgsConstructor
public class RecipeController {
	private final RecipeService recipeService;
	private final RecipeRepository recipeRepository;
	private final SqlSessionTemplate sqlSession;


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_CHEF')")
    @GetMapping(value = "/recipeForm")
    public String recipeForm(Model model) {
        model.addAttribute("recipeFormDto", new RecipeFormDto());
        return "recipe/recipeForm";
    }

	@PostMapping("/recipeForm")
	public String saveRecipe(@ModelAttribute Recipe recipe,
							 @ModelAttribute RecipeStep recipeStep,
							 @ModelAttribute RecipeIngredient rIngredient,
							 @RequestParam(value = "mainPicture") MultipartFile mainPicture,
							 @RequestParam(value = "recipePicture", required = false) List<MultipartFile> recipePicture,
							 Model model, HttpSession session, HttpServletRequest request) {
		try {
			// 레시피 전달

			// 대표사진 저장코드
			String mainPic = mainPicture.getOriginalFilename();
			if (!mainPic.equals("")) {

				String recipeImgLocation = "c:/recipe/item";
				String savePath = recipeImgLocation + "/recipeImg"; // 내가 저장할 폴더
				File file = new File(savePath); // 파일 객체 만들기

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String mainPicRename = sdf.format(new Date(System.currentTimeMillis())) + "."
						+ mainPic.substring(mainPic.lastIndexOf(".") + 1);

				if (!file.exists()) { // 경로에 savePath가 없을땐
					file.mkdir(); // 경로 만들기
				}

				mainPicture.transferTo(new File(savePath + "\\" + mainPicRename));// 파일을 buploadFile경로에 저장
				recipe.setMainPic(mainPic);
				recipe.setMainPicRename(mainPicRename);
			}
			log.info(">>>mainPic는======" + mainPic);

			recipeService.registRecipe(recipe);
			model.addAttribute("mainPic", mainPic);

			// 레시피 재료 리스트 만들어서 전달하기
			ArrayList<RecipeIngredient> rmList = new ArrayList<RecipeIngredient>();
			String amount[] = rIngredient.getAmount().split(",");
			String ingredient[] = rIngredient.getIngredient().split(",");
			int count = 1;
			for (int i = 0; i < ingredient.length; i++) {
				// 재료나 수량이 비어있지 않을때만 List에 저장
				if (!amount[i].equals("") && !ingredient[i].equals("")) {
					RecipeIngredient rIngredientOne = new RecipeIngredient();
					rIngredientOne.setAmount(amount[i]);
					rIngredientOne.setIngredient(ingredient[i]);
					rIngredientOne.setIngredientOrder(count++);
					rmList.add(rIngredientOne);
				}
			}
			log.info(">>>재료List는======" + rmList);
			recipeService.registIngredient(rmList);
			model.addAttribute("recipeIngredient", rmList);

			// 레시피 순서 리스트 만들어서 전달하기
			ArrayList<RecipeStep> rsList = new ArrayList<RecipeStep>();
			recipeStep.getRecipeDescription();
			String arrDescription[] = recipeStep.getRecipeDescription().split(",ab22bb,");
			// 더미 value까지 배열을 나누는것으로 인식해서 사용자가 ,를 입력했을때 정상적으로 table에 저장되게 한다
			arrDescription[arrDescription.length - 1] = arrDescription[arrDescription.length - 1].replace(",ab22bb",
					"");
			// 배열의 마지막은 ,가 안들어가기때문에 더미vlaue 배열값으로 인식한다, ,가 없는 더미value를 삭제 해주는 코드

			// 레시피 순서 사진 저장코드
			int countStep = 1;
			for (int i = 0; i < arrDescription.length; i++) {
				String recipePic = recipePicture.get(i).getOriginalFilename();
				String recipePicRename = "";
				if (!recipePic.equals("")) {

					String recipeImgLocation = "c:/recipe/item";
					String savePath = recipeImgLocation + "/recipeImg"; // 내가 저장할 폴더
/*					String root = request.getSession().getServletContext().getRealPath("resources");
					String savePath = root + "\\recipeImg"; // 내가 저장할 폴더*/

					File file = new File(savePath); // 파일 객체 만들기

					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					recipePicRename = sdf.format(new Date(System.currentTimeMillis())) + "stepImg" + i + "."
							+ recipePic.substring(recipePic.lastIndexOf(".") + 1);// 동시에 여러 사진이 올라가기에 이름에 순서 추가해줌

					if (!file.exists()) { // 경로에 savePath가 없을땐
						file.mkdir(); // 경로 만들기
					}
					recipePicture.get(i).transferTo(new File(savePath + "\\" + recipePicRename));// 파일을 로컬경로에
					// 저장
					///// 여기까지 사진 저장코드/////

				}
				// 여기서부터 레시피 step 테이블에 저장할 값 List화 시키는 코드
				RecipeStep rStepOne = new RecipeStep();

				rStepOne.setRecipeDescription(arrDescription[i]);

				rStepOne.setRecipeOrder(countStep++);
				rStepOne.setRecipePic(recipePic);
				rStepOne.setRecipePicRename(recipePicRename);

				rsList.add(rStepOne);
				log.info(">>>rStepOne은======" + rStepOne);
			}
			log.info(">>>순서List는======" + rsList);
			recipeService.registStep(rsList); // 레시피 순서저장 코드 종료
			model.addAttribute("recipeStep", rsList);
		} catch (Exception e) {
			log.error("레시피 저장 중 오류 발생", e);
			return "error";
		}

		return "redirect:/";
	}
    
//    수정된 부분
    @GetMapping(value = "/recipeList")
    public String recipeList() {
        return "recipe/recipeList";
    }

//    @PreAuthorize("principal.username==#boardDTO.writer") //작성자와 동일한 user만 수정 페이지 가능
//    @GetMapping("/modify")
//    public void modify() {
//    }

//추가된 부분 카테고리 반영부분
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

        List<String> recommendedKeywords = (List<String>) session.getAttribute("recommendedKeywords");
        List<Recipe> recipeList = recipeRepository.findRecipesByFilterSearched(recommendedKeywords,rctype,rcsituation,rcingredient,rcmeans); // DB에서 레시피 목록 조회
        List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipeList) {
            ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getId(), recipe.getTitle(), recipe.getImgUrl(),recipe.getTag(),recipe.getWriter());
            listRecipeDtoList.add(listRecipeDto);
        }
        redirectAttributes.addFlashAttribute("recipe", listRecipeDtoList);
        return "redirect:/recipe/recipeList"; // 원래 페이지로 리다이렉트
    }


//추천 키워드 부분
    @RequestMapping(value = "/recommendKeywords", method = RequestMethod.POST)
    @ResponseBody
    public List<String> getRecommendedKeywords(@RequestParam String keyword) {
        List<String> recommendedKeywords = null;
        try {
            recommendedKeywords = recipeService.recommendKeywords(keyword);
        } catch (RecipeService.RecipeIdExistException e) {
        }
        return recommendedKeywords;
    }


    @PostMapping("/recipeList")
    public String recipelistPOST(@RequestParam(value = "keyword", required = false) String keyword, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            List<String> recommendedKeywords = recipeService.recommendKeywords(keyword);
            session.setAttribute("recommendedKeywords", recommendedKeywords); // recommendedKeywords를 세션에 저장
            session.setAttribute("rctype", null);
            session.setAttribute("rcsituation", null);
            session.setAttribute("rcingredient", null);
            session.setAttribute("rcmeans", null);
            List<Recipe> recipeList = recipeRepository.findRecipesByFilterSearched(recommendedKeywords,null,null,null,null); // DB에서 레시피 목록 조회
            List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
            for (Recipe recipe : recipeList) {
                ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getId(), recipe.getTitle(), recipe.getImgUrl(),recipe.getTag(),recipe.getWriter());
                listRecipeDtoList.add(listRecipeDto);
            }
            redirectAttributes.addFlashAttribute("recipe", listRecipeDtoList);
        } catch (RecipeService.RecipeIdExistException e) {
            redirectAttributes.addFlashAttribute("error", "id");
        }
        return "redirect:/recipe/recipeList";
    }

}


	@GetMapping(value = "/recipeDetail/{recipeNo}")
	public String viewRecipeStep(@PathVariable int recipeNo, Model model) {

		try {
			Recipe recipe = recipeService.printOneRecipe(recipeNo);
			List<RecipeIngredient> rmList = recipeService.printOneRecipeIngredient(recipeNo);
			List<RecipeStep> rsList = recipeService.printOneRecipeStep(recipeNo);

			model.addAttribute("recipe", recipe);
			model.addAttribute("rmList", rmList);
			model.addAttribute("rsList", rsList);
		} catch (Exception e) {
			log.error("레시피 상세 로드 중 오류 발생", e);
			return "error";
		}
		return "recipe/recipeDetail";
	}






