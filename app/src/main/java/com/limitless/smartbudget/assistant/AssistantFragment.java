/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: AssistantFragment.java                                        ////////
 * ////////Class Name: AssistantFragment                                  ////////
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

package com.limitless.smartbudget.assistant;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.limitless.smartbudget.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistantFragment extends BottomSheetDialogFragment implements AssistantVisualisationInterface {

    //  State
    float mUserRMS;
    View mAssistantView;

    public AssistantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.assistant_sheet, container, false);
        mAssistantView = new View(getContext()) {
            @Override
            protected void onDraw(Canvas canvas) {
                float height = getHeight();
                float width = getWidth();

                canvas.drawCircle(width / 2, height / 2, Math.abs(mUserRMS) + 20, new Paint());

                super.onDraw(canvas);
            }
        };
        ((RelativeLayout) root).addView(mAssistantView);
        return root;
    }

    @Override
    public void onRMSChanged(float rms) {
        mUserRMS = rms;
        if (mAssistantView != null)
            mAssistantView.invalidate();
    }
}
