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
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class LessonAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    private boolean dateIsEmptyFlag = true;
    private boolean attendPointsIsEmptyFlag = true;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_lesson_adding, null);

        setupFields(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные занятия")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    //TODO добавление занятия в базу
                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));
                    bundle.putInt("openPage", getArguments().getInt("position") - 1);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_adding_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(!dateIsEmptyFlag));

        return dialog;
    }

    private void setupFields(View root) {
        EditText lessonAddingDate = root.findViewById(R.id.lessonAddingDate);
        lessonAddingDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                dateIsEmptyFlag = s.toString().trim().isEmpty();
                if (dateIsEmptyFlag)
                    lessonAddingDate.setError("Пустое поле");
                else
                    lessonAddingDate.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!dateIsEmptyFlag);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        });

        EditText lessonAddingAttendPoints = root.findViewById(R.id.lessonAddingAttendPoints);
        lessonAddingAttendPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                attendPointsIsEmptyFlag = s.toString().trim().isEmpty();
                if (attendPointsIsEmptyFlag)
                    lessonAddingAttendPoints.setError("Пустое поле");
                else
                    lessonAddingAttendPoints.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!attendPointsIsEmptyFlag);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        });
    }
}