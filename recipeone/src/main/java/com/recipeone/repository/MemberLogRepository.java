package com.recipeone.repository;

import com.recipeone.entity.Member;
import com.recipeone.entity.MemberLoginlog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberLogRepository extends JpaRepository<MemberLoginlog,Long> {


  Optional<MemberLoginlog> findByMid(String mid);
  List<MemberLoginlog> findAll();

  @Modifying
  @Transactional
  @Query(value = "update MemberLoginLog l set l.useryear = :useryear, l.usergender = :usergender where l.mid = :mid",nativeQuery = true)
  void updateMemberLoginLogData(@Param("useryear") String useryear, @Param("usergender") String usergender, @Param("mid") String mid);


  @Modifying
  @Transactional
  @Query(value = "update MemberLoginLog l set l.userlev = :userlev where l.mid = :mid",nativeQuery = true)
  void updateMemberLoginLoglev(@Param("userlev") Integer userlev, @Param("mid") String mid);

  @Query("SELECT m FROM MemberLoginlog m")
  List<MemberLoginlog> findAllMemberLoginLogs();


//  @Query(value = "SELECT m.mid, COUNT(DISTINCT m.mid) AS loginCount, m.usergender, m.useryear, m.userlev " +
//          "FROM MemberLoginlog m " +
//          "WHERE m.loginlog BETWEEN :startDate AND :endDate " +
//          "GROUP BY m.mid, m.usergender, m.useryear, m.userlev " +
//          "ORDER BY loginCount DESC", nativeQuery = true)
//  List<Object[]> findLoginCountsByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
