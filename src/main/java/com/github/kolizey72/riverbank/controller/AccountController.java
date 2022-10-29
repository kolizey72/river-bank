package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.Currency;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import com.github.kolizey72.riverbank.service.OperationService;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final OperationService operationService;

    public AccountController(AccountService accountService, OperationService operationService) {
        this.accountService = accountService;
        this.operationService = operationService;
    }

    @RequestMapping("/{num}")
    public ModelAndView accountDetails(@PathVariable long num, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Account account = accountService.findByNumber(num);

            if (accountService.findAllByUserId(user.getId()).stream().anyMatch(acc -> acc.getNumber().equals(num))) {
                ModelAndView modelAndView = new ModelAndView("accounts/show");
                modelAndView.addObject("account", account);
                modelAndView.addObject("operations", operationService.findAllByAccountNumber(num));
                return modelAndView;
            } else {
                ModelAndView modelAndView = new ModelAndView("redirect:/error");
                modelAndView.setStatus(HttpStatus.FORBIDDEN);
                return modelAndView;
            }
        } catch (NoSuchElementException e) {
            ModelAndView modelAndView = new ModelAndView("redirect:/error");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        }
    }

    @PostMapping
    public String createAccount(Authentication authentication, @RequestParam("currency") Currency currency) {
        User user = (User) authentication.getPrincipal();
        accountService.create(new Account(user, 0L, currency));
        return "redirect:/";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("accountNumber") long accountNumber,
                          @RequestParam("amount") long amount) {
        accountService.deposit(accountNumber, amount);
        return "redirect:/accounts/" + accountNumber;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountNumber") long accountNumber,
                          @RequestParam("amount") long amount) {
        try {
            accountService.withdraw(accountNumber, amount);
        } catch (IllegalArgumentException ignore) {
            //TODO
        }
        return "redirect:/accounts/" + accountNumber;
    }
}
