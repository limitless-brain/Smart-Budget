/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: AppIntro.java                                        ////////
 * ////////Class Name: AppIntro                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 1/30/21 7:57 AM                                       ////////
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

import androidx.viewpager.widget.ViewPager;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class AppIntro extends IntroActivity implements ViewPager.OnPageChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);

        //  First Slide
        addSlide(
                new SimpleSlide.Builder()
                        .canGoBackward(false)
                        .title(R.string.intro_slide_introduction_description)
                        .background(R.color.colorPrimary)
                        .image(R.mipmap.ic_launcher_round)
                        .build());
        /*//  Permission Slide
        addSlide(
                new SimpleSlide.Builder()
                        .canGoBackward(false)
                        .title(R.string.app_name)
                        .description(R.string.intro_slide1_description)
                        .image(R.mipmap.ic_launcher_round)
                        .build());*/
        //  Last Slide
        addSlide(
                new SimpleSlide.Builder()
                        .canGoBackward(false)
                        .title("Sign In")
                        .description("Keep your data safe by store it online")
                        .background(R.color.colorPrimary)
                        .image(R.mipmap.ic_launcher_round)
                        .build());

        setButtonBackVisible(false);
        addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:

                break;
            case 1:

                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
