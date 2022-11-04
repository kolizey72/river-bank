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
    public String accountDetails(@PathVariable long num, Authentication authentication, Model model) {
            User user = (User) authentication.getPrincipal();

            model.addAttribute("account", accountService.findByNumber(num, user.getId()));
            return "accounts/accountDetails";
    }

    @PostMapping
    public String createAccount(Authentication authentication, @RequestParam("currency") Currency currency) {
        User user = (User) authentication.getPrincipal();
        accountService.create(new Account(0L, currency), user.getId());
        return "redirect:/";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("accountNumber") long accountNumber,
                          @RequestParam(value = "amount", defaultValue = "0") long amount,
                          Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        try {
            accountService.deposit(user.getId(), accountNumber, amount);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("account", accountService.findByNumber(accountNumber));
            model.addAttribute("error", exception);
            return "accounts/accountDetails";
        }
        return "redirect:/accounts/" + accountNumber;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountNumber") long accountNumber,
                          @RequestParam(value = "amount", defaultValue = "0") long amount,
                           Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        try {
            accountService.withdraw(user.getId(), accountNumber, amount);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("account", accountService.findByNumber(accountNumber));
            model.addAttribute("error", exception);
            return "accounts/accountDetails";
        }
        return "redirect:/accounts/" + accountNumber;
    }

    @GetMapping("/{num}/transfer")
    public String transferPage(@PathVariable("num") long senderNumber, Authentication authentication, Model model) {

        User user = (User) authentication.getPrincipal();
        accountService.findByNumber(senderNumber, user.getId());

        model.addAttribute("senderNumber", senderNumber);
        return "accounts/transferPage";
    }

    @PostMapping("/{num}/transfer")
    public String transfer(@PathVariable("num") long senderNumber,
                           @RequestParam("recipientNumber") long recipientNumber,
                           @RequestParam("amount") long amount,
                           Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        try {
            accountService.transfer(user.getId(), senderNumber, recipientNumber, amount);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("senderNumber", senderNumber);
            model.addAttribute("error", exception);
            return "accounts/transferPage";
        } catch (UnsupportedOperationException exception) {
            //TODO
        }
        return "redirect:/accounts/" + senderNumber;
    }
}
