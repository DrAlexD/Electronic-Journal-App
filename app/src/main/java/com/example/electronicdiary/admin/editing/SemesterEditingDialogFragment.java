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

public class SemesterEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_semester_editing, null);

        SemesterEditingViewModel semesterEditingViewModel = new ViewModelProvider(this).get(SemesterEditingViewModel.class);
        long semesterId = getArguments().getLong("semesterId");
        semesterEditingViewModel.downloadSemesterById(semesterId);

        EditText semesterYear = root.findViewById(R.id.semesterYearEditing);
        CheckBox isFirstHalf = root.findViewById(R.id.isSemesterFirstHalfEditing);

        semesterEditingViewModel.getSemester().observe(this, semester -> {
            if (semester != null) {
                semesterYear.setText(String.valueOf(semester.getYear()));
                isFirstHalf.setChecked(semester.isFirstHalf());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                semesterEditingViewModel.semesterEditingDataChanged(semesterYear.getText().toString());
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
        semesterYear.addTextChangedListener(afterTextChangedListener);

        semesterEditingViewModel.getSemesterFormState().observe(this, semesterFormState -> {
            if (semesterFormState == null) {
                return;
            }

            semesterYear.setError(semesterFormState.getSemesterYearError() != null ?
                    getString(semesterFormState.getSemesterYearError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(semesterFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Измените данные семестра")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    semesterEditingViewModel.editSemester(semesterId, Integer.parseInt(semesterYear.getText().toString()), isFirstHalf.isChecked());

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_semester_editing_to_admin_actions, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }
}
