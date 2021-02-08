package com.example.electronicdiary.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.electronicdiary.R;

public class ProfileFragment extends Fragment {
    //private SubjectsAdapter subjectsAdapter;
    //private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //TODO запуск поиска
        /*subjectsAdapter = new SubjectsAdapter(getActivity().getApplicationContext(), subjects);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedStudentsList);
        recyclerView.setAdapter(subjectsAdapter);
        recyclerView.addOnItemTouchListener(getClickListener());
        */
        
        /*profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                
            }
        });*/

        return root;
    }

    /*private RecyclerItemClickListener getClickListener() {

        return new RecyclerItemClickListener(this, heroesView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                //not used
            }
        });
    }*/
}