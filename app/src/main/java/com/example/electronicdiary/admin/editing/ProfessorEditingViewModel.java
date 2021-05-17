package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Result;
import com.example.electronicdiary.admin.ProfessorFormState;
import com.example.electronicdiary.data_classes.Professor;

public class ProfessorEditingViewModel extends ViewModel {
    private final MutableLiveData<ProfessorFormState> professorFormState = new MutableLiveData<>();
    private final MutableLiveData<Result<Professor>> professor = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    LiveData<ProfessorFormState> getProfessorFormState() {
        return professorFormState;
    }

    public LiveData<Result<Professor>> getProfessor() {
        return professor;
    }

    public void professorEditingDataChanged(String professorName, String professorSecondName, String professorLogin,
                                            String professorPassword, boolean isNameOrSecondNameChanged) {
        professorFormState.setValue(new ProfessorFormState(professorName, professorSecondName, professorLogin,
                professorPassword, isNameOrSecondNameChanged));
    }

    public void downloadProfessorByIdWithLogin(long professorId) {
        Repository.getInstance().getProfessorById(professorId, professor);
    }

    public void editProfessor(long professorId, String professorName, String professorSecondName, String professorLogin, String professorPassword, String role) {
        Repository.getInstance().editProfessor(professorId, new Professor(professorName, professorSecondName, professorLogin, professorPassword, role), answer);
    }
}