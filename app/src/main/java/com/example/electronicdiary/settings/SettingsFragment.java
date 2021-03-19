package com.example.electronicdiary.settings;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.electronicdiary.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.downloadSemesters();

        ListPreference semesterChoose = findPreference(getString(R.string.semester_choose));
        settingsViewModel.getSemesters().observe(this, semesters -> {
            String[] semesterEntries = new String[semesters.size()];
            String[] semesterEntryValues = new String[semesters.size()];

            for (int i = 0; i < semesters.size(); i++) {
                semesterEntries[i] = semesters.get(i).toString();
                semesterEntryValues[i] = semesters.get(i).getFirstHalf() + "_half_" + semesters.get(i).getYear();
            }

            semesterChoose.setEntries(semesterEntries);
            semesterChoose.setEntryValues(semesterEntryValues);
            if (semesterChoose.getValue() == null) {
                semesterChoose.setValueIndex(semesterEntries.length - 1);
            }
        });
    }
}