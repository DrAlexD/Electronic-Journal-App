package com.example.electronic_journal.admin.adding;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.electronic_journal.R;
import com.example.electronic_journal.Result;

import org.jetbrains.annotations.NotNull;

public class SubjectAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private SubjectAddingViewModel subjectAddingViewModel;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_subject_adding, null);

        subjectAddingViewModel = new ViewModelProvider(this).get(SubjectAddingViewModel.class);

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
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        EditText subjectTitle = root.findViewById(R.id.subjectTitleAdding);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            {
                subjectAddingViewModel.addSubject(subjectTitle.getText().toString());
                subjectAddingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                    if (answer != null) {
                        if (answer instanceof Result.Success) {
                            Toast.makeText(getContext(), "Успешное добавление предмета", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            String error = ((Result.Error) answer).getError();
                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                            subjectAddingViewModel.setAnswer(new MutableLiveData<>());
                        }
                    }
                });
            }
        });
    }
}
