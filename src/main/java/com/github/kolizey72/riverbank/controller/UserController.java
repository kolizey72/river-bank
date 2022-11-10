package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class UserController {

    private final AccountService accountService;

    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String show(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("user", user);
        model.addAttribute("accounts", accountService.findAllByUserId(user.getId()));

        return "users/show";
    }
}
