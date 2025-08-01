package com.changmin.securewebapp.service;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final Cache<String, Boolean> logoutTokenCache;

    public void logout(String token){
        logoutTokenCache.put(token, true);
    }

    public boolean isLoggedOut(String token) {
        return logoutTokenCache.getIfPresent(token) != null;
    }
}