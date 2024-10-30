package com.nimble.Know_Gym.infrastructure.keycloak;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class KeyCloakConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Dotenv env = Dotenv.load();
    private final Converter<Jwt, Collection<GrantedAuthority>> delegate = new JwtGrantedAuthoritiesConverter();

    private List<GrantedAuthority> extractRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        Object client = resourceAccess.get(env.get("KEY_CLOAK_REALM_NAME"));

        @SuppressWarnings("unchecked")
        LinkedTreeMap<String, List<String>> clientRoleMap = (LinkedTreeMap<String, List<String>>) client;

        List<String> clientRoles = new ArrayList<>(clientRoleMap.get("roles"));

        return clientRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        List<GrantedAuthority> extractedAuthorities = extractRoles(jwt);
        Collection<GrantedAuthority> authorities = delegate.convert(jwt);

        if (authorities != null) {
            authorities.addAll(extractedAuthorities);
        }

        JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities);
        return token;
    }

}
