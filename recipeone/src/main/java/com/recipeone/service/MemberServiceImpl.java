package com.recipeone.service;

import com.recipeone.dto.MemberJoinDTO;
import com.recipeone.dto.MemberMofifyDTO;
import com.recipeone.entity.Member;
import com.recipeone.entity.MemberRole;
import com.recipeone.repository.MemberRepository;
import com.recipeone.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException, UserNickNameExistException, UserEmailExistException, ConfirmedPasswordException { //문제없는 코드
        String mid = memberJoinDTO.getMid();
        String usernickname = memberJoinDTO.getUsernickname();
        String useremail = memberJoinDTO.getUseremail();
        if (memberRepository.findById(mid).isPresent()) {
            throw new MidExistException();
        } else if (memberRepository.findByUserNickName(usernickname).isPresent()) {
            throw new UserNickNameExistException();
        } else if (memberRepository.findByUseremail(useremail).isPresent()) {
            throw new UserEmailExistException();
        } else if (!memberJoinDTO.getPassword().equals(memberJoinDTO.getConfirmedPassword())) {
            throw new ConfirmedPasswordException();
        }
        Member member = modelMapper.map(memberJoinDTO, Member.class);

        member.changePassword(passwordEncoder.encode(memberJoinDTO.getPassword()));
        member.addRole(MemberRole.USER);
        member.addlev(1);
        member.loginFailCount(0);
        memberRepository.save(member);
        log.info(member.getRoleSet());
    }

    @Override //수정이 필요함 닉네임 수정 안됨 비밀번호는 될듯?
    public void socialmodify(MemberMofifyDTO memberMofifyDTO) throws MidExistException, UserNickNameExistException, UserEmailExistException, ConfirmedPasswordException { //문제없는 코드
        Optional<Member> result = memberRepository.memberset(memberMofifyDTO.getMid());
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("username not found...");
        }
        Member member = result.get();

        String mid = member.getMid(); //소셜 로그인으로 추가된 사용자로 현재 DB에 존재하는 이메일
        String password = memberMofifyDTO.getPassword();
        String usernickname = memberMofifyDTO.getUsernickname();
        String confirmedPassword = memberMofifyDTO.getConfirmedPassword();
        String oldusernickname = member.getUsernickname();
        if (!password.equals(confirmedPassword)) {throw new ConfirmedPasswordException();}
        if (!oldusernickname.equals(usernickname) && memberRepository.findByUserNickName(usernickname).isPresent()) { throw new UserNickNameExistException();}
        if (!oldusernickname.equals(usernickname) && memberRepository.findByUserNickName(usernickname).isEmpty()) { memberRepository.updateusernickname(usernickname, mid);}
        password = passwordEncoder.encode(password);
        memberRepository.updatePassword(password, mid);

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberSecurityDTO.setUsernickname(usernickname);
    }

    @Override
    public void membermodify(MemberMofifyDTO memberMofifyDTO) throws UserNickNameExistException, UserEmailExistException {
        Optional<Member> result = memberRepository.memberset(memberMofifyDTO.getMid());
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("username not found...");
        }
        Member member = result.get();
        String mid = member.getMid();
        String olduseremail = member.getUseremail();
        String usernickname = memberMofifyDTO.getUsernickname();
        String oldusernickname = member.getUsernickname(); // 기존 닉네임 정보 받아오기
        String useremail = memberMofifyDTO.getUseremail();
        String useraddr = memberMofifyDTO.getUseraddr();
        String userfullname = memberMofifyDTO.getUserfullname();
        String userphone = memberMofifyDTO.getUserphone();
        LocalDateTime now = LocalDateTime.now();

        if (!olduseremail.equals(useremail)&& memberRepository.findByUseremail(useremail).isPresent()) { throw new UserEmailExistException(); }
        if (!olduseremail.equals(useremail)&& memberRepository.findByUseremail(useremail).isEmpty()) { memberRepository.updateuseremail(useremail, mid);}
        if (!oldusernickname.equals(usernickname) && memberRepository.findByUserNickName(usernickname).isPresent()) { throw new UserNickNameExistException();}
        if (!oldusernickname.equals(usernickname) && memberRepository.findByUserNickName(usernickname).isEmpty()) { memberRepository.updateusernickname(usernickname, mid);}
        memberRepository.updateuseraddr(useraddr, mid);
        memberRepository.updateuserfullname(userfullname, mid);
        memberRepository.updateuserphone(userphone, mid);
        memberRepository.updatemoddate(now, mid);

        // memberSecurityDTO 업데이트
        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberSecurityDTO.setUsernickname(usernickname);
        memberSecurityDTO.setUseremail(useremail);
        memberSecurityDTO.setUseraddr(useraddr);
        memberSecurityDTO.setUserfullname(userfullname);
        memberSecurityDTO.setUserphone(userphone);
        // Authentication 객체 업데이트
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(memberSecurityDTO, memberSecurityDTO.getPassword(), memberSecurityDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    @Override
    public void passwordmodify(MemberMofifyDTO memberMofifyDTO) throws ConfirmedmodifyPasswordException, WrongPasswordException { //문제없는 코드
        String mid = memberMofifyDTO.getMid();
        String oldpassword = memberRepository.findPasswordByMid(mid);
        String oldinputpassword = memberMofifyDTO.getOldinputpassword();
        String newpassword = memberMofifyDTO.getNewpassword();
        String confirmedPassword = memberMofifyDTO.getConfirmedPassword();
        LocalDateTime moddate = LocalDateTime.now();
        if (!passwordEncoder.matches(oldinputpassword, oldpassword)) { //입력한 값이 암호화된 값과 일치하는지 확인
            throw new WrongPasswordException();
        } else if (!newpassword.equals(confirmedPassword)) {
            throw new ConfirmedmodifyPasswordException();
        }
        newpassword = passwordEncoder.encode(newpassword);
        memberRepository.updatenewPassword(newpassword, mid);
        memberRepository.updatemoddate(moddate, mid);
    }

    @Override // 아이디 중복 확인
    public boolean checkDuplicatedUsername(String username) {
        return memberRepository.findById(username).isPresent();
    }

    @Override // 사용자 활동명 중복 확인
    public boolean checkDuplicatedUsernickname(String usernickname) {
        return memberRepository.findByUserNickName(usernickname).isPresent();
    }
}
