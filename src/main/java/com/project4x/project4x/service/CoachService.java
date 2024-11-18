package com.project4x.project4x.service;


import com.project4x.project4x.repository.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CoachService {

    @Autowired
    private CoachRepository coachRepository;

}
