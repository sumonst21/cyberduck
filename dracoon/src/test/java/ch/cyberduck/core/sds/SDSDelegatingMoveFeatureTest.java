package ch.cyberduck.core.sds;

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

import ch.cyberduck.core.AlphanumericRandomStringService;
import ch.cyberduck.core.AttributedList;
import ch.cyberduck.core.Credentials;
import ch.cyberduck.core.DisabledConnectionCallback;
import ch.cyberduck.core.DisabledListProgressListener;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.DisabledProgressListener;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.LoginOptions;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathCache;
import ch.cyberduck.core.VersionId;
import ch.cyberduck.core.exception.InteroperabilityException;
import ch.cyberduck.core.exception.NotfoundException;
import ch.cyberduck.core.features.Copy;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.io.StatusOutputStream;
import ch.cyberduck.core.io.StreamCopier;
import ch.cyberduck.core.pool.SessionPool;
import ch.cyberduck.core.sds.io.swagger.client.api.NodesApi;
import ch.cyberduck.core.sds.io.swagger.client.model.EncryptRoomRequest;
import ch.cyberduck.core.sds.triplecrypt.TripleCryptReadFeature;
import ch.cyberduck.core.sds.triplecrypt.TripleCryptWriteFeature;
import ch.cyberduck.core.transfer.Transfer;
import ch.cyberduck.core.transfer.TransferItem;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.core.vault.VaultCredentials;
import ch.cyberduck.core.worker.MoveWorker;
import ch.cyberduck.test.IntegrationTest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import static org.junit.Assert.*;

@Category(IntegrationTest.class)
public class SDSDelegatingMoveFeatureTest extends AbstractSDSTest {

    @Test
    public void testMove() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path test = new SDSTouchFeature(session, nodeid).touch(new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file)), new TransferStatus());
        final Path target = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        assertEquals(new SDSAttributesFinderFeature(session, nodeid).find(test).getVersionId(),
            new SDSDelegatingMoveFeature(session, nodeid, new SDSMoveFeature(session, nodeid)).move(test, target, new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback()).attributes().getVersionId());
        test.attributes().setVersionId(null);
        assertFalse(new SDSFindFeature(nodeid).find(test));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        assertEquals(0, session.getMetrics().get(Copy.class));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveDifferentDataRoom() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room1 = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path room2 = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path test = new Path(room1, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new SDSTouchFeature(session, nodeid).touch(test, new TransferStatus());
        final Path target = new Path(room2, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new SDSDelegatingMoveFeature(session, nodeid, new SDSMoveFeature(session, nodeid)).move(test, target, new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        test.attributes().setVersionId(null);
        assertFalse(new SDSFindFeature(nodeid).find(test));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        assertEquals(0, session.getMetrics().get(Copy.class));
        new SDSDeleteFeature(session, nodeid).delete(Arrays.asList(room1, room2), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveWithRenameDifferentDataRoomSameFilename() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room1 = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path room2 = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path test1 = new Path(room1, "A", EnumSet.of(Path.Type.file));
        new SDSTouchFeature(session, nodeid).touch(test1, new TransferStatus());
        final Path test2 = new Path(room2, "A", EnumSet.of(Path.Type.file));
        new SDSTouchFeature(session, nodeid).touch(test2, new TransferStatus());
        final Path target = new Path(room2, "A (2)", EnumSet.of(Path.Type.file));
        new SDSDelegatingMoveFeature(session, nodeid, new SDSMoveFeature(session, nodeid)).move(test1, target, new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        test1.attributes().setVersionId(null);
        assertFalse(new SDSFindFeature(nodeid).find(test1));
        assertTrue(new SDSFindFeature(nodeid).find(test2));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        assertEquals(0, session.getMetrics().get(Copy.class));
        new SDSDeleteFeature(session, nodeid).delete(Arrays.asList(room1, room2), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveFromEncryptedDataRoom() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room1 = new Path("test", EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt));
        room1.setAttributes(new SDSAttributesFinderFeature(session, nodeid).find(room1));
        final Path room2 = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        room2.setAttributes(new SDSAttributesFinderFeature(session, nodeid).find(room2));
        final byte[] content = RandomUtils.nextBytes(32769);
        final TransferStatus status = new TransferStatus();
        status.setLength(content.length);
        final Path test = new Path(room1, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file, Path.Type.triplecrypt));
        final SDSEncryptionBulkFeature bulk = new SDSEncryptionBulkFeature(session, nodeid);
        bulk.pre(Transfer.Type.upload, Collections.singletonMap(new TransferItem(test), status), new DisabledConnectionCallback());
        final TripleCryptWriteFeature writer = new TripleCryptWriteFeature(session, nodeid, new SDSWriteFeature(session, nodeid));
        final StatusOutputStream<VersionId> out = writer.write(test, status, new DisabledConnectionCallback());
        assertNotNull(out);
        new StreamCopier(status, status).transfer(new ByteArrayInputStream(content), out);
        final Path target = new Path(room2, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new SDSDelegatingMoveFeature(session, nodeid, new SDSMoveFeature(session, nodeid)).move(test, target, new TransferStatus().length(content.length), new Delete.DisabledCallback(), new DisabledConnectionCallback() {
            @Override
            public void warn(final Host bookmark, final String title, final String message, final String defaultButton, final String cancelButton, final String preference) {
                //
            }

            @Override
            public Credentials prompt(final Host bookmark, final String title, final String reason, final LoginOptions options) {
                return new VaultCredentials("eth[oh8uv4Eesij");
            }
        });
        assertEquals(1, session.getMetrics().get(Copy.class));
        test.attributes().setVersionId(null);
        assertFalse(new SDSFindFeature(nodeid).find(test));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        final byte[] compare = new byte[content.length];
        final InputStream stream = new SDSReadFeature(session, nodeid).read(target, new TransferStatus().length(content.length), new DisabledConnectionCallback() {
            @Override
            public void warn(final Host bookmark, final String title, final String message, final String defaultButton, final String cancelButton, final String preference) {
                //
            }

            @Override
            public Credentials prompt(final Host bookmark, final String title, final String reason, final LoginOptions options) {
                return new VaultCredentials("eth[oh8uv4Eesij");
            }
        });
        IOUtils.readFully(stream, compare);
        stream.close();
        assertArrayEquals(content, compare);
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room2), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveToEncryptedDataRoom() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room1 = new Path("test", EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt));
        room1.setAttributes(new SDSAttributesFinderFeature(session, nodeid).find(room1));
        final Path room2 = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume)), null, new TransferStatus());
        room2.setAttributes(new SDSAttributesFinderFeature(session, nodeid).find(room2));
        final byte[] content = RandomUtils.nextBytes(32769);
        final TransferStatus status = new TransferStatus();
        status.setLength(content.length);
        final Path test = new Path(room2, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file, Path.Type.triplecrypt));
        final SDSWriteFeature writer = new SDSWriteFeature(session, nodeid);
        final StatusOutputStream<VersionId> out = writer.write(test, status, new DisabledConnectionCallback());
        assertNotNull(out);
        new StreamCopier(status, status).transfer(new ByteArrayInputStream(content), out);
        final Path target = new Path(room1, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new SDSDelegatingMoveFeature(session, nodeid, new SDSMoveFeature(session, nodeid)).move(test, target, new TransferStatus().length(content.length), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        assertEquals(1, session.getMetrics().get(Copy.class));
        test.attributes().setVersionId(null);
        assertFalse(new SDSFindFeature(nodeid).find(test));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        final byte[] compare = new byte[content.length];
        final InputStream stream = new TripleCryptReadFeature(session, nodeid, new SDSReadFeature(session, nodeid)).read(target, new TransferStatus().length(content.length), new DisabledConnectionCallback() {
            @Override
            public void warn(final Host bookmark, final String title, final String message, final String defaultButton, final String cancelButton, final String preference) {
                //
            }

            @Override
            public Credentials prompt(final Host bookmark, final String title, final String reason, final LoginOptions options) {
                return new VaultCredentials("eth[oh8uv4Eesij");
            }
        });
        IOUtils.readFully(stream, compare);
        stream.close();
        assertArrayEquals(content, compare);
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room2), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveBetweenEncryptedDataRooms() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room1 = new Path("test", EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt));
        room1.setAttributes(new SDSAttributesFinderFeature(session, nodeid).find(room1));
        final Path room2 = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final EncryptRoomRequest encrypt = new EncryptRoomRequest();
        encrypt.setIsEncrypted(true);
        new NodesApi(session.getClient()).encryptRoom(Long.parseLong(new SDSNodeIdProvider(session).withCache(cache).getFileid(room2,
            new DisabledListProgressListener())), encrypt, StringUtils.EMPTY, null);
        room2.getType().add(Path.Type.triplecrypt);
        final byte[] content = RandomUtils.nextBytes(32769);
        final TransferStatus status = new TransferStatus();
        status.setLength(content.length);
        final Path test = new Path(room1, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file, Path.Type.triplecrypt));
        final SDSEncryptionBulkFeature bulk = new SDSEncryptionBulkFeature(session, nodeid);
        bulk.pre(Transfer.Type.upload, Collections.singletonMap(new TransferItem(test), status), new DisabledConnectionCallback());
        final TripleCryptWriteFeature writer = new TripleCryptWriteFeature(session, nodeid, new SDSWriteFeature(session, nodeid));
        final StatusOutputStream<VersionId> out = writer.write(test, status, new DisabledConnectionCallback());
        assertNotNull(out);
        new StreamCopier(status, status).transfer(new ByteArrayInputStream(content), out);
        final Path target = new Path(room2, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new SDSDelegatingMoveFeature(session, nodeid, new SDSMoveFeature(session, nodeid)).move(test, target, new TransferStatus().length(content.length), new Delete.DisabledCallback(), new DisabledConnectionCallback() {
            @Override
            public void warn(final Host bookmark, final String title, final String message, final String defaultButton, final String cancelButton, final String preference) {
                //
            }

            @Override
            public Credentials prompt(final Host bookmark, final String title, final String reason, final LoginOptions options) {
                return new VaultCredentials("eth[oh8uv4Eesij");
            }
        });
        test.attributes().setVersionId(null);
        assertFalse(new SDSFindFeature(nodeid).find(test));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        final byte[] compare = new byte[content.length];
        final InputStream stream = new TripleCryptReadFeature(session, nodeid, new SDSReadFeature(session, nodeid)).read(target, new TransferStatus().length(content.length), new DisabledConnectionCallback() {
            @Override
            public void warn(final Host bookmark, final String title, final String message, final String defaultButton, final String cancelButton, final String preference) {
                //
            }

            @Override
            public Credentials prompt(final Host bookmark, final String title, final String reason, final LoginOptions options) {
                return new VaultCredentials("eth[oh8uv4Eesij");
            }
        });
        IOUtils.readFully(stream, compare);
        stream.close();
        assertArrayEquals(content, compare);
        new SDSDeleteFeature(session, nodeid).delete(Arrays.asList(target, room2), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveDirectory() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final String foldername = new AlphanumericRandomStringService().random();
        final Path test = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(room, foldername, EnumSet.of(Path.Type.directory)), null, new TransferStatus());
        final Path target = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory));
        new SDSMoveFeature(session, nodeid).move(test, target, new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        assertEquals(0, session.getMetrics().get(Copy.class));
        assertFalse(new SDSFindFeature(nodeid).find(new Path(room, foldername, EnumSet.of(Path.Type.directory))));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test(expected = InteroperabilityException.class)
    public void testMoveDirectoryToRoot() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path test = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory)), null, new TransferStatus());
        final Path target = new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory));
        final SDSMoveFeature move = new SDSMoveFeature(session, nodeid);
        assertFalse(move.isSupported(test, target));
        move.move(test, target, new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        assertEquals(0, session.getMetrics().get(Copy.class));
        assertFalse(new SDSFindFeature(nodeid).find(test));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test(expected = InteroperabilityException.class)
    public void testMoveRoomToDirectory() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path target = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory));
        final Path test = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final SDSMoveFeature move = new SDSMoveFeature(session, nodeid);
        assertFalse(move.isSupported(test, target));
        move.move(test, target, new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        assertEquals(0, session.getMetrics().get(Copy.class));
        assertFalse(new SDSFindFeature(nodeid).find(test));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveSubroom() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path target = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory));
        final Path subroom = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(room,
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final SDSMoveFeature move = new SDSMoveFeature(session, nodeid);
        assertTrue(move.isSupported(subroom, target));
        move.move(subroom, target, new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        assertEquals(0, session.getMetrics().get(Copy.class));
        subroom.attributes().setVersionId(null);
        assertFalse(new SDSFindFeature(nodeid).find(subroom));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveDataRoom() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final String directoryname = new AlphanumericRandomStringService().random();
        final Path test = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(directoryname, EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final Path target = new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt));
        new SDSMoveFeature(session, nodeid).move(test, target, new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        assertEquals(0, session.getMetrics().get(Copy.class));
        assertFalse(new SDSFindFeature(nodeid).find(new Path(directoryname, EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt))));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(target), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveEncryptedDataRoom() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final String roomName = new AlphanumericRandomStringService().random();
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(roomName, EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final EncryptRoomRequest encrypt = new EncryptRoomRequest();
        encrypt.setIsEncrypted(true);
        new NodesApi(session.getClient()).encryptRoom(Long.parseLong(new SDSNodeIdProvider(session).withCache(cache).getFileid(room,
            new DisabledListProgressListener())), encrypt, StringUtils.EMPTY, null);
        final AttributedList<Path> list = new SDSListService(session, nodeid).list(room.getParent(), new DisabledListProgressListener());
        final Path encrypted = list.get(room);
        // create file inside encrypted room
        final Path folder = new SDSDirectoryFeature(session, nodeid).mkdir(
            new Path(encrypted, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.directory)), null, new TransferStatus());
        final Path renamed = new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt));
        final MoveWorker worker = new MoveWorker(Collections.singletonMap(encrypted, renamed), new SessionPool.SingleSessionPool(session), PathCache.empty(),
            new DisabledProgressListener(), new DisabledLoginCallback());
        worker.run(session);
        assertEquals(0, session.getMetrics().get(Copy.class));
        assertFalse(new SDSFindFeature(nodeid).find(new Path(roomName, EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt))));
        assertTrue(new SDSFindFeature(nodeid).find(renamed));
        assertTrue(new SDSFindFeature(nodeid).find(new Path(renamed, folder.getName(), EnumSet.of(Path.Type.directory, Path.Type.directory))));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(renamed), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveOverride() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final String filename = new AlphanumericRandomStringService().random();
        final Path test = new SDSTouchFeature(session, nodeid).touch(new Path(room, filename, EnumSet.of(Path.Type.file)), new TransferStatus());
        final Path target = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new SDSTouchFeature(session, nodeid).touch(target, new TransferStatus());
        new SDSMoveFeature(session, nodeid).move(test, target, new TransferStatus().exists(true), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        assertFalse(new SDSFindFeature(nodeid).find(new Path(room, filename, EnumSet.of(Path.Type.file))));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testMoveToDifferentParentAndRename() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt)), null, new TransferStatus());
        final String filename = new AlphanumericRandomStringService().random();
        final Path test = new SDSTouchFeature(session, nodeid).touch(new Path(room, filename, EnumSet.of(Path.Type.file)), new TransferStatus());
        final Path target = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new SDSTouchFeature(session, nodeid).touch(target, new TransferStatus());
        new SDSMoveFeature(session, nodeid).move(test, target, new TransferStatus().exists(true), new Delete.DisabledCallback(), new DisabledConnectionCallback());
        assertFalse(new SDSFindFeature(nodeid).find(new Path(room, filename, EnumSet.of(Path.Type.file))));
        assertTrue(new SDSFindFeature(nodeid).find(target));
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test(expected = NotfoundException.class)
    public void testMoveNotFound() throws Exception {
        final Path room = new Path(
            new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume, Path.Type.triplecrypt));
        final Path test = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session).withCache(cache);
        new SDSMoveFeature(session, nodeid).move(test, new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file)), new TransferStatus(), new Delete.DisabledCallback(), new DisabledConnectionCallback());
    }
}
