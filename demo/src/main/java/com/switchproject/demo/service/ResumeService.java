package com.switchproject.demo.service;

import com.switchproject.demo.dto.CreateResumeRequest;
import com.switchproject.demo.dto.ResumeResponse;
import com.switchproject.demo.model.Resume;
import com.switchproject.demo.model.User;
import com.switchproject.demo.repository.ResumeRepository;
import com.switchproject.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeService(
            ResumeRepository resumeRepository,
            UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    public Resume createResume(
            CreateResumeRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Resume resume = new Resume();

        resume.setTitle(request.getTitle());
        resume.setUser(user);

        return resumeRepository.save(resume);
    }

    public List<ResumeResponse> getMyResumes(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return resumeRepository.findByUserId(user.getId())
                .stream()
                .map(resume -> new ResumeResponse(
                        resume.getId(),
                        resume.getTitle()
                ))
                .toList();
    }
}