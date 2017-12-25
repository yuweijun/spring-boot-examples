package com.example.spring.boot;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Secured("ROLE_USER")
    public String secure() {
        return "Hello Security";
    }

    @PreAuthorize("true")
    public String authorized() {
        return "Hello World";
    }

    @PreAuthorize("false")
    public String denied() {
        return "Goodbye World";
    }

}