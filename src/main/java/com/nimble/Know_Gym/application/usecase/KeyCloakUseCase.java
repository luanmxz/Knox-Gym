package com.nimble.Know_Gym.application.usecase;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RealmRepresentation;

import org.springframework.stereotype.Service;

@Service
public class KeyCloakUseCase {

    private final String serverUrl = "http://localhost:7080";
    private final String realm = "KnoxGym";
    private final String clientId = "knoxgym";

    public Keycloak getKeycloakInstance() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .username("admin")
                .password("admin")
                .build();

        return keycloak;
    }

    public RealmRepresentation getRealm(Keycloak keycloak) {

        RealmRepresentation realm = keycloak.realm("KnoxGym").toRepresentation();

        System.out.println(realm);

        return realm;
    }
}
