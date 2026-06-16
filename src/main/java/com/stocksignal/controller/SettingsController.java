package com.stocksignal.controller;

import com.stocksignal.entity.SystemConfig;
import com.stocksignal.repository.SystemConfigRepository;
import com.stocksignal.service.TossTokenManager;
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
    private final TossTokenManager tossTokenManager;

    @GetMapping
    public String showSettings(Model model) {
        model.addAttribute("config", configRepository.findById(1L).orElseGet(SystemConfig::new));
        return "settings";
    }

    @PostMapping("/save")
    public String saveSettings(SystemConfig formConfig) {
        SystemConfig currentConfig = configRepository.findById(1L).orElseGet(SystemConfig::new);
        currentConfig.setTelegramBotToken(formConfig.getTelegramBotToken() != null ? formConfig.getTelegramBotToken().trim() : "");
        currentConfig.setTelegramChatId(formConfig.getTelegramChatId() != null ? formConfig.getTelegramChatId().trim() : "");
        currentConfig.setTossClientId(formConfig.getTossClientId() != null ? formConfig.getTossClientId().trim() : "");
        currentConfig.setTossClientSecret(formConfig.getTossClientSecret() != null ? formConfig.getTossClientSecret().trim() : "");
        currentConfig.setTossAccountSeq(formConfig.getTossAccountSeq() != null ? formConfig.getTossAccountSeq().trim() : "1");
        
        configRepository.save(currentConfig);
        tossTokenManager.clearCache();
        
        return "redirect:/settings?success=true";
    }
}