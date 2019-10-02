/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: MainActivity.java                                        ////////
 * ////////Class Name: MainActivity                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 10/2/19 4:31 PM                                       ////////
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

package com.limitless.smartbudget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.limitless.smartbudget.ui.AboutFragment;
import com.limitless.smartbudget.ui.AppViewModel;
import com.limitless.smartbudget.ui.DashboardFragment;
import com.limitless.smartbudget.ui.SettingsFragment;
import com.limitless.smartbudget.ui.TableManagementFragment;
import com.limitless.smartbudget.utils.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    //  UI
    MeowBottomNavigation bottomNavigation;
    String[] windowsTitle = {
            "Dashboard",
            "Tables Management",
            "Setting",
            "About"
    };

    FragmentManager fragmentManager;
    private AppViewModel mViewModel;
    //  Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Get reference to viewModel
        mViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        //  handling user sign-in
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );
        mViewModel.signInIntent =
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_launcher_foreground)
                        .setTheme(R.style.Theme_AppCompat_Light)
                        .build();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mViewModel.firebaseAuth = mFirebaseAuth;
        //  Firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        if (mFirebaseAuth.getCurrentUser() == null) {
            startActivityForResult(mViewModel.signInIntent, Constants.FIREBASE_REQUEST_CODE);
        } else {
            //  We already sign-in
            mViewModel.isSignIn = true;
            mViewModel.firebaseUser = mFirebaseAuth.getCurrentUser();
        }
        initUi();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Activity overridden methods
    ///////////////////////////////////////////////////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.FIREBASE_REQUEST_CODE) {
            //  it's sign in request
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                //  User sign-in
                mViewModel.firebaseUser = mFirebaseAuth.getCurrentUser();
                mViewModel.isSignIn = true;
            } else {
                if (response == null) {
                    //  User press back button
                    Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Sign-in canceled!", Snackbar.LENGTH_LONG)
                            .show();
                    mViewModel.isSignIn = false;
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Ui methods
    ///////////////////////////////////////////////////////////////////////////
    private void initUi() {

        Log.i(MainActivity.class.getSimpleName(), "Hello");
        fragmentManager = getSupportFragmentManager();
        bottomNavigation = findViewById(R.id.meow_bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, android.R.drawable.ic_menu_today));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, android.R.drawable.ic_menu_gallery));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, android.R.drawable.ic_menu_preferences));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, android.R.drawable.ic_menu_info_details));

        bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                // TODO: 9/22/2019 Switch between views
                case 1:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new DashboardFragment(
                                    getApplicationContext()))
                            .commit();
                    break;
                case 3:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new SettingsFragment(
                                    getApplicationContext(), mViewModel)).commit();
                    break;
                case 2:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new TableManagementFragment()).commit();
                    break;
                case 4:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new AboutFragment()).commit();
                    break;
            }
            Objects.requireNonNull(getSupportActionBar()).setTitle(windowsTitle[model.getId() - 1]);
            return null;
        });
        bottomNavigation.show(2, true);
        fragmentManager.beginTransaction().
                replace(R.id.fragment_layout, new TableManagementFragment()).commit();
        Objects.requireNonNull(getSupportActionBar()).setTitle(windowsTitle[1]);
    }

}
