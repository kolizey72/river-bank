package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OperationController {

    private final AccountService accountService;

    public OperationController(AccountService accountService) {
        this.accountService = accountService;
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
            return "accounts/show";
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
            return "accounts/show";
        }
        return "redirect:/accounts/" + accountNumber;
    }

    @GetMapping("/transfer")
    public String transferPage(@RequestParam(value = "num", required = false) Long senderNumber,
                               Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("senderNumber", senderNumber);
        model.addAttribute("userAccounts", accountService.findAllByUserId(user.getId()));

        return "operations/transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("senderNumber") long senderNumber,
                           @RequestParam("recipientNumber") long recipientNumber,
                           @RequestParam("amount") long amount,
                           Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();

        try {
            accountService.transfer(user.getId(), senderNumber, recipientNumber, amount);
        } catch (IllegalArgumentException exception) {
            model.addAttribute("senderNumber", senderNumber);
            model.addAttribute("userAccounts", accountService.findAllByUserId(user.getId()));
            model.addAttribute("error", exception);
            return "operations/transfer";
        } catch (UnsupportedOperationException exception) {
            //TODO
        }

        return "redirect:/accounts/" + senderNumber;
    }
}
