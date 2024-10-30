package com.nimble.Know_Gym.adapters.dto.request;

public record LoginRequest(String clientId, String username, String password, String grantType) {
}
