package com.example.electronicdiary.group;

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

public class LessonAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_lesson_adding, null);

        int semesterId = getArguments().getInt("semesterId");
        int groupId = getArguments().getInt("groupId");
        int subjectId = getArguments().getInt("subjectId");

        LessonAddingViewModel lessonAddingViewModel = new ViewModelProvider(this).get(LessonAddingViewModel.class);

        EditText lessonDate = root.findViewById(R.id.lessonDateAdding);
        EditText lessonAttendPoints = root.findViewById(R.id.lessonAttendPointsAdding);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                lessonAddingViewModel.lessonAddingDataChanged(lessonDate.getText().toString(), lessonAttendPoints.getText().toString());
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
        lessonDate.addTextChangedListener(afterTextChangedListener);
        lessonAttendPoints.addTextChangedListener(afterTextChangedListener);

        lessonAddingViewModel.getLessonFormState().observe(this, lessonFormState -> {
            if (lessonFormState == null) {
                return;
            }

            lessonDate.setError(lessonFormState.getLessonDateError() != null ?
                    getString(lessonFormState.getLessonDateError()) : null);

            lessonAttendPoints.setError(lessonFormState.getLessonAttendPointsError() != null ?
                    getString(lessonFormState.getLessonAttendPointsError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(lessonFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные занятия")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    lessonAddingViewModel.addLesson(lessonDate.getText().toString(), lessonAttendPoints.getText().toString());

                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));
                    bundle.putInt("openPage", getArguments().getInt("position") - 1);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_adding_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }
}