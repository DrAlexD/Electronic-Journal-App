package com.example.electronicdiary.settings;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Semester;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.downloadSemesters();

        ListPreference semesterChoose = findPreference(getString(R.string.current_semester));
        settingsViewModel.getSemesters().observe(this, semesters -> {
            if (semesters != null) {
                String[] semesterEntries = new String[semesters.size()];
                String[] semesterEntryValues = new String[semesters.size()];

                for (int i = 0; i < semesters.size(); i++) {
                    semesterEntries[i] = semesters.get(i).toString();
                    semesterEntryValues[i] = String.valueOf(semesters.get(i).getId());
                }

                semesterChoose.setEntries(semesterEntries);
                semesterChoose.setEntryValues(semesterEntryValues);
                if (semesterChoose.getValue() == null) {
                    semesterChoose.setValueIndex(semesterEntries.length - 1);
                }

                for (int i = 0; i < semesters.size(); i++) {
                    if (semesterEntryValues[i].equals(semesterChoose.getValue())) {
                        semesterChoose.setSummary(semesterEntries[i]);
                    }
                }
            }
        });

        semesterChoose.setOnPreferenceChangeListener((preference, newValue) -> {
            for (Semester semester : settingsViewModel.getSemesters().getValue()) {
                ListPreference listPreference = (ListPreference) preference;
                String id = String.valueOf(semester.getId());
                String value = newValue.toString();
                if (id.equals(value)) {
                    listPreference.setSummary(semester.toString());
                    listPreference.setValue(value);
                }
            }
            return false;
        });
    }
}