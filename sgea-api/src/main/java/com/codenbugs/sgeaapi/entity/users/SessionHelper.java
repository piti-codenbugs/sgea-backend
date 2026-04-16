package com.codenbugs.sgeaapi.entity.users;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionHelper {

    public User getCurrentUser(){
        return (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getIdUser();
    }
}
