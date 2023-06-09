package com.recipeone.service;

//import com.recipeone.dto.LoginCountDTO;
import com.recipeone.dto.MemberJoinDTO;
import com.recipeone.dto.MemberMofifyDTO;
import com.recipeone.entity.Member;
import com.recipeone.entity.Recipe;
import com.recipeone.security.dto.MemberSecurityDTO;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MemberService {
//    void memberlog(LoginCountDTO loginCountDTO);

    static class MidExistException extends Exception{
    }
    static class UserNickNameExistException extends Exception{
    }
    static class UserEmailExistException extends Exception{
    }
    static class ConfirmedPasswordException extends Exception{
    }
    static class ConfirmedmodifyPasswordException extends Exception{
    }
    static class WrongPasswordException extends Exception{
    }

    void join(MemberJoinDTO memberJoinDTO) throws  MidExistException,UserNickNameExistException, UserEmailExistException,ConfirmedPasswordException ;
    void socialmodify(MemberMofifyDTO memberMofifyDTO) throws  MidExistException, UserNickNameExistException,UserEmailExistException,ConfirmedPasswordException ;
    void membermodify(MemberMofifyDTO memberMofifyDTO) throws  UserNickNameExistException,UserEmailExistException ;
    void passwordmodify(MemberMofifyDTO memberMofifyDTO) throws  ConfirmedmodifyPasswordException,WrongPasswordException ;
    public boolean checkDuplicatedUsername(String username);
    public boolean checkDuplicatedUsernickname(String usernickname) ;

}
