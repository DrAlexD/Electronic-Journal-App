package com.example.electronicdiary.admin.adding;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class SemesterAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_semester_adding, null);

        SemesterAddingViewModel semesterAddingViewModel = new ViewModelProvider(this).get(SemesterAddingViewModel.class);

        EditText semesterYear = root.findViewById(R.id.semesterYearAdding);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                semesterAddingViewModel.semesterAddingDataChanged(semesterYear.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        };
        semesterYear.addTextChangedListener(afterTextChangedListener);

        semesterAddingViewModel.getSemesterAddingFormState().observe(this, semesterAddingFormState -> {
            if (semesterAddingFormState == null) {
                return;
            }

            semesterYear.setError(semesterAddingFormState.getSemesterYearError() != null ?
                    getString(semesterAddingFormState.getSemesterYearError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(semesterAddingFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные семестра")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    semesterAddingViewModel.addSemester(semesterYear.getText().toString());
                    dismiss();
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }
}
