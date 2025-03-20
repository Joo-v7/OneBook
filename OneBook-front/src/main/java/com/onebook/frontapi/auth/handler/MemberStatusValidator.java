package com.onebook.frontapi.auth.handler;

import com.onebook.frontapi.auth.exception.AccessNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Component
public class MemberStatusValidator {
    private final Map<String, Consumer<String>> statusHandlers = new HashMap<>();

    private MemberStatusValidator() {
        statusHandlers.put("ACTIVE", this::handleActive);
        statusHandlers.put("INACTIVE", this::handleInActive);
        statusHandlers.put("DELETED", this::handleDeleted);
        statusHandlers.put("SUSPENDED", this::handleSuspended);
    }

    public void validateMemberStatus(String username, String status) {
        Consumer<String> handler = statusHandlers.get(status);

        if(Objects.nonNull(handler)) {
            handler.accept(username);
            return;
        }

        throw new RuntimeException("처리할 수 없는 회원 상태: " + status);
    }

    public void handleActive(String username) {
        return;
    }

    public void handleInActive(String username) {
        throw new AccessNotFoundException(username+": INACTIVE 회원입니다");
    }

    public void handleDeleted(String username) {
        throw new AccessDeniedException(username+": DELETED 회원입니다.");
    }

    public void handleSuspended(String username) {
        throw new AccessDeniedException(username+": SUSPENDED 회원입니다.");
    }
}
