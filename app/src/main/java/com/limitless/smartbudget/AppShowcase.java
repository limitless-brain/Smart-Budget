/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: AppShowcase.java                                        ////////
 * ////////Class Name: AppShowcase                                  ////////
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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.IdpResponse;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.limitless.smartbudget.ui.fragments.Showcase.CurrencyPickerFragment;
import com.limitless.smartbudget.ui.fragments.Showcase.PermissionFragment;
import com.limitless.smartbudget.ui.fragments.Showcase.ShowcaseFragment;
import com.limitless.smartbudget.ui.fragments.Showcase.SignInFragment;
import com.limitless.smartbudget.utils.Constants;

public class AppShowcase extends IntroActivity {

    public static final int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //  Set the intro activity to be full screen
        setFullscreen(true);
        super.onCreate(savedInstanceState);

        //  Hide back/next buttons
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        //  Hide page inductor
        setPagerIndicatorVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .canGoBackward(false)
                .fragment(ShowcaseFragment.newInstance(R.layout.fragment_showcase_intro))
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .canGoBackward(false)
                .fragment(new PermissionFragment())
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .canGoBackward(false)
                .fragment(new CurrencyPickerFragment())
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .canGoBackward(false)
                .fragment(new SignInFragment())
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .canGoBackward(false)
                .fragment(ShowcaseFragment.newInstance(R.layout.fragment_showcase_done))
                .build());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //  Check if we received the result
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            //  Check whether we get the permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  Granted so we go to the next slide
                nextSlide();
            } else {
                //  Denied so we inform the user
                Toast.makeText(this, "Sorry, you can't use the app " +
                        "without audio record permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
