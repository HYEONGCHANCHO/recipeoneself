package com.recipeone.security;


import com.recipeone.entity.Member;
import com.recipeone.entity.MemberRole;
import com.recipeone.repository.MemberRepository;
import com.recipeone.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        OAuth2User oAuth2User =super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String useremail = null;

        switch (clientName){
            case "kakao":
                useremail = getKakaoEmail(paramMap);
                break;
            case "Google":
                useremail = getGoogleEmail(paramMap);
                break;
            case "Naver":
                useremail = getNaverEmail(paramMap);
                break;
        }
        return generateDTO(useremail,paramMap);
    }
    private MemberSecurityDTO generateDTO(String useremail, Map<String,Object> params){
        Optional<Member> result = memberRepository.findByUseremail(useremail);

        //데이터베이스에 해당 이메일을 가진 사용자가 없을때
        if(result.isEmpty()){//회원 추가 -- mid는 이메일 주소/ 패스워드는 1111
            Member member = Member.builder()
                    .mid(useremail)
                    .password(passwordEncoder.encode("1111"))
                    .useremail(useremail)
                    .usernickname(useremail)
                    .social(true)
                    .userlev(1)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            //MemberSecurityDTO 구성 및 반환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(useremail,"1111",useremail,useremail,true, "","","","","",1,Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;
        }else {
            //데이터베이스에 해당 이메일을 가진 사용자가 있을때
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(
                            member.getMid(),
                            member.getPassword(),
                            member.getUseremail(),
                            member.getUsernickname(),
                            member.isSocial(),
                            member.getUserfullname(),
                            member.getUserphone(),
                            member.getUseraddr(),
                            member.getUseryear(),
                            member.getUsergender(),
                            member.getUserlev(),
                            member.getRoleSet()
                                    .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name())).collect(Collectors.toList())
                    ) ;
            return memberSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String ,Object>paramMap){
        Object value = paramMap.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) value;
        String useremail = (String) accountMap.get("email");
        return useremail;
    }
    private String getGoogleEmail(Map<String, Object> paramMap) {
        String useremail = (String) paramMap.get("email");
        return useremail;
    }

    private String getNaverEmail(Map<String, Object> paramMap) {
        Object value = paramMap.get("response");
        LinkedHashMap responseMap = (LinkedHashMap) value;
        String useremail = (String) responseMap.get("email");
        return useremail;
    }
}
