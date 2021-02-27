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
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class SemesterEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    private boolean yearIsLessFlag = true;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_semester_editing, null);

        EditText semesterYear = root.findViewById(R.id.semesterYearEditing);
        semesterYear.setText(getArguments().getString("semesterYear"));
        semesterYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                yearIsLessFlag = s.toString().trim().length() < 4;
                if (yearIsLessFlag)
                    semesterYear.setError("Год должен содержать =4 символа");
                else
                    semesterYear.setError(null);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!yearIsLessFlag);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные семестра")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    //TODO изменение семестра в базе
                    //dismiss();

                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 1);
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_search_semesters_to_admin_actions, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                .setEnabled(!yearIsLessFlag));

        return dialog;
    }
}
