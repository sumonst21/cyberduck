package ch.cyberduck.core.importer;

/*
 * Copyright (c) 2002-2018 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.Local;
import ch.cyberduck.core.LocalFactory;
import ch.cyberduck.core.Protocol;
import ch.cyberduck.core.ProtocolFactory;
import ch.cyberduck.core.exception.AccessDeniedException;
import ch.cyberduck.core.exception.LocalAccessDeniedException;
import ch.cyberduck.core.preferences.PreferencesFactory;

import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.stream.JsonReader;

public class CloudMounterBookmarkCollectionWindows extends JsonBookmarkCollection {

    @Override
    public String getName() {
        return "CloudMounter";
    }

    @Override
    public Local getFile() {
        return LocalFactory.get(PreferencesFactory.get().getProperty("bookmark.import.cloudmounter.location"));
    }
    @Override
    public String getBundleIdentifier() {
        return "com.cloudmounter";
    }

    @Override
    protected void parse(final ProtocolFactory protocols, final Local file) throws AccessDeniedException {


        try {
            final JsonReader reader = new JsonReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
            reader.beginArray();
            String url;
            String user;
            boolean ssl;
            Protocol protocol;
            while(reader.hasNext()) {
                // to be continued
            }
            reader.endArray();
        }
        catch(IllegalStateException | IOException e) {
            throw new LocalAccessDeniedException(e.getMessage(), e);
        }

    }
}
