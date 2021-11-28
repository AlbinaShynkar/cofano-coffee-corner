package com.cofano.coffeecorner.controller.filters;

import com.cofano.coffeecorner.data.Database;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * This class filters all requests, and makes sure that the client is logged in.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 *
 */
@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {

    private static final String CLIENT_ID = "360617665982-cvqbqgvnd2tseqd4mccbejm3hh4blt7r.apps.googleusercontent.com";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext ctx) {

		String authorizationHeader = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);
		String idTokenString;

		if (authorizationHeader == null) {

			idTokenString = ctx.getUriInfo().getQueryParameters().getFirst("id_token");

			if (idTokenString == null) {

				ctx.abortWith(Response
						.status(Response.Status.UNAUTHORIZED)
						.entity("No token was provided!")
						.build());
				return;

			}

		} else idTokenString = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

		if (!valid(idTokenString)) {

			ctx.abortWith(Response
					.status(Response.Status.UNAUTHORIZED)
					.entity("Invalid token provided!")
					.build());
			return;

		}

    }

    private boolean valid(String idTokenString) {


        if (idTokenString.equals("")) return false;

        GoogleIdToken idToken = null;
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try { idToken = verifier.verify(idTokenString); }
        catch (GeneralSecurityException | IOException e) { e.printStackTrace(); }

        return idToken != null;

    }

}