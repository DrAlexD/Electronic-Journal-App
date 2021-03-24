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

public class LessonEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_lesson_editing, null);

        int lessonId = getArguments().getInt("lessonId");

        LessonEditingViewModel lessonEditingViewModel = new ViewModelProvider(this).get(LessonEditingViewModel.class);
        lessonEditingViewModel.downloadLessonById(lessonId);

        EditText lessonAttendPoints = root.findViewById(R.id.lessonAttendPointsEditing);

        lessonEditingViewModel.getLesson().observe(this, lesson -> {
            if (lesson == null) {
                return;
            }

            lessonAttendPoints.setText("1");
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                lessonEditingViewModel.lessonEditingDataChanged(lessonAttendPoints.getText().toString());
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
        lessonAttendPoints.addTextChangedListener(afterTextChangedListener);

        lessonEditingViewModel.getLessonFormState().observe(this, lessonFormState -> {
            if (lessonFormState == null) {
                return;
            }

            lessonAttendPoints.setError(lessonFormState.getLessonAttendPointsError() != null ?
                    getString(lessonFormState.getLessonAttendPointsError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(lessonFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить данные занятия " + getArguments().getString("lessonDate"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    lessonEditingViewModel.editLesson(lessonAttendPoints.getText().toString());

                    dismiss();
                })
                .setNegativeButton("Отменить", (dialog, id) -> {
                    dismiss();
                })
                .setNeutralButton("Удалить", (dialog, id) -> {
                    lessonEditingViewModel.deleteLesson(lessonId);

                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));
                    bundle.putInt("openPage", getArguments().getInt("position") - 1);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_editing_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        });

        return dialog;
    }
}
