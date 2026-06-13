package com.stocksignal.controller;

import com.stocksignal.entity.MorningBriefing;
import com.stocksignal.service.MorningBriefingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    @BeforeEach
    void setUp() {
        morningBriefingService = mock(MorningBriefingService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new TestUiController(morningBriefingService))
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
        given(morningBriefingService.getLatestBriefing()).willReturn(sampleBriefing);

        mockMvc.perform(get("/test/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));

        mockMvc.perform(get("/briefing"))
                .andExpect(status().isOk())
                .andExpect(view().name("briefing"))
                .andExpect(model().attribute("briefing", sampleBriefing));

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
        TestUiController controller = new TestUiController(morningBriefingService);

        assertEquals("redirect:/", controller.submitLogin());
        assertEquals("redirect:/login", controller.submitRegister());
        assertEquals("redirect:/settings", controller.submitSettings());
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