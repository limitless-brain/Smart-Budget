/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: MainActivity.java                                        ////////
 * ////////Class Name: MainActivity                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 9/26/19 12:52 PM                                       ////////
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

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.limitless.smartbudget.interfaces.OnControlPanelListener;
import com.limitless.smartbudget.ui.FullscreenFragment;
import com.limitless.smartbudget.ui.SettingsFragment;
import com.limitless.smartbudget.ui.TableManagementFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    String[] windowsTitle = {
            "Dashboard",
            "Tables Management",
            "Setting",
            "About"
    };
    ControlPanel panel;
    FragmentManager fragmentManager;
    private OnControlPanelListener mOnControlPanelListener;
    //  UI
    private MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
    }

    private void initUi() {

        Log.i(MainActivity.class.getSimpleName(), "Hello");
        fragmentManager = getSupportFragmentManager();
        bottomNavigation = findViewById(R.id.meow_bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, android.R.drawable.ic_menu_today));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, android.R.drawable.ic_menu_gallery));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, android.R.drawable.ic_menu_preferences));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, android.R.drawable.ic_menu_info_details));
        bottomNavigation.setCount(2, "22");
        bottomNavigation.show(1, true);

        bottomNavigation.setOnShowListener(model -> null);

        bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                // TODO: 9/22/2019 Switch between views
                case 1:
                case 3:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new SettingsFragment()).commit();
                    break;
                case 2:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new TableManagementFragment()).commit();
                    break;
                case 4:
                    fragmentManager.beginTransaction().
                            replace(R.id.fragment_layout, new FullscreenFragment()).commit();
                    break;
            }
            Objects.requireNonNull(getSupportActionBar()).setTitle(windowsTitle[model.getId() - 1]);
            return null;
        });
    }

    public void setControlPanelListener(OnControlPanelListener listener) {
        mOnControlPanelListener = listener;
    }
}
