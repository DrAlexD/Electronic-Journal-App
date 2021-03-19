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

public class SubjectAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_subject_adding, null);

        SubjectAddingViewModel subjectAddingViewModel = new ViewModelProvider(this).get(SubjectAddingViewModel.class);

        EditText subjectTitle = root.findViewById(R.id.subjectTitleAdding);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                subjectAddingViewModel.subjectAddingDataChanged(subjectTitle.getText().toString());
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
        subjectTitle.addTextChangedListener(afterTextChangedListener);

        subjectAddingViewModel.getSubjectFormState().observe(this, subjectFormState -> {
            if (subjectFormState == null) {
                return;
            }

            subjectTitle.setError(subjectFormState.getSubjectTitleError() != null ?
                    getString(subjectFormState.getSubjectTitleError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(subjectFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите название предмета")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    subjectAddingViewModel.addSubject(subjectTitle.getText().toString());
                    dismiss();
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }
}
