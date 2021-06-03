package com.example.electronic_journal.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Semester;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.downloadSemesters();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String role = sharedPreferences.getString("userRole", "");

        SwitchPreferenceCompat isProfessorRules = findPreference(getString(R.string.is_professor_rules));
        isProfessorRules.setEnabled(role.equals("ROLE_ADMIN") || role.equals("ROLE_PROFESSOR"));

        SwitchPreferenceCompat isAdminRules = findPreference(getString(R.string.is_admin_rules));
        isAdminRules.setEnabled(role.equals("ROLE_ADMIN"));

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