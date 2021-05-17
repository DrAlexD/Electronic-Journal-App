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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Module;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessonAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private LessonAddingViewModel lessonAddingViewModel;
    private LiveData<Module> moduleLiveData;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_lesson_adding, null);

        long moduleId = getArguments().getLong("moduleId");

        lessonAddingViewModel = new ViewModelProvider(this).get(LessonAddingViewModel.class);

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

        lessonAddingViewModel.downloadModuleById(moduleId);
        moduleLiveData = lessonAddingViewModel.getModule();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные занятия")
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        EditText dateAndTime = root.findViewById(R.id.lessonDateAndTimeAdding);
        CheckBox isLecture = root.findViewById(R.id.lessonIsLectureAdding);
        EditText pointsPerVisit = root.findViewById(R.id.lessonPointsPerVisitAdding);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            LiveData<Boolean> answerLiveData = Transformations.switchMap(moduleLiveData, module -> {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Date dateAndTimeR = dateFormat.parse(dateAndTime.getText().toString());

                    lessonAddingViewModel.addLesson(module,
                            dateAndTimeR, isLecture.isChecked(), Integer.parseInt(pointsPerVisit.getText().toString()));

                    return lessonAddingViewModel.getAnswer();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            });

            answerLiveData.observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", lessonAddingViewModel.getModule().getValue().getModuleNumber() - 1);
                    bundle.putLong("subjectInfoId", lessonAddingViewModel.getModule().getValue().getSubjectInfo().getId());

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_adding_to_group_performance, bundle);
                }
            });
        });
    }
}