package com.recipeone.service;

import com.recipeone.dto.MemberJoinDTO;
import com.recipeone.dto.MemberMofifyDTO;

public interface MemberService {

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
