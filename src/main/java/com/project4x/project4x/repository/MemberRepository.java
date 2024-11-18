package com.project4x.project4x.repository;

import com.project4x.project4x.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserName(String userName);

    @Query("SELECT m FROM Member m WHERE m.userName = :userName")
    Member findByUserNameForGetDetails(@Param("userName") String userName);

    Member findByuserName(String userName);
}
