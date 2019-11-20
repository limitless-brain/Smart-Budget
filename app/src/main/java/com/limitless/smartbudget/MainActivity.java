/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: MainActivity.java                                        ////////
 * ////////Class Name: MainActivity                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 11/20/19 1:05 PM                                       ////////
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
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.limitless.smartbudget.assistant.Assistant;
import com.limitless.smartbudget.assistant.AssistantDataInterface;
import com.limitless.smartbudget.ui.fragments.AboutFragment;
import com.limitless.smartbudget.ui.fragments.DashboardFragment;
import com.limitless.smartbudget.ui.fragments.SettingsFragment;
import com.limitless.smartbudget.ui.fragments.TableManagementFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Start showcase activity
        if (getPreferences(MODE_PRIVATE).getBoolean(FIRST_TIME, true)) {
            startActivityForResult(new Intent(this, AppShowcase.class)
                    , INTRO_REQUEST_CODE);
        }

        setContentView(R.layout.activity_main);

        initUi();
        initApp();

    }

    private void initApp() {

        //  Assistant
        assistant = new Assistant(this)
                .setMaxResults(PERMISSION_REQUEST)
                .setConfidence(0.8f)
                .setMaxResults(3)
                .setOffline(true)
                .setAwake(false);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Activity overridden methods
    ///////////////////////////////////////////////////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTRO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getPreferences(MODE_PRIVATE).edit().putBoolean(FIRST_TIME, false).apply();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            } else {
                finish();
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
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new DashboardFragment())
                            .commit();
                    break;
                case 2:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new TableManagementFragment()).commit();
                    break;
                case 3:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new SettingsFragment()).commit();
                    break;
                case 4:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new AboutFragment()).commit();
                    break;
            }
            Objects.requireNonNull(getSupportActionBar()).setTitle(windowsTitle[model.getId() - 1]);
            /*mMenu.findItem(R.id.speechAction).setVisible(selectedPage > 0);*/
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
        if (item.getItemId() == R.id.speechAction) {
            if (getPreferences(MODE_PRIVATE).getBoolean(VOICE_NOTE, true)) {
                new AlertDialog.Builder(this)
                        .setTitle("Voice Command")
                        .setMessage("The voice feature is still under development and might stop the application. " +
                                "The record will be added upon hearing the user say for example \"I spend 10$ on food\". " +
                                "The voice command must contain a value and a category. " +
                                "In addition to that, you have to be at its tab of the tables management. " +
                                "Also, the category must be added into Manage Categories in the setting.")
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
        }
        return super.onOptionsItemSelected(item);
    }
}
