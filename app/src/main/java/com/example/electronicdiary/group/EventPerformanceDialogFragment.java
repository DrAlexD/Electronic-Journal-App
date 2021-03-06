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

import java.util.ArrayList;

public class EventPerformanceDialogFragment extends DialogFragment {
    private final ArrayList<String> values = new ArrayList<>();
    private final ArrayList<View> changedOptions = new ArrayList<>();
    private AlertDialog dialog;

    private boolean isHasData = false;
    private boolean earnedPointsIsEmptyFlag = true;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_performance, null);

        downloadData();

        setData(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Успеваемость по " + getArguments().getString("eventTitle"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    for (View option : changedOptions) {
                        //TODO отправка новых установленных значений успеваемости по мероприятию
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_performance_to_group_performance, bundle);
                })
                .setNegativeButton("Отменить", (dialog, id) -> {
                    dismiss();
                })
                .setNeutralButton("Удалить", (dialog, id) -> {
                    //TODO удаление успеваемости по мероприятию из базы
                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_performance_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!earnedPointsIsEmptyFlag);
        });

        return dialog;
    }

    private void downloadData() {
        //TODO загрузка информации о успеваемости студента по мероприятию
        isHasData = true;
        if (isHasData) {
            values.add(getArguments().getString("earnedPoints"));
        } else {
            values.add("");
        }
    }

    private void setData(View root) {
        String value = values.get(0);
        EditText eventEarnedPoints = root.findViewById(R.id.eventPerformanceEarnedPoints);
        if (isHasData) {
            eventEarnedPoints.setText(value);
            earnedPointsIsEmptyFlag = false;
        }
        eventEarnedPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                earnedPointsIsEmptyFlag = s.toString().trim().isEmpty();
                if (earnedPointsIsEmptyFlag)
                    eventEarnedPoints.setError("Пустое поле");
                else
                    eventEarnedPoints.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!earnedPointsIsEmptyFlag);

                if (value.equals(s.toString()))
                    changedOptions.remove(eventEarnedPoints);
                else if (!changedOptions.contains(eventEarnedPoints))
                    changedOptions.add(eventEarnedPoints);
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
