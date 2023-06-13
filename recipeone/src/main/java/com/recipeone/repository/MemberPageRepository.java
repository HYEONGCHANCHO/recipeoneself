package com.recipeone.repository;

import com.recipeone.entity.Memberpagelog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberPageRepository extends JpaRepository<Memberpagelog,Long> {

}
