package com.example.electronicdiary.search.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Student;
import com.example.electronicdiary.search.StudentsAdapter;

import java.util.ArrayList;

public class SearchAllStudentsFragment extends Fragment {
    private StudentsAdapter studentsAdapter;
    private ArrayList<Student> students;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_all_students, container, false);

        downloadData();

        int actionCode = getArguments().getInt("actionCode");

        View.OnClickListener onItemClickListener = view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            if (actionCode == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("student", students.get(position).getFullName());
                Navigation.findNavController(view).navigate(R.id.action_search_all_students_to_dialog_student_editing, bundle);
            } else if (actionCode == 2) {
                //TODO удаление студента из базы

                Bundle bundle = new Bundle();
                bundle.putInt("openPage", actionCode);
                Navigation.findNavController(view).navigate(R.id.action_search_all_students_to_admin_actions, bundle);
            } else if (actionCode == 3) {
                Bundle bundle = new Bundle();
                bundle.putInt("actionCode", actionCode);
                bundle.putString("student", students.get(position).getFullName());
                bundle.putString("group", students.get(position).getGroup());

                Navigation.findNavController(view).navigate(R.id.action_search_all_students_to_search_all_groups, bundle);
            }
        };
        studentsAdapter = new StudentsAdapter(getContext(), students, onItemClickListener);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAllStudentsList);
        recyclerView.setAdapter(studentsAdapter);
        recyclerView.setHasFixedSize(false);

        final SearchView searchView = root.findViewById(R.id.allStudentsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        return root;
    }

    private void downloadData() {
        //TODO поиск всех студентов
        students = new ArrayList<>();
        students.add(new Student(1, "1ИУ9-11", "1Александр", "1Другаков"));
        students.add(new Student(2, "2ИУ9-21", "2Александр", "2Другаков"));
        students.add(new Student(3, "3ИУ9-31", "3Александр", "3Другаков"));
        students.add(new Student(4, "4ИУ9-41", "4Александр", "4Другаков"));
        students.add(new Student(5, "5ИУ9-51", "5Александр", "5Другаков"));
        students.add(new Student(6, "6ИУ9-61", "6Александр", "6Другаков"));
        students.add(new Student(7, "7ИУ9-71", "7Александр", "7Другаков"));
        students.add(new Student(8, "8ИУ9-81", "8Александр", "8Другаков"));
        students.add(new Student(9, "9ИУ9-91", "9Александр", "9Другаков"));
    }

    private SearchView.OnQueryTextListener getSearchTextUpdateListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentsAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}