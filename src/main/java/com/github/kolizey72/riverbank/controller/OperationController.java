package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import com.github.kolizey72.riverbank.service.OperationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OperationController {

    private final OperationService operationService;
    private final AccountService accountService;

    public OperationController(OperationService operationService, AccountService accountService) {
        this.operationService = operationService;
        this.accountService = accountService;
    }


    @GetMapping("/accounts/{num}/operations")
    public String showOperationsOfAccount(@PathVariable long num, Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        Account account = accountService.findByNumber(num, user.getId());

        model.addAttribute("account", account);
        model.addAttribute("operations", operationService.findAllByAccountNumberReverseOrder(num));

        return "accounts/operationsList";
    }
}
