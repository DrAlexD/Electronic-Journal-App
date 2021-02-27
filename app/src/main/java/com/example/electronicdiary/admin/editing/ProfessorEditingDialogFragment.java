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
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ProfessorEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private CheckBox generateCheckBox;
    private EditText professorLogin;
    private EditText professorPassword;
    private EditText professorName;
    private EditText professorSecondName;

    private int professorId;

    private boolean nameIsEmptyFlag = true;
    private boolean secondNameIsEmptyFlag = true;
    private boolean loginIsEmptyFlag = true;
    private boolean passwordIsEmptyFlag = true;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_professor_editing, null);

        downloadData();

        generateCheckBox = root.findViewById(R.id.professorEditingGenerate);
        professorLogin = root.findViewById(R.id.professorLoginEditing);
        professorPassword = root.findViewById(R.id.professorPasswordEditing);
        professorName = root.findViewById(R.id.professorNameEditing);
        professorSecondName = root.findViewById(R.id.professorSecondNameEditing);

        professorName.setText(getArguments().getString("professor").split(" ")[0]);
        professorSecondName.setText(getArguments().getString("professor").split(" ")[1]);

        setupAutoGenerate();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные преподавателя")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    //TODO изменение преподавателя в базе
                    //dismiss();

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_search_professors_to_admin_actions, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(!nameIsEmptyFlag && !secondNameIsEmptyFlag && !loginIsEmptyFlag && !passwordIsEmptyFlag));

        return dialog;
    }

    private void downloadData() {
        //TODO загрузить по id информацию о преподавателе
        professorId = new Random().nextInt(1000);
    }

    private void setupAutoGenerate() {
        professorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                nameIsEmptyFlag = s.toString().trim().isEmpty();
                if (nameIsEmptyFlag)
                    professorName.setError("Пустое поле");
                else
                    professorName.setError(null);
                afterNameAndSecondNameChanges();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        });

        professorSecondName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                secondNameIsEmptyFlag = s.toString().trim().isEmpty();
                if (secondNameIsEmptyFlag)
                    professorSecondName.setError("Пустое поле");
                else
                    professorSecondName.setError(null);
                afterNameAndSecondNameChanges();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        });

        professorLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                loginIsEmptyFlag = s.toString().trim().isEmpty();
                if (loginIsEmptyFlag)
                    professorLogin.setError("Пустое поле");
                else
                    professorLogin.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!nameIsEmptyFlag &&
                        !secondNameIsEmptyFlag && !loginIsEmptyFlag && !passwordIsEmptyFlag);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        });

        professorPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                passwordIsEmptyFlag = s.toString().trim().isEmpty();
                if (passwordIsEmptyFlag)
                    professorPassword.setError("Пустое поле");
                else
                    professorPassword.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!nameIsEmptyFlag &&
                        !secondNameIsEmptyFlag && !loginIsEmptyFlag && !passwordIsEmptyFlag);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        });

        generateCheckBox.setOnClickListener(view -> {
            if (generateCheckBox.isChecked()) {
                setGeneratedLoginAndPassword();
            } else {
                setEmptyLoginAndPassword();
            }
        });
    }

    private void afterNameAndSecondNameChanges() {
        if (!nameIsEmptyFlag && !secondNameIsEmptyFlag) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!loginIsEmptyFlag && !passwordIsEmptyFlag);
            generateCheckBox.setEnabled(true);

            if (generateCheckBox.isChecked()) {
                setGeneratedLoginAndPassword();
            }
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            generateCheckBox.setEnabled(false);

            setEmptyLoginAndPassword();
        }
    }

    private void setGeneratedLoginAndPassword() {
        professorLogin.setText(professorName.getText().toString().toLowerCase() +
                professorSecondName.getText().toString().toLowerCase() + professorId);
        professorPassword.setText("123456" + professorId);
    }

    private void setEmptyLoginAndPassword() {
        if (!professorLogin.getText().toString().isEmpty())
            professorLogin.setText("");
        if (!professorPassword.getText().toString().isEmpty())
            professorPassword.setText("");
    }
}
