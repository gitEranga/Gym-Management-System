package com.project4x.project4x.service;

import com.project4x.project4x.entity.Member;
import com.project4x.project4x.entity.Reservation;
import com.project4x.project4x.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member saveMember(Member member) {
        // Validate that the password and confirmPassword match
        if (!member.getPassword().equals(member.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        return memberRepository.save(member);
    }

    public Optional<Member> findByUserNameAndPassword(String userName, String password) {
        return memberRepository.findByUserName(userName)
                .filter(member -> member.getPassword().equals(password));
    }

    public Optional<Member> findByUserName(String userName) {
        return memberRepository.findByUserName(userName);
    }
}
