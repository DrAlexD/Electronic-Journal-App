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

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class SubjectAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    private boolean titleIsEmptyFlag = true;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_subject_adding, null);

        EditText subjectTitle = root.findViewById(R.id.subjectTitleAdding);
        subjectTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                titleIsEmptyFlag = s.toString().trim().isEmpty();
                if (titleIsEmptyFlag)
                    subjectTitle.setError("Пустое поле");
                else
                    subjectTitle.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!titleIsEmptyFlag);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите название предмета")
                .setPositiveButton("Подтвердить", (dialog, id) -> {

                    //TODO добавление предмета в базу
                    dismiss();
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(!titleIsEmptyFlag));

        return dialog;
    }
}
