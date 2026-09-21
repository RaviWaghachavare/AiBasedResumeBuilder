package com.switchproject.demo.controller;

import com.switchproject.demo.dto.CreateResumeRequest;
import com.switchproject.demo.dto.ResumeResponse;
import com.switchproject.demo.model.Resume;
import com.switchproject.demo.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    public ResponseEntity<List<ResumeResponse>> getMyResumes(
            Authentication authentication) {

        return ResponseEntity.ok(
                resumeService.getMyResumes(authentication)
        );
    }
    @PostMapping
    public ResponseEntity<Resume> createResume(
            @Valid @RequestBody CreateResumeRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                resumeService.createResume(request, authentication)
        );
    }
}