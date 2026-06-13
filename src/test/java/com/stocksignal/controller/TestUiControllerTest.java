package com.stocksignal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestUiControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
                mockMvc = MockMvcBuilders.standaloneSetup(new TestUiController())
                                .setSingleView(new NoOpView())
                                .build();
    }

    @Test
    void getRoot_and_screenRoutes_returnHtmlViews() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));

        mockMvc.perform(get("/briefing"))
                .andExpect(status().isOk())
                .andExpect(view().name("briefing"));

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
        TestUiController controller = new TestUiController();

        assertEquals("redirect:/login", controller.submitLogin());
        assertEquals("redirect:/register", controller.submitRegister());
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