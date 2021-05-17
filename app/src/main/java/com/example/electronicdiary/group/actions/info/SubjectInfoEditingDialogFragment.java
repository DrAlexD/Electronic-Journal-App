package com.example.electronicdiary.group.actions.info;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.SubjectInfo;

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

        CheckBox isExam = root.findViewById(R.id.subjectInfoIsExamEditing);
        CheckBox isDifferentiatedCredit = root.findViewById(R.id.subjectInfoIsDifferentiatedCreditEditing);

        subjectInfoEditingViewModel.getSubjectInfo().observe(this, subjectInfo -> {
            if (subjectInfo != null) {
                isExam.setChecked(subjectInfo.isExam());
                isDifferentiatedCredit.setChecked(subjectInfo.isDifferentiatedCredit());
            }
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
                .setTitle("Изменить данные по предмету")
                .setPositiveButton("Подтвердить", null).create();

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        CheckBox isExam = root.findViewById(R.id.subjectInfoIsExamEditing);
        CheckBox isDifferentiatedCredit = root.findViewById(R.id.subjectInfoIsDifferentiatedCreditEditing);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            SubjectInfo subjectInfo = subjectInfoEditingViewModel.getSubjectInfo().getValue();

            subjectInfoEditingViewModel.editSubjectInfo(subjectInfoId, subjectInfo.getGroup(), subjectInfo.getSubject(),
                    subjectInfo.getLecturerId(), subjectInfo.getSeminarian(), subjectInfo.getSemester(),
                    isExam.isChecked(), isDifferentiatedCredit.isChecked());

            subjectInfoEditingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("subjectInfoId", subjectInfoId);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_subject_info_editing_to_group_performance, bundle);
                }
            });
        });
    }
}
