package ch.cyberduck.core.oauth;

/*
 * Copyright (c) 2002-2017 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.DisabledCancelCallback;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.LoginCallback;
import ch.cyberduck.core.OAuthTokens;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.LoginFailureException;
import ch.cyberduck.core.http.DisabledServiceUnavailableRetryStrategy;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

public class OAuth2ErrorResponseInterceptor extends DisabledServiceUnavailableRetryStrategy {
    private static final Logger log = Logger.getLogger(OAuth2ErrorResponseInterceptor.class);

    private static final int MAX_RETRIES = 1;

    private final Host bookmark;
    private final OAuth2RequestInterceptor service;
    private final LoginCallback prompt;

    public OAuth2ErrorResponseInterceptor(final Host bookmark
        , final OAuth2RequestInterceptor service, final LoginCallback prompt) {
        this.bookmark = bookmark;
        this.service = service;
        this.prompt = prompt;
    }

    @Override
    public boolean retryRequest(final HttpResponse response, final int executionCount, final HttpContext context) {
        switch(response.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_UNAUTHORIZED:
                if(executionCount <= MAX_RETRIES) {
                    try {
                        try {
                            log.info(String.format("Attempt to refresh OAuth tokens for failure %s", response));
                            service.setTokens(service.refresh());
                        }
                        catch(LoginFailureException e) {
                            log.warn(String.format("Failure refreshing OAuth tokens. %s", e));
                            // Reset OAuth Tokens
                            bookmark.getCredentials().setOauth(OAuthTokens.EMPTY);
                            service.setTokens(service.authorize(bookmark, prompt, new DisabledCancelCallback()));
                        }
                        // Try again
                        return true;
                    }
                    catch(BackgroundException e) {
                        log.warn(String.format("Failure refreshing OAuth tokens. %s", e));
                    }
                }
                break;
        }
        return false;
    }
}
