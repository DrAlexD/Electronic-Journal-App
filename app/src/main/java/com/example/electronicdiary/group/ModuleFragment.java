package com.example.electronicdiary.group;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.electronicdiary.R;
import com.example.electronicdiary.search.Student;

import java.util.ArrayList;
import java.util.Date;

public class ModuleFragment extends Fragment {
    private GroupPerformanceViewModel groupPerformanceViewModel;
    private int position;
    private ModuleViewModel moduleViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_module, container, false);

        position = getArguments().getInt("position");
        groupPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(GroupPerformanceViewModel.class);
        moduleViewModel = new ViewModelProvider(this).get(ModuleViewModel.class);
        moduleViewModel.setPosition(position);

        generateVisitsTable(root);

        return root;
    }

    private void generateVisitsTable(View root) {
        ArrayList<StudentInModule> studentsInModule = groupPerformanceViewModel.getStudentsInModule().getValue();
        ArrayList<Student> students = groupPerformanceViewModel.getStudents().getValue();
        ArrayList<Date> visits = groupPerformanceViewModel.getVisits().getValue();

        TableLayout studentsInModuleVisitsTable = root.findViewById(R.id.studentsInModuleVisitsTable);
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());

        TableRow visitsRow = new TableRow(getContext());
        visitsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        visitsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
        TextView infoView = new TextView(getContext());
        infoView.setTextSize(20);
        infoView.setText("Студент\\Занятие");
        infoView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        infoView.setGravity(Gravity.CENTER);
        visitsRow.addView(infoView);
        for (Date visit : visits) {
            TextView visitView = new TextView(getContext());
            visitView.setTextSize(20);
            visitView.setText(visit.getDate() + "." + (visit.getMonth() + 1));
            visitView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            visitView.setGravity(Gravity.CENTER);
            //visitView.setRotation(90);
            visitsRow.addView(visitView);
        }
        studentsInModuleVisitsTable.addView(visitsRow);

        for (int i = 0; i < studentsInModule.size(); i++) {
            StudentInModule studentInModule = studentsInModule.get(i);

            TableRow pointsRow = new TableRow(getContext());
            pointsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                    LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
            pointsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

            TextView studentView = new TextView(getContext());
            studentView.setText(students.get(i).getName());
            studentView.setTextSize(20);
            studentView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            studentView.setGravity(Gravity.CENTER);
            pointsRow.addView(studentView);

            for (Date visit : visits) {
                TextView pointsView = new TextView(getContext());
                pointsView.setTextSize(20);
                pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                if (studentInModule.getVisitsWithPoints().containsKey(visit))
                    pointsView.setText(studentInModule.getVisitsWithPoints().get(visit).toString());
                else
                    pointsView.setText("");
                pointsView.setGravity(Gravity.CENTER);
                pointsRow.addView(pointsView);
            }
            studentsInModuleVisitsTable.addView(pointsRow);
        }
    }
}