/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: PermissionFragment.java                                        ////////
 * ////////Class Name: PermissionFragment                                  ////////
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

package com.limitless.smartbudget.ui.fragments.Showcase;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.limitless.smartbudget.AppShowcase;
import com.limitless.smartbudget.R;

import java.util.Objects;

public class PermissionFragment extends SlideFragment {

    //  State fields
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get base context
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_showcase_permission, container, false);

        if (isPermissionGranted())
        {
            nextSlide();
            return root;
        }

        Button grant = root.findViewById(R.id.showcase_permission_button);
        grant.setOnClickListener(v -> askForPermission());

        return root;
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity())
                , new String[]{Manifest.permission.RECORD_AUDIO}, AppShowcase.PERMISSIONS_REQUEST_CODE);
    }

    /**
     * Check for audio permission
     *
     * @return permission state
     */
    private boolean isPermissionGranted() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }

    @Override
    public boolean canGoForward() {
        return isPermissionGranted();
    }
}
