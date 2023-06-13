package com.recipeone.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    @Id
    private String mid;
    private String password;
    private String useremail;
    private String usernickname;
    private int loginFailCount;
    private String userfullname;
    private String userphone;
    private String usergender;
    private String useryear;
    private String useraddr;
    private boolean social;
    private Integer userlev;
    private LocalDateTime loginlog;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>(); //enum MemberRole에 있는 상수값이 roleSet이라는 변수에 저장

    public void changePassword(String password){
        this.password = password;
    }
    public void changeEmail(String useremail){
        this.useremail = useremail;
    }
    public void addlev(Integer userlev){
        this.userlev = userlev;
    }
    public void loginFailCount(int loginFailCount){
        this.loginFailCount = loginFailCount;
    }
    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }
    public void changeSocial(boolean social){
        this.social = social;
    }
    public void increaseLoginFailCount() {
        this.loginFailCount++;
    }

}
