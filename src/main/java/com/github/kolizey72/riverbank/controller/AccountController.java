package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.Currency;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("/{num}")
    public ModelAndView accountDetails(@PathVariable long num, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();

            if (accountService.findAllByUserId(user.getId()).stream().anyMatch(acc -> acc.getNumber().equals(num))) {
                ModelAndView modelAndView = new ModelAndView("accounts/show");
                modelAndView.addObject("account", accountService.findByNumber(num));
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
        accountService.create(new Account(0L, currency), user.getId());
        return "redirect:/";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("accountNumber") long accountNumber,
                          @RequestParam("amount") long amount,
                          Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        try {
            accountService.deposit(user.getId(), accountNumber, amount);
        } catch (IllegalArgumentException ignore) {
            //TODO
        }
        return "redirect:/accounts/" + accountNumber;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountNumber") long accountNumber,
                          @RequestParam("amount") long amount,
                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        try {
            accountService.withdraw(user.getId(), accountNumber, amount);
        } catch (IllegalArgumentException ignore) {
            //TODO
        }
        return "redirect:/accounts/" + accountNumber;
    }

    @GetMapping("/{num}/transfer")
    public ModelAndView transferPage(@PathVariable("num") long senderNumber, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        try {
            if (accountService.findAllByUserId(user.getId()).stream().anyMatch(acc -> acc.getNumber().equals(senderNumber))) {
                ModelAndView modelAndView = new ModelAndView("accounts/transfer");
                modelAndView.addObject("senderNumber", senderNumber);
                return modelAndView;
            } else {
                ModelAndView modelAndView = new ModelAndView("redirect:/error");
                modelAndView.setStatus(HttpStatus.FORBIDDEN);
                return modelAndView;
            }
        } catch (NoSuchElementException exception) {
            ModelAndView modelAndView = new ModelAndView("redirect:/error");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        }
    }

    @PostMapping("/{num}/transfer")
    public String transfer(@PathVariable("num") long senderNumber,
                           @RequestParam("recipientNumber") long recipientNumber,
                           @RequestParam("amount") long amount,
                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        try {
            accountService.transfer(user.getId(), senderNumber, recipientNumber, amount);
        } catch (UnsupportedOperationException exception) {
            //TODO
        }
        return "redirect:/accounts/" + senderNumber;
    }
}
