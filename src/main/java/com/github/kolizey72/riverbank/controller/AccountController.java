package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.Currency;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("/{num}")
    public String show(@PathVariable long num,
                                 Authentication authentication, Model model) {
            User user = (User) authentication.getPrincipal();

            model.addAttribute("account", accountService.findByNumber(num, user.getId()));

            return "accounts/show";
    }

    @PostMapping
    public String create(@RequestParam("currency") Currency currency,
                                Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        accountService.create(new Account(0L, currency), user.getId());

        return "redirect:/profile";
    }

    @DeleteMapping("/{num}")
    public String delete(@PathVariable("num") long number,
                         Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        accountService.delete(number, user.getId());

        return "redirect:/profile";
    }
}
