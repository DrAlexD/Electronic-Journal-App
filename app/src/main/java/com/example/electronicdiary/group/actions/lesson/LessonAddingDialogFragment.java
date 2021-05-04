package com.example.electronicdiary.group.actions.lesson;

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

import java.util.Date;

public class LessonAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_lesson_adding, null);

        int moduleNumber = getArguments().getInt("moduleNumber");
        long groupId = getArguments().getLong("groupId");
        long subjectId = getArguments().getLong("subjectId");
        long lecturerId = getArguments().getLong("lecturerId");
        long seminarianId = getArguments().getLong("seminarianId");
        long semesterId = getArguments().getLong("semesterId");

        LessonAddingViewModel lessonAddingViewModel = new ViewModelProvider(this).get(LessonAddingViewModel.class);

        EditText dateAndTime = root.findViewById(R.id.lessonDateAndTimeAdding);
        CheckBox isLecture = root.findViewById(R.id.lessonIsLectureAdding);
        EditText pointsPerVisit = root.findViewById(R.id.lessonPointsPerVisitAdding);
        pointsPerVisit.setText("1");
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                lessonAddingViewModel.lessonAddingDataChanged(dateAndTime.getText().toString(), pointsPerVisit.getText().toString());
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
        dateAndTime.addTextChangedListener(afterTextChangedListener);
        pointsPerVisit.addTextChangedListener(afterTextChangedListener);

        lessonAddingViewModel.getLessonFormState().observe(this, lessonFormState -> {
            if (lessonFormState == null) {
                return;
            }

            dateAndTime.setError(lessonFormState.getDateAndTimeError() != null ?
                    getString(lessonFormState.getDateAndTimeError()) : null);

            pointsPerVisit.setError(lessonFormState.getPointsPerVisitError() != null ?
                    getString(lessonFormState.getPointsPerVisitError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(lessonFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные занятия")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    String date = dateAndTime.getText().toString().split(" ")[0];
                    String time = dateAndTime.getText().toString().split(" ")[1];
                    String[] splitedDate = date.split("\\.");
                    String[] splitedTime = time.split(":");
                    lessonAddingViewModel.addLesson(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId,
                            new Date(Integer.parseInt(splitedDate[2]), Integer.parseInt(splitedDate[1]) - 1,
                                    Integer.parseInt(splitedDate[0]), Integer.parseInt(splitedTime[0]), Integer.parseInt(splitedTime[1])),
                            isLecture.isChecked(), Integer.parseInt(pointsPerVisit.getText().toString()));

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", moduleNumber - 1);
                    bundle.putLong("groupId", groupId);
                    bundle.putLong("subjectId", subjectId);
                    bundle.putLong("lecturerId", lecturerId);
                    bundle.putLong("seminarianId", seminarianId);
                    bundle.putLong("semesterId", semesterId);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_adding_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }
}