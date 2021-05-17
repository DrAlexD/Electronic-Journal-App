package com.example.electronicdiary.admin.editing;

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
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class GroupEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private GroupEditingViewModel groupEditingViewModel;
    private View root;
    private long groupId;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_group_editing, null);

        groupEditingViewModel = new ViewModelProvider(this).get(GroupEditingViewModel.class);
        groupId = getArguments().getLong("groupId");
        groupEditingViewModel.downloadGroupById(groupId);

        EditText groupTitle = root.findViewById(R.id.groupTitleEditing);

        groupEditingViewModel.getGroup().observe(this, group -> {
            if (group != null) {
                groupTitle.setText(group.getTitle());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                groupEditingViewModel.groupEditingDataChanged(groupTitle.getText().toString());
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

        groupEditingViewModel.getGroupFormState().observe(this, groupFormState -> {
            if (groupFormState == null) {
                return;
            }

            groupTitle.setError(groupFormState.getGroupTitleError() != null ?
                    getString(groupFormState.getGroupTitleError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(groupFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Измените название группы")
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        EditText groupTitle = root.findViewById(R.id.groupTitleEditing);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            groupEditingViewModel.editGroup(groupId, groupTitle.getText().toString());

            groupEditingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_group_editing_to_admin_actions, bundle);
                }
            });
        });
    }
}
