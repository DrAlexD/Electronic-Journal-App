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

public class GroupAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    private boolean titleIsLessFlag = true;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_group_adding, null);

        EditText groupTitle = root.findViewById(R.id.groupTitleAdding);
        groupTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                titleIsLessFlag = s.toString().trim().length() < 5;
                if (titleIsLessFlag)
                    groupTitle.setError("Группа должна содежать >4 символов");
                else
                    groupTitle.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!titleIsLessFlag);
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
                .setTitle("Введите группу")
                .setPositiveButton("Подтвердить", (dialog, id) -> {

                    //TODO добавление группы в базу
                    dismiss();
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(!titleIsLessFlag));

        return dialog;
    }
}
