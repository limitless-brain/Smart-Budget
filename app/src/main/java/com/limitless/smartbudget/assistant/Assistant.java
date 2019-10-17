/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: Assistant.java                                        ////////
 * ////////Class Name: Assistant                                  ////////
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Locale;

public class Assistant implements RecognitionListener, OnAudioFocusChangeListener {

    //  Constants
    private static final String TAG = Assistant.class.getSimpleName();
    private static final int ASSISTANT_SLEEP = 0;
    private static final int ASSISTANT_AWAKE = 1;
    private final AssistantFragment mAssistantFragment = new AssistantFragment();
    //  State Fields
    private int mState;
    private boolean mHotWordDetected = false;
    private boolean mIsListening = false;
    //  Speech Intent
    private boolean mOnline = true;
    private Locale mLanguage = Locale.US;
    private int mMaxResult;
    private float mConfidence;
    //  Assistant management
    private Handler mRestartHandler = new Handler();
    //  Assistant SpeechRecognizer
    private SpeechRecognizer mSpeechRecognizer;
    //  Assistant Feedback
    private TextToSpeech mVoiceFeedback;
    private TextToSpeech.OnInitListener onVoiceInitListener = new OnInitListener() {
        @Override
        public void onInit(int status) {
            Log.d(TAG, "onVoiceInit + status= " + status);
        }
    };
    private AssistantVisualisationInterface mAssistantVisualListener;
    private AssistantDataInterface mAssistantDataListener;
    //  Activity
    private Activity mActivity;

    /**
     * Constructor to initialize base variables
     *
     * @param activity reference to application context so we can get resources
     */
    public Assistant(Activity activity) {
        //  Initialize speech and voice
        mActivity = activity;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        mSpeechRecognizer.setRecognitionListener(this);
        mVoiceFeedback = new TextToSpeech(mActivity, onVoiceInitListener);
    }

    private void listenToHotWard() {
        //  Check if we already listening
        if (mIsListening)
            return;
        mIsListening = true;
        mSpeechRecognizer.startListening(getIntentForHotWord());
    }

    private Intent getIntentForHotWord() {
        Intent intent = new Intent(RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE);
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, !mOnline);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL
                , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, mLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                Long.valueOf(5000));
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, mMaxResult);
        intent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, mConfidence);
        return intent;
    }

    public Assistant setAwake(boolean awake) {
        mState = awake ? ASSISTANT_AWAKE : ASSISTANT_SLEEP;
        return this;
    }

    /**
     * Enable assistant to work offline Note: only if the language supported
     *
     * @param offline boolean represent the state
     * @return Assistant current instance
     */
    public Assistant setOffline(boolean offline) {
        mOnline = !offline;
        return this;
    }

    public Assistant setLanguage(Locale language) {
        mLanguage = language;
        return this;
    }

    public Assistant setConfidence(float confidence) {
        mConfidence = confidence;
        return this;
    }

    public Assistant setMaxResults(int results) {
        mMaxResult = results;
        return this;
    }

    public Assistant start() {
        //  Check if assistant set to be awake
        if (mState == ASSISTANT_AWAKE) {
            //  Listen to hot-word
            //  Cancel any operation we have
            mSpeechRecognizer.cancel();
            listenToHotWard();
        }
        return this;
    }

    public void setAssistantVisualListener(AssistantVisualisationInterface listener) {
        mAssistantVisualListener = listener;
    }

    public void setAssistantDataListener(AssistantDataInterface listener) {
        mAssistantDataListener = listener;
    }

    public Object listenToRecord(@NonNull FragmentManager fragmentManager) {
        mState = ASSISTANT_SLEEP;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL
                , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mActivity.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, mConfidence);
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, mOnline);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
                6000);
        setAssistantVisualListener(mAssistantFragment);
        mAssistantFragment.show(fragmentManager
                , mAssistantFragment.getTag());
        mSpeechRecognizer.startListening(intent);
        return null;
    }

    private void muteBeepSound(boolean mute) {
        AudioManager audioManager = (AudioManager) mActivity
                .getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null)
            return;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Recognition Listener Methods
    ///////////////////////////////////////////////////////////////////////////


    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        mAssistantVisualListener.onRMSChanged(rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        mAssistantFragment.dismiss();
    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        if (mState == ASSISTANT_AWAKE) {
            mIsListening = false;
            if (!mHotWordDetected) {
                mRestartHandler = new Handler();
                mRestartHandler.postDelayed(this::start, 500);
            } else {
                mState = ASSISTANT_SLEEP;
                mRestartHandler = new Handler();
                mRestartHandler.postDelayed(this::listenToUser, 500);
            }
        } else {
            //  Processing for record
            ArrayList<String> strings = results.
                    getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            RecordModel model = new RecordModel();
            assert strings != null;
            model.parseRecord(strings.get(0), mActivity.getApplicationContext());
            mAssistantDataListener.onReceiveRecord(model);
        }
    }

    private void listenToUser() {
        mVoiceFeedback.speak("Yes, I'm here", TextToSpeech.QUEUE_FLUSH
                , null, null);
        mState = ASSISTANT_AWAKE;
        mHotWordDetected = false;
        start();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        //  Get the partial results
        ArrayList<String> results = partialResults.
                getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        //  Check for hot-word
        /*if (mState == ASSISTANT_AWAKE) {
            if (results == null) return;
            for (String s : results) {
                s = s.toLowerCase();
                mHotWordDetected = s.contains("jenny");
                Log.d(TAG, "onPartialResults: result = " + s);
            }
            if (mHotWordDetected) {
                Toast.makeText(mActivity, "Yes, what i can do", Toast.LENGTH_LONG).show();
                mSpeechRecognizer.stopListening();
            }
        }*/
    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    ///////////////////////////////////////////////////////////////////////////
    // On Audio focus change methods
    ///////////////////////////////////////////////////////////////////////////


    @Override
    public void onAudioFocusChange(int focusChange) {

    }
}
