package com.stocksignal.controller;

import com.stocksignal.dto.DashboardStatisticsDto;
import com.stocksignal.entity.MorningBriefing;
import com.stocksignal.entity.User;
import com.stocksignal.repository.UserRepository;
import com.stocksignal.service.MorningBriefingService;
import com.stocksignal.service.StockSignalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestUiControllerTest {

    private MockMvc mockMvc;
    private MorningBriefingService morningBriefingService;
        private StockSignalService stockSignalService;
        private UserRepository userRepository;
        private User sampleUser;

    @BeforeEach
    void setUp() {
        morningBriefingService = mock(MorningBriefingService.class);
        stockSignalService = mock(StockSignalService.class);
        userRepository = mock(UserRepository.class);
        sampleUser = new User(
                "shalsdn18",
                "encryptedPassword",
                "shalsdn18@hannam.ac.kr",
                "778899123",
                "123456789:ABCdefGhIJKlmNoPQ_TestToken"
        );
        given(userRepository.findByUsername("shalsdn18")).willReturn(Optional.of(sampleUser));
        given(userRepository.save(sampleUser)).willReturn(sampleUser);

        mockMvc = MockMvcBuilders.standaloneSetup(new TestUiController(morningBriefingService, stockSignalService, userRepository))
                .setSingleView(new NoOpView())
                .build();
    }

    @Test
    void getRoot_and_screenRoutes_returnHtmlViews() throws Exception {
        MorningBriefing sampleBriefing = new MorningBriefing(
                "🚀 오늘의 AI 증시 요약",
                "<p>나스닥 <b>1.2% 상승</b></p>",
                "안정세"
        );
        DashboardStatisticsDto stats = new DashboardStatisticsDto();
        stats.setTotalCount(142);
        stats.setBuyCount(98);
        stats.setSellCount(44);
        given(morningBriefingService.getLatestBriefing()).willReturn(sampleBriefing);
        given(stockSignalService.calculateOverallStatistics()).willReturn(stats);

        mockMvc.perform(get("/test/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));

        mockMvc.perform(get("/briefing"))
                .andExpect(status().isOk())
                .andExpect(view().name("briefing"))
                .andExpect(model().attribute("briefing", sampleBriefing))
                .andExpect(model().attribute("dashboardStatistics", stats));

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"));

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void postTestUiEndpoints_redirectBackToScreens() throws Exception {
                TestUiController controller = new TestUiController(morningBriefingService, stockSignalService, userRepository);

        assertEquals("redirect:/", controller.submitLogin());
        assertEquals("redirect:/login", controller.submitRegister());

                RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
                assertEquals("redirect:/settings", controller.submitSettings("new-chat-id", "new-bot-token", redirectAttributes));
                assertEquals("설정이 안전하게 저장되었습니다.", redirectAttributes.getFlashAttributes().get("successMessage"));
                assertEquals("new-chat-id", sampleUser.getTelegramChatId());
                assertEquals("new-bot-token", sampleUser.getTelegramBotToken());
    }

        private static final class NoOpView implements View {

                @Override
                public String getContentType() {
                        return "text/html";
                }

                @Override
                public void render(java.util.Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
                        response.setStatus(HttpServletResponse.SC_OK);
                }
        }
}