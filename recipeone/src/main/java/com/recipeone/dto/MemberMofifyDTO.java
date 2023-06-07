package com.recipeone.dto;

import lombok.Data;

@Data
public class MemberMofifyDTO {

    private String mid;
    private String password;
    private String oldinputpassword;
    private String newpassword;
    private String confirmedPassword;
    private String usernickname;
    private String userfullname;
    private String userphone;
    private String useraddr;
    private String useremail;
    private String useryear;
    private String usergender;
}
