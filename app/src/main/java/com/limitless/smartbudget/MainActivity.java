/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: MainActivity.java                                        ////////
 * ////////Class Name: MainActivity                                  ////////
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

package com.limitless.smartbudget;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.limitless.smartbudget.assistant.Assistant;
import com.limitless.smartbudget.assistant.AssistantDataInterface;
import com.limitless.smartbudget.firebase.FirebaseManager;
import com.limitless.smartbudget.ui.AboutFragment;
import com.limitless.smartbudget.ui.DashboardFragment;
import com.limitless.smartbudget.ui.SettingsFragment;
import com.limitless.smartbudget.ui.TableManagementFragment;
import com.limitless.smartbudget.utils.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int PERMISSION_REQUEST = 100;
    public static final String VOICE_NOTE = "VoiceNote";
    public static final String FIRST_TIME = "firstTime";
    public static final int INTRO_REQUEST_CODE = 10;

    //  UI
    MeowBottomNavigation mBottomNavigation;
    Menu mMenu;
    String[] windowsTitle = {
            "Dashboard",
            "Tables Management",
            "Setting",
            "About"
    };

    FragmentManager fragmentManager;

    private Assistant assistant;
    private int selectedPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        initApp();
    }

    private void initApp() {
        //  Check permission
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Snackbar.make(mBottomNavigation, "Audio Record permission required for" +
                        "budget assistant", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Grant", v -> {

                            ActivityCompat.requestPermissions(this
                                    , new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST);
                        })
                        .setActionTextColor(Color.CYAN)
                        .show();
            } else {
                ActivityCompat.requestPermissions(this
                        , new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST);
            }
        }

        //  Assistant
        assistant = new Assistant(this)
                .setMaxResults(PERMISSION_REQUEST)
                .setConfidence(0.8f)
                .setMaxResults(3)
                .setOffline(true)
                .setAwake(false);

        //  handling user sign-in
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        if (!FirebaseManager.getInstance().isSignedIn()) {
            Snackbar.make(mBottomNavigation, "You are not sign-in!"
                    , Snackbar.LENGTH_INDEFINITE)
                    .setAction("Sign-In", v -> {
                        startActivityForResult(FirebaseManager.getInstance().getSignInIntent()
                                , Constants.FIREBASE_REQUEST_CODE);
                    })
                    .setActionTextColor(Color.MAGENTA)
                    .show();
        } else {
            //  We already sign-in
        }

        if (getPreferences(MODE_PRIVATE).getBoolean(FIRST_TIME, true)) {
            startActivityForResult(new Intent(this, AppIntro.class)
                    , INTRO_REQUEST_CODE);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Activity overridden methods
    ///////////////////////////////////////////////////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.FIREBASE_REQUEST_CODE:
                //  it's sign in request
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (resultCode == RESULT_OK) {
                    //  User sign-in
                } else {
                    if (response == null) {
                        //  User press back button
                        Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Sign-in canceled!", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
                break;
            case INTRO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    getPreferences(MODE_PRIVATE).edit().putBoolean(FIRST_TIME, false).apply();
                } else if (resultCode == RESULT_CANCELED) {
                    finish();
                } else {
                    finish();
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(this
                            , new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST);
                }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Ui methods
    ///////////////////////////////////////////////////////////////////////////
    private void initUi() {

        fragmentManager = getSupportFragmentManager();
        mBottomNavigation = findViewById(R.id.meow_bottom_navigation);
        mBottomNavigation.add(new MeowBottomNavigation.Model(1, android.R.drawable.ic_menu_today));
        mBottomNavigation.add(new MeowBottomNavigation.Model(2, android.R.drawable.ic_menu_gallery));
        mBottomNavigation.add(new MeowBottomNavigation.Model(3, android.R.drawable.ic_menu_preferences));
        mBottomNavigation.add(new MeowBottomNavigation.Model(4, android.R.drawable.ic_menu_info_details));

        mBottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    selectedPage = 2;
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new DashboardFragment(
                                    getApplicationContext()))
                            .commit();
                    break;
                case 2:
                    selectedPage = 1;
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new TableManagementFragment()).commit();
                    break;
                case 3:
                    selectedPage = 0;
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new SettingsFragment(
                                    getApplicationContext())).commit();
                    break;
                case 4:
                    selectedPage = -1;
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new AboutFragment()).commit();
                    break;
            }
            Objects.requireNonNull(getSupportActionBar()).setTitle(windowsTitle[model.getId() - 1]);
            mMenu.findItem(R.id.speechAction).setVisible(selectedPage > 0);

            return null;
        });
        mBottomNavigation.show(2, true);
        fragmentManager.beginTransaction().
                replace(R.id.fragment_layout, new TableManagementFragment()).commit();
        Objects.requireNonNull(getSupportActionBar()).setTitle(windowsTitle[1]);
    }


    public void setAssistantDataListener(AssistantDataInterface listener) {
        assistant.setAssistantDataListener(listener);
    }
    ///////////////////////////////////////////////////////////////////////////
    // Action Bar menu
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.speechAction:
                if (getPreferences(MODE_PRIVATE).getBoolean(VOICE_NOTE, true)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Voice Command")
                            .setMessage("This feature still " +
                                    "under development and it's case app crashing." +
                                    "\nTo use this you must be in Table management tab." +
                                    "\nYou have to say a category name if you want to add" +
                                    "to living expenses and you must select it's tab." +
                                    "\nSaying category name as description in other table" +
                                    " tabs case to add record with out description.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                dialog.dismiss();
                                getPreferences(MODE_PRIVATE).edit().putBoolean(VOICE_NOTE, false)
                                        .apply();
                                assistant.listenToRecord(getSupportFragmentManager());
                            })
                            .show();
                } else {
                    assistant.listenToRecord(getSupportFragmentManager());
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
