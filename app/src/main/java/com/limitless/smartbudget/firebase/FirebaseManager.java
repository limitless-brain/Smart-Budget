/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: FirebaseManager.java                                        ////////
 * ////////Class Name: FirebaseManager                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 10/17/19 2:53 PM                                       ////////
 * ////////Author: yazan                                                   ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.limitless.smartbudget.firebase;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.limitless.smartbudget.R;

import java.util.Arrays;
import java.util.List;

public class FirebaseManager implements FirebaseAuth.AuthStateListener {

    //  Singleton instance of this class
    private static FirebaseManager INSTANCE = null;
    //  Firebase authentication ui
    private FirebaseAuth mFirebaseAuth;

    //  State Variables
    //  Firebase sign-in methods
    private List<AuthUI.IdpConfig> mAuthProviders;
    private Intent mSignInIntent;

    /**
     * Private constructor so we will have one instance of this class
     */
    private FirebaseManager() {
        initFirebase();
    }

    /**
     * Get instance of this class
     *
     * @return Instance of {@linkplain FirebaseManager}
     */
    public static FirebaseManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new FirebaseManager();
        return INSTANCE;
    }

    /**
     * Init firebase
     */
    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthProviders = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());
        mSignInIntent = AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setAvailableProviders(mAuthProviders)
                .setIsSmartLockEnabled(false, true)
                .setTheme(R.style.Theme_AppCompat_Light)
                .build();
    }

    public boolean isSignedIn() {
        return mFirebaseAuth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public Intent getSignInIntent() {
        return mSignInIntent;
    }

    public void addAuthStateListener(FirebaseAuth.AuthStateListener listener) {
        mFirebaseAuth.addAuthStateListener(listener);
    }

    public void signOut() {
        mFirebaseAuth.signOut();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Authentication state listener methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        // TODO: 10/17/2019 add sign in dialog
    }
}
