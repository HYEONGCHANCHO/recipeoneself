package com.recipeone.security;

import com.recipeone.entity.Member;
import com.recipeone.repository.MemberRepository;
import com.recipeone.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor

public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> result = memberRepository.getWithRoles(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("username not found...");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO =
                new MemberSecurityDTO(
                        member.getMid(),
                        member.getPassword(),
                        member.getUseremail(),
                        member.getUsernickname(),
                        false,
                        member.getUserfullname(),
                        member.getUserphone(),
                        member.getUseraddr(),
                        member.getUseryear(),
                        member.getUsergender(),
                        member.getUserlev(),
                        member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toList())
                );

        return memberSecurityDTO;
    }
}
