package com.github.kolizey72.riverbank.controller;

import com.github.kolizey72.riverbank.entity.Account;
import com.github.kolizey72.riverbank.entity.User;
import com.github.kolizey72.riverbank.service.AccountService;
import com.github.kolizey72.riverbank.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@Controller
public class OperationController {

    private final OperationService operationService;
    private final AccountService accountService;

    public OperationController(OperationService operationService, AccountService accountService) {
        this.operationService = operationService;
        this.accountService = accountService;
    }


    @GetMapping("/accounts/{num}/operations")
    public ModelAndView showOperationsOfAccount(@PathVariable long num, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Account account = accountService.findByNumber(num);

            if (accountService.findAllByUserId(user.getId()).stream().anyMatch(acc -> acc.getNumber().equals(num))) {
                ModelAndView modelAndView = new ModelAndView("accounts/operationsList");
                modelAndView.addObject("account", account);
                modelAndView.addObject("operations", operationService.findAllByAccountNumberReverseOrder(num));
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
}
