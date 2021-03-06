package com.example.electronicdiary.group;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LessonPerformanceDialogFragment extends DialogFragment {
    private final ArrayList<String> values = new ArrayList<>();
    private final ArrayList<View> changedOptions = new ArrayList<>();
    private AlertDialog dialog;

    private boolean isHasData = false;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_lesson_performance, null);

        downloadData();

        setData(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Успеваемость " + getArguments().getString("lessonDate"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    for (View option : changedOptions) {
                        //TODO отправка новых установленных значений успеваемости по занятию
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));
                    bundle.putInt("openPage", getArguments().getInt("position") - 1);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_performance_to_group_performance, bundle);
                })
                .setNegativeButton("Отменить", (dialog, id) -> {
                    dismiss();
                })
                .setNeutralButton("Удалить", (dialog, id) -> {
                    //TODO удаление успеваемости по занятию из базы
                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));
                    bundle.putInt("openPage", getArguments().getInt("position") - 1);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_performance_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
        });

        return dialog;
    }

    private void downloadData() {
        //TODO загрузка информации о успеваемости студента по занятию
        isHasData = true;
        if (isHasData) {
            values.add(getArguments().getString("isAttended"));
        } else {
            values.add("");
        }
    }

    private void setData(View root) {
        String value = values.get(0);
        CheckBox lessonIsAttended = root.findViewById(R.id.lessonPerformanceIsAttended);
        if (isHasData) {
            lessonIsAttended.setChecked("true".equals(value));
        }
        lessonIsAttended.setOnClickListener(view -> {
            String s = lessonIsAttended.isChecked() ? "true" : "false";
            if (value.equals(s))
                changedOptions.remove(lessonIsAttended);
            else if (!changedOptions.contains(lessonIsAttended))
                changedOptions.add(lessonIsAttended);
        });
    }
}
