package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.ProfessorFormState;
import com.example.electronicdiary.data_classes.Professor;

public class ProfessorAddingViewModel extends ViewModel {
    private final MutableLiveData<ProfessorFormState> professorFormState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    LiveData<ProfessorFormState> getProfessorFormState() {
        return professorFormState;
    }

    public void professorAddingDataChanged(String professorName, String professorSecondName, String professorLogin,
                                           String professorPassword, boolean isNameOrSecondNameChanged) {
        professorFormState.setValue(new ProfessorFormState(professorName, professorSecondName, professorLogin,
                professorPassword, isNameOrSecondNameChanged));
    }

    public void addProfessor(String professorName, String professorSecondName, String professorLogin, String professorPassword, String role) {
        Repository.getInstance().addProfessor(new Professor(professorName, professorSecondName, professorLogin, professorPassword, role), answer);
    }
}