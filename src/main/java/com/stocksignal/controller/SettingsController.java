package com.stocksignal.controller;

import com.stocksignal.entity.SystemConfig;
import com.stocksignal.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SystemConfigRepository configRepository;

    @GetMapping
    public String showSettings(Model model) {
        // DB에 없으면 쓰기를 하지 않고 메모리 상에서 빈 객체를 생성하여 Thymeleaf 예외를 원천 방어함
        SystemConfig config = configRepository.findById(1L).orElseGet(SystemConfig::new);
        model.addAttribute("config", config);
        return "settings";
    }

    @PostMapping("/save")
    public String saveSettings(SystemConfig formConfig) {
        // 단일 레코드 유지를 위해 바인딩 ID를 1L로 영구 고정 고수함
        formConfig.setId(1L);
        configRepository.save(formConfig);
        return "redirect:/settings?success=true";
    }
}
