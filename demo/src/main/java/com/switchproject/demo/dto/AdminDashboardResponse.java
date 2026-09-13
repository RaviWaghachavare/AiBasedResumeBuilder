package com.switchproject.demo.dto;

public class AdminDashboardResponse {

    private long totalUsers;
    private long totalAdmins;
    private long totalNormalUsers;
    private long totalApiHits;

    public AdminDashboardResponse(
            long totalUsers,
            long totalAdmins,
            long totalNormalUsers,
            long totalApiHits) {

        this.totalUsers = totalUsers;
        this.totalAdmins = totalAdmins;
        this.totalNormalUsers = totalNormalUsers;
        this.totalApiHits = totalApiHits;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalAdmins() {
        return totalAdmins;
    }

    public long getTotalNormalUsers() {
        return totalNormalUsers;
    }

    public long getTotalApiHits() {
        return totalApiHits;
    }
}