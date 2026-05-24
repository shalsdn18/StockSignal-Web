package com.stocksignal.controller;

import com.stocksignal.entity.MorningBriefing;
import com.stocksignal.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestUiController {

    @GetMapping("/test/login")
    public String testLogin() {
        return "login";
    }

    @GetMapping("/test/register")
    public String testRegister() {
        return "register";
    }

    @GetMapping("/test/briefing")
    public String testBriefing(Model model) {
        MorningBriefing briefing = new MorningBriefing(
                "🚀 2026년 5월 24일 주요 증시 동향 및 AI 시그널 요약",
                "<p>나스닥 지수가 <b>1.2% 상승</b> 마감했습니다.</p>",
                "나스닥 +1.2%, 환율 1,345원"
        );

        model.addAttribute("briefing", briefing);
        model.addAttribute("totalCount", 142);
        model.addAttribute("buyCount", 98);
        model.addAttribute("sellCount", 44);

        return "briefing";
    }

    @GetMapping("/test/settings")
    public String testSettings(Model model) {
        User user = new User(
                "shalsdn18",
                "encryptedPassword",
                "shalsdn18@hannam.ac.kr",
                "778899123",
                "123456789:ABCdefGhIJKlmNoPQ_TestToken"
        );

        model.addAttribute("user", user);
        model.addAttribute("totalCount", 142);
        model.addAttribute("buyCount", 98);
        model.addAttribute("sellCount", 44);

        return "settings";
    }
}