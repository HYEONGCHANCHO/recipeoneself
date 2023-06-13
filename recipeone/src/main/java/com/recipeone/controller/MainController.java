package com.recipeone.controller;

import com.recipeone.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping(value = "/")
    public String main(){
        return "main";
    }

}
