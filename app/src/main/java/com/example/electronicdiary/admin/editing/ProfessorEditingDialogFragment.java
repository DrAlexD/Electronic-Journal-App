package com.example.electronicdiary.admin.editing;

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
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class ProfessorEditingDialogFragment extends DialogFragment {
    private long professorId;

    private AlertDialog dialog;
    private ProfessorEditingViewModel professorEditingViewModel;

    private EditText professorName;
    private EditText professorSecondName;
    private EditText professorLogin;
    private EditText professorPassword;
    private CheckBox generateCheckBox;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_professor_editing, null);

        professorId = getArguments().getLong("professorId");
        professorEditingViewModel = new ViewModelProvider(this).get(ProfessorEditingViewModel.class);
        professorEditingViewModel.downloadProfessorByIdWithLogin(professorId);

        setupAutoGenerate(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Измените данные преподавателя")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    professorEditingViewModel.editProfessor(professorId, professorName.getText().toString(),
                            professorSecondName.getText().toString(), professorLogin.getText().toString(),
                            professorPassword.getText().toString());

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_professor_editing_to_admin_actions, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }

    private void setupAutoGenerate(View root) {
        generateCheckBox = root.findViewById(R.id.professorEditingGenerate);
        professorName = root.findViewById(R.id.professorNameEditing);
        professorSecondName = root.findViewById(R.id.professorSecondNameEditing);
        professorLogin = root.findViewById(R.id.professorLoginEditing);
        professorPassword = root.findViewById(R.id.professorPasswordEditing);

        professorEditingViewModel.getProfessor().observe(this, professor -> {
            if (professor == null) {
                return;
            }

            professorName.setText(professor.getFirstName());
            professorSecondName.setText(professor.getSecondName());
            professorLogin.setText(professor.getUsername());
            professorPassword.setText(professor.getPassword());
            generateCheckBox.setEnabled(true);
        });

        TextWatcher afterNameAndSecondNameChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                professorEditingViewModel.professorEditingDataChanged(professorName.getText().toString(),
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
                professorEditingViewModel.professorEditingDataChanged(professorName.getText().toString(),
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

        professorEditingViewModel.getProfessorFormState().observe(this, professorFormState -> {
            if (professorFormState == null) {
                return;
            }

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(professorFormState.isDataValid());
            if (professorFormState.isNameOrSecondNameChanged()) {
                professorName.setError(professorFormState.getProfessorNameError() != null ?
                        getString(professorFormState.getProfessorNameError()) : null);
                professorSecondName.setError(professorFormState.getProfessorSecondNameError() != null ?
                        getString(professorFormState.getProfessorSecondNameError()) : null);
                if (professorFormState.getProfessorNameError() == null && professorFormState.getProfessorSecondNameError() == null) {
                    generateCheckBox.setEnabled(true);

                    if (generateCheckBox.isChecked()) {
                        setGeneratedLoginAndPassword();
                    }
                } else {
                    generateCheckBox.setEnabled(false);

                    setEmptyLoginAndPassword();
                }
            } else {
                professorLogin.setError(professorFormState.getProfessorLoginError() != null ?
                        getString(professorFormState.getProfessorLoginError()) : null);
                professorPassword.setError(professorFormState.getProfessorPasswordError() != null ?
                        getString(professorFormState.getProfessorPasswordError()) : null);
            }
        });
    }

    private void setGeneratedLoginAndPassword() {
        professorLogin.setText(professorName.getText().toString().toLowerCase() +
                professorSecondName.getText().toString().toLowerCase() + professorId);
        professorPassword.setText("123456" + professorId);
    }

    private void setEmptyLoginAndPassword() {
        professorLogin.setText("");
        professorPassword.setText("");
    }
}
