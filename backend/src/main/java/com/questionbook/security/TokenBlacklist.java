package com.questionbook.security;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {
    private final Set<String> revokedJtis = ConcurrentHashMap.newKeySet();

    public void revoke(String jti) {
        revokedJtis.add(jti);
    }

    public boolean isRevoked(String jti) {
        return revokedJtis.contains(jti);
    }
}
