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

public class LessonEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_lesson_editing, null);

        int lessonId = getArguments().getInt("lessonId");

        LessonEditingViewModel lessonEditingViewModel = new ViewModelProvider(this).get(LessonEditingViewModel.class);
        lessonEditingViewModel.downloadLessonById(lessonId);

        EditText dateAndTime = root.findViewById(R.id.lessonDateAndTimeEditing);
        CheckBox isLecture = root.findViewById(R.id.lessonIsLectureEditing);
        EditText pointsPerVisit = root.findViewById(R.id.lessonPointsPerVisitEditing);
        lessonEditingViewModel.getLesson().observe(this, lesson -> {
            if (lesson == null) {
                return;
            }

            dateAndTime.setText(((lesson.getDateAndTime().getDate()) < 10 ? "0" + (lesson.getDateAndTime().getDate()) :
                    (lesson.getDateAndTime().getDate())) + "." +
                    ((lesson.getDateAndTime().getMonth() + 1) < 10 ? "0" + (lesson.getDateAndTime().getMonth() + 1) :
                            (lesson.getDateAndTime().getMonth() + 1)) + "." + lesson.getDateAndTime().getYear() + " " +
                    ((lesson.getDateAndTime().getHours()) < 10 ? "0" + (lesson.getDateAndTime().getHours()) :
                            (lesson.getDateAndTime().getHours())) + ":" +
                    ((lesson.getDateAndTime().getMinutes()) < 10 ? "0" + (lesson.getDateAndTime().getMinutes()) :
                            (lesson.getDateAndTime().getMinutes())));
            isLecture.setChecked(lesson.isLecture());
            pointsPerVisit.setText(String.valueOf(lesson.getPointsPerVisit()));
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                lessonEditingViewModel.lessonEditingDataChanged(dateAndTime.getText().toString(), pointsPerVisit.getText().toString());
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

        lessonEditingViewModel.getLessonFormState().observe(this, lessonFormState -> {
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
                .setTitle("Изменить данные занятия")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    String date = dateAndTime.getText().toString().split(" ")[0];
                    String time = dateAndTime.getText().toString().split(" ")[1];
                    String[] splitedDate = date.split("\\.");
                    String[] splitedTime = time.split(":");

                    lessonEditingViewModel.editLesson(lessonId, new Date(Integer.parseInt(splitedDate[2]),
                                    Integer.parseInt(splitedDate[1]) - 1, Integer.parseInt(splitedDate[0]),
                                    Integer.parseInt(splitedTime[0]), Integer.parseInt(splitedTime[1])), isLecture.isChecked(),
                            Integer.parseInt(pointsPerVisit.getText().toString()));

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", lessonEditingViewModel.getLesson().getValue().getModuleNumber() - 1);
                    bundle.putInt("groupId", lessonEditingViewModel.getLesson().getValue().getGroupId());
                    bundle.putInt("subjectId", lessonEditingViewModel.getLesson().getValue().getSubjectId());
                    bundle.putInt("lecturerId", lessonEditingViewModel.getLesson().getValue().getLecturerId());
                    bundle.putInt("seminarianId", lessonEditingViewModel.getLesson().getValue().getSeminarianId());
                    bundle.putInt("semesterId", lessonEditingViewModel.getLesson().getValue().getSemesterId());

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_editing_to_group_performance, bundle);
                })
                .setNegativeButton("Отменить", (dialog, id) -> {
                    dismiss();
                })
                .setNeutralButton("Удалить", (dialog, id) -> {
                    lessonEditingViewModel.deleteLesson(lessonId);

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", lessonEditingViewModel.getLesson().getValue().getModuleNumber() - 1);
                    bundle.putInt("groupId", lessonEditingViewModel.getLesson().getValue().getGroupId());
                    bundle.putInt("subjectId", lessonEditingViewModel.getLesson().getValue().getSubjectId());
                    bundle.putInt("lecturerId", lessonEditingViewModel.getLesson().getValue().getLecturerId());
                    bundle.putInt("seminarianId", lessonEditingViewModel.getLesson().getValue().getSeminarianId());
                    bundle.putInt("semesterId", lessonEditingViewModel.getLesson().getValue().getSemesterId());

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_editing_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        });

        return dialog;
    }
}
