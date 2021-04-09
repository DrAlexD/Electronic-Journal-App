package com.example.electronicdiary.group.actions.info;

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

public class ModuleInfoEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_module_info_editing, null);

        int moduleNumber = getArguments().getInt("moduleNumber");
        int groupId = getArguments().getInt("groupId");
        int subjectId = getArguments().getInt("subjectId");
        int lecturerId = getArguments().getInt("lecturerId");
        int seminarianId = getArguments().getInt("seminarianId");
        int semesterId = getArguments().getInt("semesterId");

        ModuleInfoEditingViewModel moduleInfoEditingViewModel = new ViewModelProvider(this).get(ModuleInfoEditingViewModel.class);
        moduleInfoEditingViewModel.downloadModuleInfoByNumber(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId);

        EditText minPoints = root.findViewById(R.id.moduleInfoMinPointsEditing);
        EditText maxPoints = root.findViewById(R.id.moduleInfoMaxPointsEditing);
        moduleInfoEditingViewModel.getModuleInfo().observe(this, moduleInfo -> {
            if (moduleInfo == null) {
                return;
            }

            minPoints.setText(String.valueOf(moduleInfo.getMinPoints()));
            maxPoints.setText(String.valueOf(moduleInfo.getMaxPoints()));
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                moduleInfoEditingViewModel.moduleInfoEditingDataChanged(minPoints.getText().toString(), maxPoints.getText().toString());
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
        minPoints.addTextChangedListener(afterTextChangedListener);
        maxPoints.addTextChangedListener(afterTextChangedListener);

        moduleInfoEditingViewModel.getModuleInfoFormState().observe(this, moduleInfoFormState -> {
            if (moduleInfoFormState == null) {
                return;
            }

            minPoints.setError(moduleInfoFormState.getMinPointsError() != null ?
                    getString(moduleInfoFormState.getMinPointsError()) : null);
            maxPoints.setError(moduleInfoFormState.getMaxPointsError() != null ?
                    getString(moduleInfoFormState.getMaxPointsError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(moduleInfoFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить успеваемость в " + moduleInfoEditingViewModel.getModuleInfo().getValue().getModuleNumber() + " модуле")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    moduleInfoEditingViewModel.editModuleInfo(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId,
                            Integer.parseInt(minPoints.getText().toString()), Integer.parseInt(maxPoints.getText().toString()));

                    dismiss();
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }
}
