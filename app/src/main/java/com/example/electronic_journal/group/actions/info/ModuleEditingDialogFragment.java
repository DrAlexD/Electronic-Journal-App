package com.example.electronic_journal.group.actions.info;

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
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Module;

import org.jetbrains.annotations.NotNull;

public class ModuleEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private ModuleEditingViewModel moduleEditingViewModel;
    private long moduleId;
    private boolean isFromEventsPerformance;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_module_info_editing, null);

        moduleId = getArguments().getLong("moduleId");
        int moduleNumber = getArguments().getInt("moduleNumber");
        isFromEventsPerformance = getArguments().getBoolean("isFromEventsPerformance");

        moduleEditingViewModel = new ViewModelProvider(this).get(ModuleEditingViewModel.class);
        moduleEditingViewModel.downloadModuleById(moduleId);

        EditText minPoints = root.findViewById(R.id.moduleMinPointsEditing);
        EditText maxPoints = root.findViewById(R.id.moduleMaxPointsEditing);
        maxPoints.setEnabled(false);

        moduleEditingViewModel.getModule().observe(this, module -> {
            if (module != null) {
                minPoints.setText(String.valueOf(module.getMinPoints()));
                maxPoints.setText(String.valueOf(module.getMaxAvailablePoints()));
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                moduleEditingViewModel.moduleEditingDataChanged(minPoints.getText().toString());
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

        moduleEditingViewModel.getModuleFormState().observe(this, moduleFormState -> {
            if (moduleFormState == null) {
                return;
            }

            minPoints.setError(moduleFormState.getMinPointsError() != null ?
                    getString(moduleFormState.getMinPointsError()) : null);
/*            maxPoints.setError(moduleFormState.getMaxPointsError() != null ?
                    getString(moduleFormState.getMaxPointsError()) : null);*/

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(moduleFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить информацию " + moduleNumber + " модуля")
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        EditText minPoints = root.findViewById(R.id.moduleMinPointsEditing);
        EditText maxPoints = root.findViewById(R.id.moduleMaxPointsEditing);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            Module module = moduleEditingViewModel.getModule().getValue();
            moduleEditingViewModel.editModule(moduleId, module.getModuleNumber(), module.getSubjectInfo(),
                    Integer.parseInt(minPoints.getText().toString()), Integer.parseInt(maxPoints.getText().toString()));

            moduleEditingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null && answer != -2) {
                    if (answer == -1) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("subjectInfoId", module.getSubjectInfo().getId());
                        if (isFromEventsPerformance)
                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_module_info_editing_to_group_performance_events, bundle);
                        else
                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_module_info_editing_to_group_performance_lessons, bundle);
                    } else {
                        Toast.makeText(getContext(), "Доступное значение мин. баллов в модуле: " + answer, Toast.LENGTH_LONG).show();
                        moduleEditingViewModel.setAnswer(new MutableLiveData<>());
                    }
                }
            });
        });
    }
}
