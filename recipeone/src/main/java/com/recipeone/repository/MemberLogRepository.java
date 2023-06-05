package com.recipeone.repository;

import com.recipeone.entity.Member;
import com.recipeone.entity.MemberLoginlog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberLogRepository extends JpaRepository<MemberLoginlog,Long> {


  Optional<MemberLoginlog> findByMid(String mid);
  List<MemberLoginlog> findAll();

}
