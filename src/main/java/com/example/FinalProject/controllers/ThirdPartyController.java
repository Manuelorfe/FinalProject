package com.example.FinalProject.controllers;

import com.example.FinalProject.dtos.ThirdPartyTransactionDTO;
import com.example.FinalProject.models.accounts.Account;
import com.example.FinalProject.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.headers.HeadersSecurityMarker;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/third-party")
public class ThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/transference")
    @ResponseStatus(HttpStatus.OK)
    public Account makeThirdPartyTransference (@RequestHeader String hashedKey, @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO){
        return thirdPartyService.makeThirdPartyTransference(hashedKey,thirdPartyTransactionDTO);
    }


}
