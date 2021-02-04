package com.example.electronicdiary.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.electronicdiary.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_screen, rootKey);

        /*EditTextPreference numberPreference = findPreference("number");

        if (numberPreference != null) {
            numberPreference.setOnBindEditTextListener(
                    new EditTextPreference.OnBindEditTextListener() {
                        @Override
                        public void onBindEditText(@NonNull EditText editText) {
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                    });
        }*/
    }
}