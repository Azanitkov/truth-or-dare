package com.example.truthordare.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin-area")
public class AdminAuthController {

    @Value("${app.admin.code}")
    private String adminCode;

    @GetMapping("/login")
    public String loginForm() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String code, HttpSession session) {
        if (adminCode.equals(code)) {
            session.setAttribute("adminAuth", true);
            return "redirect:/admin";
        } else {
            return "redirect:/admin-area/login?error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("adminAuth");
        return "redirect:/";
    }
}