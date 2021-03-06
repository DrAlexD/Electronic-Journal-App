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

public class EventInfoDialogFragment extends DialogFragment {
    private final ArrayList<String> values = new ArrayList<>();
    private final ArrayList<View> changedOptions = new ArrayList<>();
    private AlertDialog dialog;

    private boolean minPointsIsEmptyFlag = true;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_info, null);

        downloadData();

        setData(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить данные " + getArguments().getString("eventTitle"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    for (View option : changedOptions) {
                        //TODO отправка новых установленных значений мероприятия
                    }
                    dismiss();
                })
                .setNegativeButton("Отменить", (dialog, id) -> {
                    dismiss();
                })
                .setNeutralButton("Удалить", (dialog, id) -> {
                    //TODO удаление мероприятия из базы
                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_info_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!minPointsIsEmptyFlag);
        });

        return dialog;
    }

    private void downloadData() {
        //TODO загрузка информации о мероприятии
        values.add("7");
    }

    private void setData(View root) {
        String value = values.get(0);
        EditText eventMinPoints = root.findViewById(R.id.eventInfoMinPoints);
        eventMinPoints.setText(value);
        minPointsIsEmptyFlag = false;
        eventMinPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                minPointsIsEmptyFlag = s.toString().trim().isEmpty();
                if (minPointsIsEmptyFlag)
                    eventMinPoints.setError("Пустое поле");
                else
                    eventMinPoints.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!minPointsIsEmptyFlag);

                if (value.equals(s.toString()))
                    changedOptions.remove(eventMinPoints);
                else if (!changedOptions.contains(eventMinPoints))
                    changedOptions.add(eventMinPoints);
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
