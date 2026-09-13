package com.switchproject.demo.service;

import com.switchproject.demo.dto.AdminDashboardResponse;
import com.switchproject.demo.model.Role;
import com.switchproject.demo.repository.ApiLogRepository;
import com.switchproject.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final ApiLogRepository apiLogRepository;

    public AdminDashboardService(
            UserRepository userRepository,
            ApiLogRepository apiLogRepository) {

        this.userRepository = userRepository;
        this.apiLogRepository = apiLogRepository;
    }

    public AdminDashboardResponse getDashboard() {

        long totalUsers = userRepository.count();

        long totalAdmins =
                userRepository.countByRole(Role.ADMIN);

        long totalNormalUsers =
                userRepository.countByRole(Role.USER);

        long totalApiHits =
                apiLogRepository.count();

        return new AdminDashboardResponse(
                totalUsers,
                totalAdmins,
                totalNormalUsers,
                totalApiHits
        );
    }
}