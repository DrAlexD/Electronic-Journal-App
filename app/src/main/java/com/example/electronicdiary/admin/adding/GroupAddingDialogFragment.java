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

public class GroupAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_group_adding, null);

        GroupAddingViewModel groupAddingViewModel = new ViewModelProvider(this).get(GroupAddingViewModel.class);

        EditText groupTitle = root.findViewById(R.id.groupTitleAdding);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                groupAddingViewModel.groupAddingDataChanged(groupTitle.getText().toString());
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
        groupTitle.addTextChangedListener(afterTextChangedListener);

        groupAddingViewModel.getGroupAddingFormState().observe(this, groupAddingFormState -> {
            if (groupAddingFormState == null) {
                return;
            }

            groupTitle.setError(groupAddingFormState.getGroupTitleError() != null ?
                    getString(groupAddingFormState.getGroupTitleError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(groupAddingFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите группу")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    groupAddingViewModel.addGroup(groupTitle.getText().toString());
                    dismiss();
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }
}
