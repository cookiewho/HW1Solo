package com.example.hw1soloagain;

import static org.junit.Assert.assertEquals;

import android.view.View;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginActivityTest {
    @Test
    public void samePasswords() {
        assertEquals(true, LoginActivity.passwordCheck("testing", "testing"));
    }

    @Test
    public void differentPasswords() {
        assertEquals(false, LoginActivity.passwordCheck("testing", "notTesting"));
    }

    @Test
    public void validUsername() {
        List<User> validUsers =  new ArrayList<>();
        User testUser = new User(1, "David", "c0w0kie", "legitEmail@wow.com");
        validUsers.add(testUser);
        assertEquals(true, LoginActivity.usernameCheck(validUsers, "c0w0kie"));
    }

    @Test
    public void invalidUsernames() {
        List<User> validUsers =  new ArrayList<>();
        User testUser = new User(1, "David", "c0w0kie", "legitEmail@wow.com");
        validUsers.add(testUser);
        assertEquals(false, LoginActivity.usernameCheck(validUsers, "notAUser"));
    }

}