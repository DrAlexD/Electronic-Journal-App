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

public class SubjectEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_subject_editing, null);

        SubjectEditingViewModel subjectEditingViewModel = new ViewModelProvider(this).get(SubjectEditingViewModel.class);
        long subjectId = getArguments().getLong("subjectId");
        subjectEditingViewModel.downloadSubjectById(subjectId);

        EditText subjectTitle = root.findViewById(R.id.subjectTitleEditing);

        subjectEditingViewModel.getSubject().observe(this, subject -> {
            if (subject == null) {
                return;
            }

            subjectTitle.setText(subject.getTitle());
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                subjectEditingViewModel.subjectEditingDataChanged(subjectTitle.getText().toString());
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
        subjectTitle.addTextChangedListener(afterTextChangedListener);

        subjectEditingViewModel.getSubjectFormState().observe(this, subjectFormState -> {
            if (subjectFormState == null) {
                return;
            }

            subjectTitle.setError(subjectFormState.getSubjectTitleError() != null ?
                    getString(subjectFormState.getSubjectTitleError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(subjectFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Измените название предмета")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    subjectEditingViewModel.editSubject(subjectId, subjectTitle.getText().toString());

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_subject_editing_to_admin_actions, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }
}
