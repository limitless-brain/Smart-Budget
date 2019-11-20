/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: CurrencyPickerFragment.java                                        ////////
 * ////////Class Name: CurrencyPickerFragment                                  ////////
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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyPickerFragment extends SlideFragment {

    //  Tag for debug propose
    private static final String TAG = CurrencyPickerFragment.class.getSimpleName();

    public static final String PREF_KEY_CURRENCY = "currency";
    public static final String PREF_KEY_CURRENCY_SYMBOL = "currency_symbol";

    private static final String[] UNWANTED_CURRENCIES = {"AFA", "ALK", "AOK", "AON"
            , "AOR", "ARL", "ARM", "ARP", "AZM", "BAD", "BAN", "BOL", "BRB", "BRC", "BRE"
            , "BRN", "BRR", "BRZ", "BYB", "BYR", "CSD", "GHC", "ILR", "ISJ", "KRH", "KRO"
            , "MKN", "MXV", "MZM", "NIC", "PES", "PLZ", "ROL", "RUR", "SDD", "SDP", "STN"
            , "TMM", "TRL", "UGS", "USN", "USS", "UTI", "UYP", "VEB", "VNN", "XXX", "YUD"
            , "YUM", "YUN", "YUR", "ZMK", "ZRN", "ZRZ", "ZWD"};

    // State Fields
    private Activity mActivity;
    private Context mContext;
    private ArrayAdapter<String> mListDataAdapter;
    private Button mText;
    private View.OnClickListener onClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View root = inflater.inflate(R.layout.fragment_showcase_currency, container, false);

        //  Currency Picker
        Button chooser = root.findViewById(R.id.showcase_currency_choose_btn);
        mListDataAdapter = new ArrayAdapter<>(mContext
                , R.layout.showcase_currency_list_item
                , android.R.id.text1
                , new ArrayList<>());
        onClickListener = v -> {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(mContext);
            sheetDialog.setContentView(R.layout.showcase_currency_list);
            ListView listView = sheetDialog.findViewById(R.id.showcase_currency_list_view);
            if (listView != null) {
                listView.setAdapter(mListDataAdapter);
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    mText.setText((String) listView.getAdapter().getItem(position));
                    sheetDialog.dismiss();
                });
            }
            sheetDialog.show();
        };
        chooser.setOnClickListener(onClickListener);

        //  Currency result
        mText = root.findViewById(R.id.showcase_currency_text);
        mText.setOnClickListener(onClickListener);

        //  Currency done
        Button done = root.findViewById(R.id.showcase_currency_done_btn);
        done.setOnClickListener(v -> {
            String code = mText.getText().subSequence(0, 3).toString();
            mActivity.getSharedPreferences(Constants.SETTING_PREF_FILE, Context.MODE_PRIVATE).edit()
                    .putString(PREF_KEY_CURRENCY, code)
                    .apply();
            nextSlide();
        });

        generateCurrencyList();
        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utility methods
    ///////////////////////////////////////////////////////////////////////////

    private List<java.util.Currency> getSortedCurrencies() {
        Set<java.util.Currency> currencies = java.util.Currency.getAvailableCurrencies();
        List<java.util.Currency> sortedCurrencies = new ArrayList<>(currencies);
        Collections.sort(sortedCurrencies,
                (o1, o2) -> o1.getCurrencyCode()
                        .compareTo(o2.getCurrencyCode()));
        return sortedCurrencies;
    }


    private void generateCurrencyList() {
        List<String> unwantedCurrencies = Arrays.asList(UNWANTED_CURRENCIES);
        Observable<Currency> observable = Observable
                .fromIterable(getSortedCurrencies())
                .filter(currency -> !unwantedCurrencies.contains(currency.getCurrencyCode()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<Currency>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Currency currency) {
                Log.d(TAG, "onNext: " + currency.getCurrencyCode()
                        + " : " + currency.getDisplayName()
                        + " : " + currency.getSymbol());
                String currencyString = currency.getCurrencyCode() + "\t\t\t\t\t"
                        + currency.getDisplayName();
                mListDataAdapter.add(currencyString);
                if (mText != null && currency.getCurrencyCode().equals("USD"))
                    mText.setText(currencyString);
                mListDataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
