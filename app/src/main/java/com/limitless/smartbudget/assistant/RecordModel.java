/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: RecordModel.java                                        ////////
 * ////////Class Name: RecordModel                                  ////////
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

package com.limitless.smartbudget.assistant;

import android.content.Context;
import android.util.Log;

import com.limitless.smartbudget.ControlPanel;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represent a language model for a record
 */
public class RecordModel {
    public static final int OPERATION_NONE = -1;
    public static final int OPERATION_ADD = 0;
    public static final int OPERATION_UPDATE = 1;
    // TODO: 10/3/2019 add more models
    private static final String[] RECORD_OPERATION_ADD = {"add", "spent", "spend", "paid"
            , "got", "receive"};
    private static final String[] RECORD_OPERATION_UPDATE = {"modify", "update", "edit"};
    private static final String[] LINKING_WORD = {"on", "for", "in", "at"};
    private static final String TAG = RecordModel.class.getSimpleName();
    private String mValue = "";
    private List<String> mDescription = new ArrayList<>();
    private ArrayList<String> mCategories = new ArrayList<>();
    private List mCategoriesObjects;
    private Category mCategory = null;
    private int mOperation = OPERATION_NONE;

    public RecordModel parseRecord(String record, Context context) {
        //  Get available categories
        getAvailableCategory(context);
        //  First create a list array with each word
        ArrayList<String> words = createWordList(record);
        if (words == null) return this;

        //  Define operation
        boolean hasOperation = searchForOperation(words);

        //  Search for category inside the record
        boolean hasCategory = searchForCategory(words);

        //  Search for value
        boolean hasValue = searchForValue(words);

        //  Search for description
        boolean hasDescription = searchForDescription(words);

        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Parsing data to extract information
    ///////////////////////////////////////////////////////////////////////////
    private boolean searchForDescription(ArrayList<String> words) {

        boolean hasDescription = false;
        int linkIndex = -1;

        //  Remove linking words
        for (String word : words) {
            for (String link : LINKING_WORD) {
                if (word.toLowerCase().equals(link)) {
                    hasDescription = true;
                    linkIndex = words.indexOf(word);
                    break;
                }
            }
            if (hasDescription)
                break;
        }
        if (linkIndex != -1) {
            //  Add the rest for description
            mDescription = words.subList(linkIndex + 1, words.size());
        }
        Log.i(TAG, "searchForDescription state = " + hasDescription
                + " linkingIndex = " + linkIndex + " description = " + mDescription);
        return hasDescription;
    }

    private boolean searchForOperation(ArrayList<String> words) {
        boolean hasOperation = false;
        for (String word : words) {
            for (String op : RECORD_OPERATION_ADD) {
                if (word.toLowerCase().equals(op)) {
                    hasOperation = true;
                    mOperation = OPERATION_ADD;
                    break;
                }
            }
            for (String op : RECORD_OPERATION_UPDATE) {
                if (word.toLowerCase().equals(op)) {
                    hasOperation = true;
                    mOperation = OPERATION_UPDATE;
                    break;
                }
            }
            Log.i(TAG, "searchForOperation has = " + hasOperation + " word = " + word);
            if (hasOperation)
                break;
        }
        return hasOperation;
    }

    private boolean searchForValue(ArrayList<String> words) {
        boolean found = false;
        for (String word : words) {

            word = word.replaceAll("[\\p{Sc},]", "");
            found = word.matches("\\d+(\\.\\d+)?");
            if (found) {
                mValue = word;
                break;
            }
        }
        Log.i(TAG, "Search for value " + (found ? "success" : "failed")
                + " value = " + mValue);
        return found;
    }

    private boolean searchForCategory(ArrayList<String> words) {
        boolean success = false;

        //  iterate over the list for a category
        for (String currentWord : words) {
            for (String category : mCategories) {
                if (currentWord.toLowerCase().equals(category.toLowerCase())) {
                    Log.i(TAG, "Category found = " + category);
                    int i = mCategories.indexOf(category);
                    mCategory = (Category) mCategoriesObjects.get(i);
                    words.remove(currentWord);
                    success = true;
                    break;
                }
            }
            if (success)
                break;
        }
        return success;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Handling income data and create lists
    ///////////////////////////////////////////////////////////////////////////
    private ArrayList<String> createWordList(String record) {

        if (record.isEmpty()) return null;

        //  Start by detect words
        return new ArrayList<>(Arrays.asList(record.split("[ ]")));
    }

    private void getAvailableCategory(Context context) {
        mCategoriesObjects = (List) ControlPanel.getInstance(context).getAllRecords(Constants.CATEGORIES);
        for (Object o : mCategoriesObjects) {
            mCategories.add(((Category) o).getName());
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters of this model
    ///////////////////////////////////////////////////////////////////////////

    public boolean hasOperation() {
        return mOperation != OPERATION_NONE;
    }

    public boolean hasDescription() {
        return !mDescription.isEmpty();
    }

    public boolean hasValue() {
        return !mValue.isEmpty();
    }

    public boolean hasCategory() {
        return mCategory != null;
    }

    public Category getCategory() {
        return mCategory;
    }

    public String getValue() {
        return mValue;
    }

    public String getDescription() {
        StringBuilder result = new StringBuilder();
        for (String word : mDescription) {
            result.append(word).append(" ");
        }
        return result.toString();
    }

    public int getOperation() {
        return mOperation;
    }
}
