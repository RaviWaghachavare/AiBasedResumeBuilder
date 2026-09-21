package com.switchproject.demo.dto;

public class ResumeResponse {

    private Long id;
    private String title;

    public ResumeResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}