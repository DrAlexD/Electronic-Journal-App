package com.example.electronic_journal.admin.adding;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.electronic_journal.R;
import com.example.electronic_journal.Result;

import org.jetbrains.annotations.NotNull;

public class SemesterAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private SemesterAddingViewModel semesterAddingViewModel;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_semester_adding, null);

        semesterAddingViewModel = new ViewModelProvider(this).get(SemesterAddingViewModel.class);

        EditText semesterYear = root.findViewById(R.id.semesterYearAdding);
        RadioButton isFirstHalf = root.findViewById(R.id.isSemesterFirstHalfAdding);
        RadioButton isSecondHalf = root.findViewById(R.id.isSemesterSecondHalfAdding);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                semesterAddingViewModel.semesterAddingDataChanged(semesterYear.getText().toString(), isFirstHalf.isChecked(),
                        isSecondHalf.isChecked());
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

        isFirstHalf.setOnCheckedChangeListener((compoundButton, b) ->
                semesterAddingViewModel.semesterAddingDataChanged(semesterYear.getText().toString(),
                        isFirstHalf.isChecked(), isSecondHalf.isChecked()));

        isSecondHalf.setOnCheckedChangeListener((compoundButton, b) ->
                semesterAddingViewModel.semesterAddingDataChanged(semesterYear.getText().toString(),
                        isFirstHalf.isChecked(), isSecondHalf.isChecked()));

        semesterAddingViewModel.getSemesterFormState().observe(this, semesterFormState -> {
            if (semesterFormState == null) {
                return;
            }

            semesterYear.setError(semesterFormState.getSemesterYearError() != null ?
                    getString(semesterFormState.getSemesterYearError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(semesterFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные семестра")
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        EditText semesterYear = root.findViewById(R.id.semesterYearAdding);
        RadioButton isFirstHalf = root.findViewById(R.id.isSemesterFirstHalfAdding);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            semesterAddingViewModel.addSemester(Integer.parseInt(semesterYear.getText().toString()), isFirstHalf.isChecked());
            semesterAddingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    if (answer instanceof Result.Success) {
                        Toast.makeText(getContext(), "Успешное добавление семестра", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        String error = ((Result.Error) answer).getError();
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        semesterAddingViewModel.setAnswer(new MutableLiveData<>());
                    }
                }
            });
        });
    }
}
