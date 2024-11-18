package com.project4x.project4x.repository;

import com.project4x.project4x.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {

   Admin findByUserName(String userName);


}
