package com.example.electronic_journal.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.admin.ProfessorFormState;
import com.example.electronic_journal.data_classes.Professor;

public class ProfessorAddingViewModel extends ViewModel {
    private final MutableLiveData<ProfessorFormState> professorFormState = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> answer = new MutableLiveData<>();

    public LiveData<Result<Boolean>> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Result<Boolean>> answer) {
        this.answer = answer;
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