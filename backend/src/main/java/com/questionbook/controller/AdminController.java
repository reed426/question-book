package com.questionbook.controller;

import com.questionbook.dto.AdminStatsResponse;
import com.questionbook.dto.CustomQuestionAdminView;
import com.questionbook.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/stats")
    public AdminStatsResponse stats() {
        return adminService.getStats();
    }

    @GetMapping("/custom-questions")
    public List<CustomQuestionAdminView> customQuestions() {
        return adminService.getRecentCustomQuestions();
    }
}
