package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final AccountService accountService;

    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String userDetails(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        List<Account> accounts = accountService.findAllByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);

        return "users/userDetails";
    }
}
