package fr.ael.hopital.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityRestController {

    @GetMapping("profile")
    public UsernamePasswordAuthenticationToken authentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UsernamePasswordAuthenticationToken)authentication;
    }
}
