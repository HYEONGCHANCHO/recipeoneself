package com.recipeone.controller;

import com.recipeone.dto.ListRecipeDto;
import com.recipeone.entity.*;
import com.recipeone.repository.MemberLogRepository;
import com.recipeone.repository.MemberRepository;
import com.recipeone.repository.RecipeRepository;
import com.recipeone.security.dto.MemberSecurityDTO;
import com.recipeone.service.FileService;
import com.recipeone.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/recipe")
@Log4j2
@RequiredArgsConstructor
public class RecipeController {
	private final RecipeService recipeService;
	private final RecipeRepository recipeRepository;
	private final MemberRepository memberRepository;
	private final MemberLogRepository memberLogRepository;

	@Autowired
	private FileService fileService;

	private String recipeImgLocation = "c:/recipe/item";

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_CHEF')")
	@GetMapping(value = "/recipeForm")
	public String recipeForm() {
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
			String mainPicRename = "";
			String mainpicurl = "";

			if (!mainPic.isEmpty()) {
				mainPicRename = fileService.uploadFile(recipeImgLocation, mainPic, mainPicture.getBytes());
				Path folderPath = Paths.get(recipeImgLocation);
				mainpicurl = "/images/item/" + mainPicRename;
				try {
					// 파일 저장
					mainPicture.transferTo(new File(folderPath + mainPicRename));
					recipe.setMainpic(mainPic);
					recipe.setMainpicrename(mainPicRename);
					recipe.setMainpicurl(mainpicurl);
					model.addAttribute("mainPic", mainPicRename);
				} catch (Exception e) {
					log.error("레시피 메인사진 중 오류 발생", e);
					return "error";
				}
			}
			recipeService.registRecipe(recipe);

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
					rIngredientOne.setIngredientorder(count++);
					rmList.add(rIngredientOne);
				}
			}
			log.info(">>>재료List는======" + rmList);
			recipeService.registIngredient(rmList);
			model.addAttribute("recipeIngredient", rmList);
			// 레시피 순서 리스트 만들어서 전달하기
			ArrayList<RecipeStep> rsList = new ArrayList<RecipeStep>();
			recipeStep.getRecipedescription();
			String arrDescription[] = recipeStep.getRecipedescription().split(",ab22bb,");
			// 더미 value까지 배열을 나누는것으로 인식해서 사용자가 ,를 입력했을때 정상적으로 table에 저장되게 한다
			arrDescription[arrDescription.length - 1] = arrDescription[arrDescription.length - 1].replace(",ab22bb",
					"");
			// 배열의 마지막은 ,가 안들어가기때문에 더미vlaue 배열값으로 인식한다, ,가 없는 더미value를 삭제 해주는 코드
			// 레시피 순서 사진 저장코드
			int countStep = 1;
			for (int i = 0; i < arrDescription.length; i++) {
				String recipePic = recipePicture.get(i).getOriginalFilename();
				String recipePicRename = "";
				String recipepicurl = "";
				if (!recipePic.isEmpty()) {
					recipePicRename = fileService.uploadFile(recipeImgLocation, recipePic, recipePicture.get(i).getBytes());
					Path folderPath = Paths.get(recipeImgLocation);
					recipepicurl = "/images/item/" + recipePicRename;

					try {
						// 파일 저장
						recipePicture.get(i).transferTo(new File(folderPath + recipePicRename));
					} catch (Exception e) {
						log.error("레시피 순서 사진 중 오류 발생", e);
						return "error";
					}
				}
				// 여기서부터 레시피 step 테이블에 저장할 값 List화 시키는 코드
				RecipeStep rStepOne = new RecipeStep();
				rStepOne.setRecipedescription(arrDescription[i]);
				rStepOne.setRecipeorder(countStep++);
				rStepOne.setRecipepic(recipePic);
				rStepOne.setRecipepicrename(recipePicRename);
				rStepOne.setRecipepicurl(recipepicurl);
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
		//        4차병합 게시글 수에 따라 유저 레벨 상승
		int count  = recipeRepository.countByUserNickname(recipe.getWriter());
		int[] levelRanges = {2, 6, 16, 51};
		Integer userlev = 1;

		for (int i = 0; i < levelRanges.length; i++) {
			if (count >= levelRanges[i]) {
				userlev = i + 2;
			} else {
				break;
			}
		}
		memberRepository.updateuserlev(userlev, recipe.getWriter());
		// memberSecurityDTO 업데이트
		MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		memberSecurityDTO.setUserlev(userlev);
		// Authentication 객체 업데이트
		Authentication newAuthentication = new UsernamePasswordAuthenticationToken(memberSecurityDTO, memberSecurityDTO.getPassword(), memberSecurityDTO.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
		//memberlog 업데이트
		memberLogRepository.updateMemberLoginLoglev(userlev,recipe.getWriter());

		return "redirect:/";
	}

	@GetMapping(value = "/recipeList")
	public String recipeList(HttpSession session, Model model,
							 @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {
		String rctype = (String) session.getAttribute("rctype");
		String rcsituation = (String) session.getAttribute("rcsituation");
		String rcingredient = (String) session.getAttribute("rcingredient");
		String rcmeans = (String) session.getAttribute("rcmeans");

		int count = 0;

		List<String> recommendedKeywords = (List<String>) session.getAttribute("recommendedKeywords");
		List<Recipe> recipeList = recipeRepository.findRecipesByFilterSearched(recommendedKeywords, rctype, rcsituation, rcingredient, rcmeans);

		List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
		for (Recipe recipe : recipeList) {
			ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getRecipeno(), recipe.getTitle(), recipe.getMainpicrename(),
					recipe.getTag(), recipe.getWriter(), recipe.getRecipestatus(), recipe.getMainpicurl());
			if (recipe.getRecipestatus().equals("Y")){
				count++;
			}
			listRecipeDtoList.add(listRecipeDto);
		}

		int limit = 10; // 한 페이지에 표시할 레시피 개수
		int totalRecord = recipeService.totalRecord(); // 게시물 총 갯수
		int totalPage = (int) Math.ceil((double)totalRecord / limit); // 페이징 번호

		int startIndex = (currentPage - 1) * limit;
		int endIndex = Math.min(startIndex + limit, listRecipeDtoList.size());

		List<ListRecipeDto> sublist = new ArrayList<>();
		for (int i = startIndex; i < endIndex; i++) {
			ListRecipeDto recipeDto = listRecipeDtoList.get(i);
			if (recipeDto.getRecipestatus().equals("Y")) {
				sublist.add(recipeDto);
			}
		}

		Pagination pagination = new Pagination(startIndex, limit, totalPage);

		int blockSize = 10;
		int blockStart = (int) Math.floor((currentPage - 1) / blockSize) * blockSize + 1;
		int blockEnd = Math.min(blockStart + blockSize - 1, totalPage);

		log.info(">>>>>totalRecord>>>>> "+totalRecord);
		log.info(">>>>>endIndex>>>>> "+endIndex);
		log.info(">>>>>currentPage>>>>> "+currentPage);
		log.info(">>>>>페이징>>>>> "+pagination);

		model.addAttribute("recipe", sublist);
		model.addAttribute("recipeCount", count);
		model.addAttribute("pagination", recipeService.printRecipeList(pagination));
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("blockStart", blockStart);
		model.addAttribute("blockEnd", blockEnd);

		return "recipe/recipeList";
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

			List<Recipe> recipeList = recipeRepository.findRecipesByFilterSearched(recommendedKeywords, null, null, null, null); // DB에서 레시피 목록 조회

			List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
			for (Recipe recipe : recipeList) {
				ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getRecipeno(), recipe.getTitle(), recipe.getMainpicrename(),
						recipe.getTag(), recipe.getWriter(), recipe.getRecipestatus(), recipe.getMainpicurl());
				listRecipeDtoList.add(listRecipeDto);
			}
			redirectAttributes.addFlashAttribute("recipe", listRecipeDtoList);
		} catch (RecipeService.RecipeIdExistException e) {
			redirectAttributes.addFlashAttribute("error", "id");
		}
		return "redirect:/recipe/recipeList";
	}

	@PostMapping("/sendData")
	public String handleFormData(@RequestParam(value = "rctype", required = false) String rctype,
								 @RequestParam(value = "rcsituation", required = false) String rcsituation,
								 @RequestParam(value = "rcingredient", required = false) String rcingredient,
								 @RequestParam(value = "rcmeans", required = false) String rcmeans, HttpSession session, RedirectAttributes redirectAttributes) {

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
		List<Recipe> recipeList = recipeRepository.findRecipesByFilterSearched(recommendedKeywords, rctype, rcsituation, rcingredient, rcmeans); // DB에서 레시피 목록 조회
		List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
		for (Recipe recipe : recipeList) {
			ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getRecipeno(), recipe.getTitle(), recipe.getMainpicrename(), recipe.getTag(), recipe.getWriter(), recipe.getRecipestatus(), recipe.getMainpicurl());
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

	@GetMapping(value = "/recipeDetail/{recipeno}")
	public String viewRecipeStep(@PathVariable(value = "recipeno") int recipeno, Model model, @RequestParam(value = "currentPage",required = false, defaultValue = "1") int currentPage) {

		try {
			Recipe recipe = recipeService.printOneRecipe(recipeno);
			List<RecipeIngredient> rmList = recipeService.printOneRecipeIngredient(recipeno);
			List<RecipeStep> rsList = recipeService.printOneRecipeStep(recipeno);


			model.addAttribute("recipe", recipe);
			log.info(">>>recipe는>>>"+recipe);
			model.addAttribute("rmList", rmList);
			model.addAttribute("rsList", rsList);
			model.addAttribute("currentPage",currentPage);
		} catch (Exception e) {
			log.error("레시피 상세 로드 중 오류 발생", e);
			return "error";
		}
		return "recipe/recipeDetail";
	}

	@GetMapping(value = "/recipeModify/{recipeno}")
	public String recipeModify(@PathVariable("recipeno") int recipeno, HttpSession session, Model model) {

		try {
			Recipe recipe = recipeService.printOneRecipe(recipeno);
			List<RecipeIngredient> rmList = recipeService.printOneRecipeIngredient(recipeno);
			List<RecipeStep> rsList = recipeService.printOneRecipeStep(recipeno);

			Member writer = (Member)session.getAttribute("writer");

			model.addAttribute("recipe", recipe);
			model.addAttribute("rmList", rmList);
			model.addAttribute("rmListSize", rmList.size());
			model.addAttribute("rsList", rsList);

			return "recipe/recipeModify";

		} catch (Exception e) {
			log.error("레시피 수정 중 오류 발생", e);
			return "error";
		}
	}

	@PostMapping(value = "/recipeModify")
	public String modifyRecipe(@ModelAttribute Recipe recipe,
							   @ModelAttribute RecipeStep recipeStep,
							   @ModelAttribute RecipeIngredient rIngredient,
							   @RequestParam(value = "mainPicture") MultipartFile mainPicture,
							   @RequestParam(value = "recipePicture", required = false) List<MultipartFile> recipePicture,
							   @RequestParam(value = "recipeno") Integer recipeno,
							   Model model) {
		try {
			// 대표사진 교체
			String mainPic = mainPicture.getOriginalFilename();

			if (mainPicture != null && !mainPic.isEmpty()) {
				// 기존 파일 삭제
				Path folderPath = Paths.get(recipeImgLocation);
				File existingFile = new File(folderPath + recipe.getMainpicrename());
				if (existingFile.exists()) {
					existingFile.delete();
				}

				// 새로운 파일 저장
				String mainPicRename = fileService.uploadFile(recipeImgLocation, mainPic, mainPicture.getBytes());;
				String mainpicurl = "/images/item/" + mainPicRename;
				try {
					mainPicture.transferTo(new File(folderPath + mainPicRename));
					recipe.setMainpic(mainPic);
					recipe.setMainpicrename(mainPicRename);
					recipe.setRecipeno(recipeno);
					recipe.setMainpicurl(mainpicurl);
					model.addAttribute("mainPic", mainPicRename);
				} catch (Exception e) {
					log.error("레시피 메인사진 교체 중 오류 발생", e);
					return "error";
				}
			}
			log.info(">>>교체mainPic는======" + mainPic);

			recipeService.modifyOneRecipe(recipe); // 레시피 테이블 저장

			// 레시피 재료 리스트 만들어서 전달하기
			ArrayList<RecipeIngredient> rmList = new ArrayList<RecipeIngredient>();
			String amount[] = rIngredient.getAmount().split(",");
			String ingredient[] = rIngredient.getIngredient().split(",");
			for (int i = 0; i < ingredient.length; i++) {
				if (!amount[i].equals("") && !ingredient[i].equals("")) {
					RecipeIngredient rIngredientOne = new RecipeIngredient();
					rIngredientOne.setAmount(amount[i]);
					rIngredientOne.setIngredient(ingredient[i]);
					rIngredientOne.setIngredientorder(i+1);
					rIngredientOne.setRecipeno(recipeno);
					rmList.add(rIngredientOne);
				}
			}
			log.info(">>>교체재료List는======" + rmList);
			recipeService.modifyOneRecipeIngredient(rmList);
			model.addAttribute("recipeIngredient", rmList);

			// 레시피 순서 리스트 만들어서 전달하기
			ArrayList<RecipeStep> rsList = new ArrayList<RecipeStep>();
			String arrDescription[] = recipeStep.getRecipedescription().split(",ab22bb,");
			// 더미 value까지 배열을 나누는것으로 인식해서 사용자가 ,를 입력했을때 정상적으로 table에 저장되게 한다
			arrDescription[arrDescription.length - 1] = arrDescription[arrDescription.length - 1].replace(",ab22bb","");
			// 배열의 마지막은 ,가 안들어가기때문에 더미vlaue 배열값으로 인식한다, ,가 없는 더미value를 삭제 해주는 코드

			for (int i = 0; i < arrDescription.length; i++) {

				String recipePic = recipePicture.get(i).getOriginalFilename();

				if (recipePicture.get(i) != null && !recipePic.isEmpty()) {
					String recipePicRename = fileService.uploadFile(recipeImgLocation, recipePic, recipePicture.get(i).getBytes());

					Path folderPath = Paths.get(recipeImgLocation);
					File existingFile = new File(folderPath + recipe.getMainpicrename());
					if (existingFile.exists()) {
						existingFile.delete();
					}
					String recipepicurl = "/images/item/" + recipePicRename;

					try {
						// 파일 저장
						recipePicture.get(i).transferTo(new File(folderPath + recipePicRename));
					} catch (Exception e) {
						log.error("레시피 순서 사진 중 오류 발생", e);
						return "error";
					}

					// 여기서부터 레시피 step 테이블에 저장할 값 List화 시키는 코드
					RecipeStep rStepOne = new RecipeStep();
					if (i < arrDescription.length) {
						if (!arrDescription[i].equals("")) {
							rStepOne.setRecipedescription(arrDescription[i]);
						}
					}
					rStepOne.setRecipeno(recipeno);
					rStepOne.setRecipepic(recipePic);
					rStepOne.setRecipepicrename(recipePicRename);
					rStepOne.setRecipepicurl(recipepicurl);
					rStepOne.setRecipeorder(i+1);
					rStepOne.setRecipedescription(arrDescription[i]);
					model.addAttribute("recipePic", recipePicRename);

					rsList.add(rStepOne);
					log.info(">>>교체rStepOne은======" + rStepOne);
				}
			}
			log.info(">>>교체순서List는======" + rsList);
			recipeService.modifyOneRecipeStep(rsList); // 레시피 순서 수정
			model.addAttribute("recipeStep", rsList);


		} catch (Exception e) {
			log.error("레시피 교체 저장 중 오류 발생", e);
			return "error";
		}
		return "redirect:/recipe/recipeDetail/" + recipeno;
	}

	@GetMapping(value = "/recipeDelete/{recipeno}")
	public String recipeDelete(@PathVariable("recipeno") int recipeno, Model model) {
		try {
			Recipe recipe = recipeService.printOneRecipe(recipeno);
			List<RecipeIngredient> rmList = recipeService.printOneRecipeIngredient(recipeno);
			List<RecipeStep> rsList = recipeService.printOneRecipeStep(recipeno);

			model.addAttribute("recipe", recipe);
			model.addAttribute("rmList", rmList);
			model.addAttribute("rsList", rsList);
		} catch (Exception e) {
			log.error("레시피 삭제 중 오류 발생", e);
			return "error";
		}
		return "recipe/recipeDelete";
	}

	@PostMapping(value = "/recipeDelete")
	public String recipeDelete(@RequestParam(value = "recipeno") int recipeno) {
		try {
			// 레시피 삭제 처리
			recipeService.removeOneRecipe(recipeno);

		} catch (Exception e) {
			log.error("레시피 삭제 중 오류 발생", e);
			return "error";
		}
		return "redirect:/recipe/recipeList";
	}

}