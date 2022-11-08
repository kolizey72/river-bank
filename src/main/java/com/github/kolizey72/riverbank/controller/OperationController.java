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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("accounts/deposit")
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

    @PostMapping("accounts/withdraw")
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

    @GetMapping("/transfer")
    public String transferPage(@RequestParam(value = "num", required = false) Long senderNumber, Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("senderNumber", senderNumber);
        model.addAttribute("userAccounts", accountService.findAllByUserId(user.getId()));
        return "accounts/transferPage";
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
            return "accounts/transferPage";
        } catch (UnsupportedOperationException exception) {
            //TODO
        }
        return "redirect:/accounts/" + senderNumber;
    }
}
