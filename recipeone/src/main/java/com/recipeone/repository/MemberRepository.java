package com.recipeone.repository;

import com.recipeone.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(String mid);

    @Query("select m from Member m where m.mid = :mid")
    Optional<Member> memberset(String mid);

    Optional<Member> findById(String mid);

    @Query("select m from Member m where m.usernickname = :usernickname")
    Optional<Member> findByUserNickName(String usernickname);

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByUseremail(String useremail);

    @Query("SELECT password FROM Member WHERE mid = :mid")
    String findPasswordByMid(@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.password = :password, m.social = false where m.mid = :mid")
    void updatePassword(@Param("password")String password,@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.password = :newpassword where m.mid = :mid")
    void updatenewPassword(@Param("newpassword")String newpassword,@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.usernickname = :usernickname where m.mid = :mid")
    void updateusernickname(@Param("usernickname")String usernickname,@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.useremail = :useremail where m.mid = :mid")
    void updateuseremail(@Param("useremail")String useremail,@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.userfullname = :userfullname where m.mid = :mid")
    void updateuserfullname(@Param("userfullname")String userfullname,@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.useraddr = :useraddr where m.mid = :mid")
    void updateuseraddr(@Param("useraddr")String useraddr,@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.userphone = :userphone where m.mid = :mid")
    void updateuserphone(@Param("userphone")String userphone,@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.moddate = :moddate where m.mid = :mid")
    void updatemoddate(@Param("moddate") LocalDateTime moddate, @Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.userlev = :userlev where m.usernickname = :usernickname")
    void updateuserlev(@Param("userlev") Integer userlev, @Param("usernickname") String usernickname);

    @Modifying
    @Transactional
    @Query("update Member m set m.useryear = :useryear where m.mid = :mid")
    void updateuseryear(@Param("useryear") String useryear, @Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.usergender = :usergender where m.mid = :mid")
    void updateusergender(@Param("usergender") String usergender, @Param("mid") String mid);

    @Query(value ="SELECT * FROM Member m", nativeQuery = true)
    List<Member> findMembers();

}
