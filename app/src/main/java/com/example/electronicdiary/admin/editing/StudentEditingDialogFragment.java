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

public class StudentEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private CheckBox generateCheckBox;
    private EditText studentLogin;
    private EditText studentPassword;
    private EditText studentName;
    private EditText studentSecondName;

    private int studentId;

    private boolean nameIsEmptyFlag = true;
    private boolean secondNameIsEmptyFlag = true;
    private boolean loginIsEmptyFlag = true;
    private boolean passwordIsEmptyFlag = true;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_editing, null);

        downloadData();

        generateCheckBox = root.findViewById(R.id.studentEditingGenerate);
        studentName = root.findViewById(R.id.studentNameEditing);
        studentSecondName = root.findViewById(R.id.studentSecondNameEditing);
        studentLogin = root.findViewById(R.id.studentLoginEditing);
        studentPassword = root.findViewById(R.id.studentPasswordEditing);

        studentName.setText(getArguments().getString("student").split(" ")[0]);
        studentSecondName.setText(getArguments().getString("student").split(" ")[1]);
        nameIsEmptyFlag = false;
        secondNameIsEmptyFlag = false;
        generateCheckBox.setEnabled(true);

        setupAutoGenerate();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Измените данные студента")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    //TODO изменение студента в базе
                    //dismiss();

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_editing_to_admin_actions, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(!nameIsEmptyFlag && !secondNameIsEmptyFlag && !loginIsEmptyFlag && !passwordIsEmptyFlag));

        return dialog;
    }

    private void downloadData() {
        //TODO загрузить по id информацию о студенте
        studentId = new Random().nextInt(1000);
    }

    private void setupAutoGenerate() {
        studentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                nameIsEmptyFlag = s.toString().trim().isEmpty();
                if (nameIsEmptyFlag)
                    studentName.setError("Пустое поле");
                else
                    studentName.setError(null);
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

        studentSecondName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                secondNameIsEmptyFlag = s.toString().trim().isEmpty();
                if (secondNameIsEmptyFlag)
                    studentSecondName.setError("Пустое поле");
                else
                    studentSecondName.setError(null);
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

        studentLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                loginIsEmptyFlag = s.toString().trim().isEmpty();
                if (loginIsEmptyFlag)
                    studentLogin.setError("Пустое поле");
                else
                    studentLogin.setError(null);
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

        studentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                passwordIsEmptyFlag = s.toString().trim().isEmpty();
                if (passwordIsEmptyFlag)
                    studentPassword.setError("Пустое поле");
                else
                    studentPassword.setError(null);
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
        studentLogin.setText(studentName.getText().toString().toLowerCase() +
                studentSecondName.getText().toString().toLowerCase() + studentId);
        studentPassword.setText("123456" + studentId);
    }

    private void setEmptyLoginAndPassword() {
        if (!studentLogin.getText().toString().isEmpty())
            studentLogin.setText("");
        if (!studentPassword.getText().toString().isEmpty())
            studentPassword.setText("");
    }
}
