package com.example.electronicdiary.admin.adding;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class ProfessorAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private ProfessorAddingViewModel professorAddingViewModel;

    private EditText professorName;
    private EditText professorSecondName;
    private EditText professorLogin;
    private EditText professorPassword;
    private CheckBox generateCheckBox;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_professor_adding, null);

        professorAddingViewModel = new ViewModelProvider(this).get(ProfessorAddingViewModel.class);
        professorAddingViewModel.downloadLastProfessorId();

        setupAutoGenerate(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные преподавателя")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    professorAddingViewModel.addProfessor(professorName.getText().toString(),
                            professorSecondName.getText().toString(), professorLogin.getText().toString(),
                            professorPassword.getText().toString());
                    dismiss();
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }

    private void setupAutoGenerate(View root) {
        generateCheckBox = root.findViewById(R.id.professorAddingGenerate);
        professorName = root.findViewById(R.id.professorNameAdding);
        professorSecondName = root.findViewById(R.id.professorSecondNameAdding);
        professorLogin = root.findViewById(R.id.professorLoginAdding);
        professorPassword = root.findViewById(R.id.professorPasswordAdding);

        TextWatcher afterNameAndSecondNameChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                professorAddingViewModel.professorAddingDataChanged(professorName.getText().toString(),
                        professorSecondName.getText().toString(), professorLogin.getText().toString(),
                        professorPassword.getText().toString(), true);
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

        TextWatcher afterLoginAndPasswordChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                professorAddingViewModel.professorAddingDataChanged(professorName.getText().toString(),
                        professorSecondName.getText().toString(), professorLogin.getText().toString(),
                        professorPassword.getText().toString(), false);
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

        professorName.addTextChangedListener(afterNameAndSecondNameChangedListener);
        professorSecondName.addTextChangedListener(afterNameAndSecondNameChangedListener);
        professorLogin.addTextChangedListener(afterLoginAndPasswordChangedListener);
        professorPassword.addTextChangedListener(afterLoginAndPasswordChangedListener);
        generateCheckBox.setOnClickListener(view -> {
            if (generateCheckBox.isChecked()) {
                setGeneratedLoginAndPassword();
            } else {
                setEmptyLoginAndPassword();
            }
        });

        professorAddingViewModel.getProfessorAddingFormState().observe(this, professorAddingFormState -> {
            if (professorAddingFormState == null) {
                return;
            }

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(professorAddingFormState.isDataValid());
            if (professorAddingFormState.isNameOrSecondNameChanged()) {
                professorName.setError(professorAddingFormState.getProfessorNameError() != null ?
                        getString(professorAddingFormState.getProfessorNameError()) : null);
                professorSecondName.setError(professorAddingFormState.getProfessorSecondNameError() != null ?
                        getString(professorAddingFormState.getProfessorSecondNameError()) : null);
                if (professorAddingFormState.getProfessorNameError() == null && professorAddingFormState.getProfessorSecondNameError() == null) {
                    generateCheckBox.setEnabled(true);

                    if (generateCheckBox.isChecked()) {
                        setGeneratedLoginAndPassword();
                    }
                } else {
                    generateCheckBox.setEnabled(false);

                    setEmptyLoginAndPassword();
                }
            } else {
                professorLogin.setError(professorAddingFormState.getProfessorLoginError() != null ?
                        getString(professorAddingFormState.getProfessorLoginError()) : null);
                professorPassword.setError(professorAddingFormState.getProfessorPasswordError() != null ?
                        getString(professorAddingFormState.getProfessorPasswordError()) : null);
            }
        });
    }

    private void setGeneratedLoginAndPassword() {
        professorLogin.setText(professorName.getText().toString().toLowerCase() +
                professorSecondName.getText().toString().toLowerCase() + professorAddingViewModel.getLastProfessorId().getValue().toString());
        professorPassword.setText("123456" + professorAddingViewModel.getLastProfessorId().getValue().toString());
    }

    private void setEmptyLoginAndPassword() {
        professorLogin.setText("");
        professorPassword.setText("");
    }
}
