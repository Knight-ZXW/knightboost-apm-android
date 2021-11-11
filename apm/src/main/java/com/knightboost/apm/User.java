package com.knightboost.apm;


import com.knightboost.moonlight.util.CollectionUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Map;

/**
 *
 * created by Knight-ZXW on 2021/11/9
 * Information about the user who triggered an event.
 *
 * <p>```json { "user": { "id": "unique_id", "username": "my_user", "email": "foo@example.com",
 * "ip_address": "127.0.0.1", "subscription": "basic" } } ```
 */
public class User {

    /** Email address of the user. */
    private @Nullable String email;

    /** Unique identifier of the user. */
    private @Nullable String id;

    /** Username of the user. */
    private @Nullable
    String username;

    /** Remote IP address of the user. */
    private @Nullable String ipAddress;

    /**
     * Additional arbitrary fields, as stored in the database (and sometimes as sent by clients). All
     * data from `self.other` should end up here after store normalization.
     */
    private @Nullable Map<String, @NotNull String> other;

    /** unknown fields, only internal usage. */
    private @Nullable Map<String,@NotNull Object> unknown;

    public User() {}

    public User(final @NotNull User user) {
        this.email = user.email;
        this.username = user.username;
        this.id = user.id;
        this.ipAddress = user.ipAddress;
        this.other = CollectionUtils.newConcurrentHashMap(user.other);
        this.unknown = CollectionUtils.newConcurrentHashMap(user.unknown);
    }

    /**
     * Gets the e-mail address of the user.
     *
     * @return the e-mail.
     */
    public @Nullable String getEmail() {
        return email;
    }

    /**
     * Gets the e-mail address of the user.
     *
     * @param email the e-mail.
     */
    public void setEmail(final @Nullable String email) {
        this.email = email;
    }

    /**
     * Gets the id of the user.
     *
     * @return the id.
     */
    public @Nullable String getId() {
        return id;
    }

    /**
     * Sets the id of the user.
     *
     * @param id the user id.
     */
    public void setId(final @Nullable String id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username.
     */
    public @Nullable String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username.
     */
    public void setUsername(final @Nullable String username) {
        this.username = username;
    }

    /**
     * Gets the IP address of the user.
     *
     * @return the IP address of the user.
     */
    public @Nullable String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the IP address of the user.
     *
     * @param ipAddress the IP address of the user.
     */
    public void setIpAddress(final @Nullable String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Gets other user related data.
     *
     * @return the other user data.
     */
    public @Nullable Map<String, String> getOthers() {
        return other;
    }

    /**
     * Sets other user related data.
     *
     * @param other the other user related data..
     */
    public void setOthers(final @Nullable Map<String, String> other) {
        this.other = CollectionUtils.newConcurrentHashMap(other);
    }


    /**
     * the User's unknown fields
     *
     * @return the unknown map
     */
    @TestOnly
    @Nullable
    Map<String, @NotNull Object> getUnknown() {
        return unknown;
    }
}
