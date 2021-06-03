package com.example.electronic_journal.group.actions.info;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.SubjectInfo;

import org.jetbrains.annotations.NotNull;

public class SubjectInfoEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private long subjectInfoId;
    private SubjectInfoEditingViewModel subjectInfoEditingViewModel;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_subject_info_editing, null);

        subjectInfoId = getArguments().getLong("subjectInfoId");

        subjectInfoEditingViewModel = new ViewModelProvider(this).get(SubjectInfoEditingViewModel.class);
        subjectInfoEditingViewModel.downloadSubjectInfo(subjectInfoId);

        RadioButton isExam = root.findViewById(R.id.subjectInfoIsExamEditing);
        RadioButton isDifferentiatedCredit = root.findViewById(R.id.subjectInfoIsDifferentiatedCreditEditing);
        RadioButton isCredit = root.findViewById(R.id.subjectInfoIsCreditEditing);

        subjectInfoEditingViewModel.getSubjectInfo().observe(this, subjectInfo -> {
            if (subjectInfo != null) {
                if (subjectInfo.isExam())
                    isExam.setChecked(true);
                else if (subjectInfo.isDifferentiatedCredit())
                    isDifferentiatedCredit.setChecked(true);
                else
                    isCredit.setChecked(true);
            }
        });

        isExam.setOnCheckedChangeListener((compoundButton, b) -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(isExam.isChecked() || isDifferentiatedCredit.isChecked() || isCredit.isChecked())
        );

        isDifferentiatedCredit.setOnCheckedChangeListener((compoundButton, b) -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(isExam.isChecked() || isDifferentiatedCredit.isChecked() || isCredit.isChecked())
        );

        isCredit.setOnCheckedChangeListener((compoundButton, b) -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(isExam.isChecked() || isDifferentiatedCredit.isChecked() || isCredit.isChecked())
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить данные по предмету")
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        RadioButton isExam = root.findViewById(R.id.subjectInfoIsExamEditing);
        RadioButton isDifferentiatedCredit = root.findViewById(R.id.subjectInfoIsDifferentiatedCreditEditing);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            SubjectInfo subjectInfo = subjectInfoEditingViewModel.getSubjectInfo().getValue();

            subjectInfoEditingViewModel.editSubjectInfo(subjectInfoId, subjectInfo.getGroup(), subjectInfo.getSubject(),
                    subjectInfo.getLecturerId(), subjectInfo.getSeminarsProfessor(), subjectInfo.getSemester(),
                    isExam.isChecked(), isDifferentiatedCredit.isChecked());

            subjectInfoEditingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("subjectInfoId", subjectInfoId);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_subject_info_editing_to_group_performance_events, bundle);
                }
            });
        });
    }
}
