package com.recipeone.controller;

import com.recipeone.dto.ListRecipeDto;
import com.recipeone.dto.MemberJoinDTO;
import com.recipeone.dto.MemberMofifyDTO;
import com.recipeone.entity.Member;
import com.recipeone.entity.MemberLoginlog;
import com.recipeone.entity.Recipe;
import com.recipeone.repository.MemberLogRepository;
import com.recipeone.entity.Recipe;
import com.recipeone.repository.MemberRepository;
import com.recipeone.repository.RecipeRepository;
import com.recipeone.security.dto.MemberSecurityDTO;
import com.recipeone.service.MemberService;
import com.recipeone.service.MemberServiceImpl;
import com.recipeone.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RecipeRepository recipeRepository;

    private final RecipeService recipeService;

    private final MemberLogRepository memberLogRepository;


    @GetMapping("/login") //그냥 /login으로 요청왔을 때
    public void loginGET(String error, String logout) {
        log.info("login get...........");
        log.info("log out:" + logout);
        if (logout != null) {
            log.info("user logout.........");
        }
    }

    @PostMapping("/login")
    public String loginPost() {
        log.info("login 성공...........");
        return "redirect:/main2";
    }

    @GetMapping("/socialmodify")
    public void modifyGET() {
        log.info("GET modify...........");
    }

    @PostMapping("/socialmodify") //소셜로그인 닉네임,비번바꾸기
    public String socialmodifyPost(MemberMofifyDTO memberMofifyDTO, RedirectAttributes redirectAttributes) {
        try {
            memberService.socialmodify(memberMofifyDTO);
        } catch (MemberServiceImpl.MidExistException e) {
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/socialmodify";
        } catch (MemberService.UserEmailExistException e) {
            redirectAttributes.addFlashAttribute("error", "useremail");
            return "redirect:/member/socialmodify";
        } catch (MemberService.UserNickNameExistException e) {
            redirectAttributes.addFlashAttribute("error", "usernickname");
            return "redirect:/member/socialmodify";
        } catch (MemberService.ConfirmedPasswordException e) {
            redirectAttributes.addFlashAttribute("error", "confirmedPassword");
            return "redirect:/member/socialmodify";
        }
        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/";
    }

    @GetMapping("/membermodify")
    public void membermodifyGET() {
        log.info("GET membermodify...........");
    }

    @PostMapping("/membermodify") //회원 기본 정보 수정
    public String membermodifyPost(MemberMofifyDTO memberMofifyDTO, RedirectAttributes redirectAttributes) {
        try {
            memberService.membermodify(memberMofifyDTO);
        } catch (MemberService.UserEmailExistException e) {
            redirectAttributes.addFlashAttribute("error", "useremail");
            return "redirect:/member/membermodify";
        } catch (MemberService.UserNickNameExistException e) {
            redirectAttributes.addFlashAttribute("error", "usernickname");
            return "redirect:/member/membermodify";
        }
        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/member/mypage";
    }

    @GetMapping("/passwordmodify")
    public void passwordmodifyGET() {
        log.info("GET passwordmodify...........");
    }

    @PostMapping("/passwordmodify") //회원 비밀번호 수정
    public String passwordmodifyPost(MemberMofifyDTO memberMofifyDTO, RedirectAttributes redirectAttributes) {
        log.info(memberMofifyDTO);
        try {
            memberService.passwordmodify(memberMofifyDTO);
        } catch (MemberService.WrongPasswordException e) {
            redirectAttributes.addFlashAttribute("error", "WrongPassword");
            return "redirect:/member/passwordmodify";
        } catch (MemberService.ConfirmedmodifyPasswordException e) {
            redirectAttributes.addFlashAttribute("error", "ConfirmedmodifyPassword");
            return "redirect:/member/passwordmodify";
        }
        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/member/mypage";
    }

    @GetMapping("/join")
    public void joinGET() {
        log.info("join get...");
    }

    @PostMapping("/join") // 회원가입 폼 작성해서 내용 보낼 때
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes) {
        log.info(memberJoinDTO);
        try {
            memberService.join(memberJoinDTO); //MemberService의 join()메소드를 호출해서 회원가입 처리
        } catch (
                MemberServiceImpl.MidExistException e) { //MemberService에서 MidExistException이 있을 경우(id가 중복되는 경우) error라는 속성에 mid를 담아서 다시 /member/join경로로 redirect한다.
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        } catch (
                MemberServiceImpl.UserNickNameExistException e) { //MemberService에서 MidExistException이 있을 경우(id가 중복되는 경우) error라는 속성에 mid를 담아서 다시 /member/join경로로 redirect한다.
            redirectAttributes.addFlashAttribute("error", "usernickname");
            return "redirect:/member/join";
        } catch (MemberService.UserEmailExistException e) {
            redirectAttributes.addFlashAttribute("error", "useremail");
            return "redirect:/member/join";
        } catch (MemberService.ConfirmedPasswordException e) {
            redirectAttributes.addFlashAttribute("error", "confirmedPassword");
            return "redirect:/member/join";
        }
        redirectAttributes.addFlashAttribute("result", "success");
        //정상적으로 회원가입이 되었을 경우 result라는 속성에 success라는 값을 담아서 /member/login 경로로 redirect한다.
        //Flash Attribute를 이용해서 한번의 요청에만 해당 속성값이 전달된다.
        return "redirect:/member/login";
    }

    @GetMapping("/mypage") //회원 마이페이지
    public void mypageGET() {
        log.info("mypage get...");
    }
   
    @PostMapping("/check-username")  // 아이디 중복 확인
    @ResponseBody
    public Map<String, String> checkUsername(@RequestParam String username) {
        Map<String, String> result = new HashMap<>();
        boolean isDuplicated = memberService.checkDuplicatedUsername(username);
        if (isDuplicated) {
            result.put("result", "fail");
        } else {
            result.put("result", "success");
        }
        return result;
    }

    @PostMapping("/check-usernickname") // 사용자 활동명 중복 확인
    @ResponseBody
    public Map<String, String> checkUsernickname(@RequestParam String usernickname) {
        Map<String, String> result = new HashMap<>();
        boolean isDuplicated = memberService.checkDuplicatedUsernickname(usernickname);
        if (isDuplicated) {
            result.put("result", "fail");
        } else {
            result.put("result", "success");
        }
        return result;
    }

    // 4차 병합 부분
    @PostMapping(value = "/myregist")
    public String myregistPost() {
        return "member/myregist";
    }

   // 4차 병합 부분
    @GetMapping(value = "/myregist" )
    public String myregistGet(@RequestParam("usernickname") String usernickname, Model model) {
        List<Recipe> recipeList = recipeRepository.findRecipeByUserNickname(usernickname);
        List<ListRecipeDto> listRecipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipeList) {
            ListRecipeDto listRecipeDto = new ListRecipeDto(recipe.getRecipeno(), recipe.getTitle(), recipe.getMainpicrename(), recipe.getTag(), recipe.getWriter(),recipe.getRecipestatus());
            listRecipeDtoList.add(listRecipeDto);
        }
        model.addAttribute("recipe", listRecipeDtoList);
        return "member/myregist";
    }

    //일단 기록만 나오는건 구현 완료
    @GetMapping("/admin")
    public String getMembers(Model model) {
        List<Member> members = memberRepository.findMembers();
        model.addAttribute("members", members);
        List<MemberLoginlog> memberLoginLogs = memberLogRepository.findAllMemberLoginLogs();
        model.addAttribute("memberLoginLogs", memberLoginLogs);


        return "member/admin";
    }






}
