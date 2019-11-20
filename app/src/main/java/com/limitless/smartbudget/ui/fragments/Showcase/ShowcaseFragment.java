/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: ShowcaseFragment.java                                        ////////
 * ////////Class Name: ShowcaseFragment                                  ////////
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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.limitless.smartbudget.MainActivity;
import com.limitless.smartbudget.R;

public class ShowcaseFragment extends SlideFragment {

    private static final String TAG = ShowcaseFragment.class.getSimpleName();

    private static final String LAYOUT_RES_ID = "LayoutRes_Id";
    private int mLayoutResId;

    public static ShowcaseFragment newInstance(int layoutResId) {
        ShowcaseFragment showcaseFragment = new ShowcaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_RES_ID, layoutResId);
        showcaseFragment.setArguments(bundle);
        Log.d(TAG, "newInstance: fragment created!");
        return showcaseFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(LAYOUT_RES_ID)) {
            mLayoutResId = getArguments().getInt(LAYOUT_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutResId, container, false);

        //  For done slide
        TextView doneText = view.findViewById(R.id.showcase_done_get_started);
        if(doneText != null)
        {
            doneText.setOnClickListener(v -> {
                nextSlide();
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            if (isVisible() && mLayoutResId == R.layout.fragment_showcase_intro)
                nextSlide();
        }, 3000);
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }
}
