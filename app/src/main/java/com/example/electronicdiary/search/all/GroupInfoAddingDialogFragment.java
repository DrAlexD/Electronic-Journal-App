package com.example.electronicdiary.search.all;

import android.app.Dialog;
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

public class GroupInfoAddingDialogFragment extends DialogFragment {

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_group_info_adding, null);

        CheckBox isLecturer = root.findViewById(R.id.isLecturer);
        CheckBox isExam = root.findViewById(R.id.isExam);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder.setView(root)
                .setTitle("Отметьте доп. информацию по группе")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    Repository.getInstance().addAvailableSubject(getArguments().getInt("professorId"),
                            isLecturer.isChecked(), getArguments().getInt("groupId"), getArguments().getInt("subjectId"),
                            getArguments().getInt("semesterId"), isExam.isChecked());

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_group_info_adding_to_profile);
                }).create();
    }
}
