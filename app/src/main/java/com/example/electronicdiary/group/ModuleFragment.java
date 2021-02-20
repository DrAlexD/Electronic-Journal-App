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
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.student.Student;

import java.util.ArrayList;
import java.util.Date;

public class ModuleFragment extends Fragment {
    private int position;

    private String group;
    private ArrayList<Student> students;
    private ArrayList<Date> visits;
    private ArrayList<StudentInModule> studentsInModule;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_module, container, false);

        position = getArguments().getInt("position");

        GroupPerformanceViewModel groupPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(GroupPerformanceViewModel.class);
        studentsInModule = groupPerformanceViewModel.getStudentsInModule().getValue();
        students = groupPerformanceViewModel.getStudents().getValue();
        visits = groupPerformanceViewModel.getVisits().getValue();
        group = groupPerformanceViewModel.getGroup().getValue();

        ModuleViewModel moduleViewModel = new ViewModelProvider(this).get(ModuleViewModel.class);
        moduleViewModel.setPosition(position);

        generateVisitsTable(root);

        return root;
    }

    private void generateVisitsTable(View root) {
        TableLayout studentsInModuleVisitsTable = root.findViewById(R.id.studentsInModuleVisitsTable);
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        TableRow visitsRow = generateVisitsRow(padding2inDp, padding5inDp);
        studentsInModuleVisitsTable.addView(visitsRow);

        for (int i = 0; i < studentsInModule.size(); i++) {
            TableRow pointsRow = generatePointsRow(i, padding2inDp, padding5inDp);
            studentsInModuleVisitsTable.addView(pointsRow);
        }
    }

    private TableRow generateVisitsRow(int padding2inDp, int padding5inDp) {
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

        return visitsRow;
    }

    private TableRow generatePointsRow(int i, int padding2inDp, int padding5inDp) {
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
        studentView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("student", ((TextView) view).getText().toString());
            bundle.putString("group", group);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_student_profile, bundle);
        });
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

        return pointsRow;
    }
}