package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import com.github.kolizey72.riverbank.service.OperationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final AccountService accountService;
    private final OperationService operationService;

    public UserController(AccountService accountService, OperationService operationService) {
        this.accountService = accountService;
        this.operationService = operationService;
    }

    @GetMapping
    public String userDetails(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("user", user);
        model.addAttribute("accounts", accountService.findAllByUserId(user.getId()));

        return "users/show";
    }

    @GetMapping("/operations")
    public String showOperations(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("user", user);
        model.addAttribute("operations", operationService.findAllByUserId(user.getId()));

        return "operations/list";
    }
}
