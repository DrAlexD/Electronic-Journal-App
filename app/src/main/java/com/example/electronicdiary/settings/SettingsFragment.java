package com.example.electronicdiary.settings;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Semester;

import java.util.ArrayList;

public class SettingsFragment extends PreferenceFragmentCompat {
    private ArrayList<Semester> semesters;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        downloadData();

        String[] semesterEntries = new String[semesters.size()];
        String[] semesterEntryValues = new String[semesters.size()];

        for (int i = 0; i < semesters.size(); i++) {
            semesterEntries[i] = semesters.get(i).toString();
            semesterEntryValues[i] = semesters.get(i).getFirstHalf() + "_half_" + semesters.get(i).getYear();
        }

        ListPreference semesterChoose = findPreference(getString(R.string.semester_choose));
        semesterChoose.setEntries(semesterEntries);
        semesterChoose.setEntryValues(semesterEntryValues);
        if (semesterChoose.getValue() == null) {
            semesterChoose.setValueIndex(semesterEntries.length - 1);
        }

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

    private void downloadData() {
        //TODO поиск всех семестров
        semesters = new ArrayList<>();
        semesters.add(new Semester(2018, true));
        semesters.add(new Semester(2018, false));
        semesters.add(new Semester(2019, true));
        semesters.add(new Semester(2019, false));
        semesters.add(new Semester(2020, true));
        semesters.add(new Semester(2020, false));
        semesters.add(new Semester(2021, true));
    }
}