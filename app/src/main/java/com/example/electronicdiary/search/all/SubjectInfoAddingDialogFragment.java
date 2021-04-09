package com.example.electronicdiary.search.all;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;

import org.jetbrains.annotations.NotNull;

public class SubjectInfoAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_subject_info_adding, null);

        CheckBox isLecturer = root.findViewById(R.id.subjectInfoIsLecturerAdding);
        CheckBox isSeminarian = root.findViewById(R.id.subjectInfoIsSeminarianAdding);
        CheckBox isExam = root.findViewById(R.id.subjectInfoIsExamAdding);
        CheckBox isDifferentiatedCredit = root.findViewById(R.id.subjectInfoIsDifferentiatedCreditAdding);

        isLecturer.setOnClickListener(view -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isLecturer.isChecked() || isSeminarian.isChecked());
        });

        isSeminarian.setOnClickListener(view -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isLecturer.isChecked() || isSeminarian.isChecked());
        });

        isExam.setOnClickListener(view -> {
            if (isExam.isChecked() && isDifferentiatedCredit.isChecked()) {
                isDifferentiatedCredit.setChecked(false);
            }
        });

        isDifferentiatedCredit.setOnClickListener(view -> {
            if (isExam.isChecked() && isDifferentiatedCredit.isChecked()) {
                isExam.setChecked(false);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        dialog = builder.setView(root)
                .setTitle("Укажите доп. информацию")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    Repository.getInstance().addAvailableSubject(getArguments().getInt("professorId"),
                            isLecturer.isChecked(), isSeminarian.isChecked(), getArguments().getInt("groupId"), getArguments().getInt("subjectId"),
                            getArguments().getInt("semesterId"), isExam.isChecked(), isDifferentiatedCredit.isChecked());

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_subject_info_adding_to_profile);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }
}
