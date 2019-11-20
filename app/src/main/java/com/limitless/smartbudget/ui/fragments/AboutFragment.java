/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: AboutFragment.java                                        ////////
 * ////////Class Name: AboutFragment                                  ////////
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

package com.limitless.smartbudget.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.limitless.smartbudget.R;
import com.limitless.smartbudget.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView yazanProfile = view.findViewById(R.id.aboutMeImage);
        Picasso.get().load(Uri.parse(Constants.YAZAN_PIC_URL))
                .into(yazanProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (isVisible())
                            Constants.roundPicture(yazanProfile, Objects.requireNonNull(getContext()));
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        ImageView anasProfile = view.findViewById(R.id.aboutAnasImage);
        Picasso.get().load(Uri.parse(Constants.ANAS_PIC_URL))
                .into(anasProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (isVisible())
                            Constants.roundPicture(anasProfile, Objects.requireNonNull(getContext()));
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }
}
