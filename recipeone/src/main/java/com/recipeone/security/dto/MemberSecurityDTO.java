package com.recipeone.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {
    private String mid;
    private String password;
    private String useremail;
    private String usernickname;
    private boolean social;
    private String userfullname;
    private String userphone;
    private String useraddr;
    private String useryear;
    private String usergender;
    private Integer userlev;

    private Map<String , Object> props; //소셜 로그인 정보

    public MemberSecurityDTO(String username, String password, String useremail,  String usernickname, boolean social,String userfullname,
                             String userphone,String useraddr,String useryear,String usergender, Integer userlev,Collection<? extends GrantedAuthority> authorities){
        super(username,password,authorities);
        this.mid=username;
        this.password=password;
        this.useremail=useremail;
        this.usernickname=usernickname;
        this.social=social;
        this.userfullname=userfullname;
        this.userphone=userphone;
        this.useraddr=useraddr;
        this.useryear=useryear;
        this.usergender=usergender;
        this.userlev=userlev;
    }

    @Override
    public Map<String,Object> getAttributes(){
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.mid;
    }

}
