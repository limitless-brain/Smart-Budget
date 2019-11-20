/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: SignInFragment.java                                        ////////
 * ////////Class Name: SignInFragment                                  ////////
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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.firebase.FirebaseManager;
import com.limitless.smartbudget.utils.Constants;

import static android.app.Activity.RESULT_OK;

public class SignInFragment extends SlideFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_showcase_sign_in, container, false);

        if (FirebaseManager.getInstance().isSignedIn()) {
            nextSlide();
            return root;
        }
        Button signIn = root.findViewById(R.id.showcase_signIn_btn);
        signIn.setOnClickListener(v -> startActivityForResult(FirebaseManager.getInstance().getSignInIntent()
                , Constants.FIREBASE_REQUEST_CODE));
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  Check if we received firebase request
        if(requestCode == Constants.FIREBASE_REQUEST_CODE)
        {
            //  Check if user is signed in
            if(resultCode == RESULT_OK)
                nextSlide();
        }
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }

    @Override
    public boolean canGoForward() {
        return FirebaseManager.getInstance().isSignedIn();
    }
}
