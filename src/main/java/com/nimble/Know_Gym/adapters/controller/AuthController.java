package com.nimble.Know_Gym.adapters.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.nimble.Know_Gym.adapters.dto.request.LoginRequest;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final Dotenv env = Dotenv.load();

    @GetMapping("/")
    public String index(@AuthenticationPrincipal Jwt jwt) {
        String response = String.format("Hello, %s", jwt.getClaimAsString("preferred_username"));
        return response;
    }

    @PostMapping("/")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", request.clientId());
        formData.add("username", request.username());
        formData.add("password", request.password());
        formData.add("grant_type", request.grantType());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(env.get("KEY_CLOAK_TOKEN_URL"), entity,
                String.class);

        return response;
    }
}
