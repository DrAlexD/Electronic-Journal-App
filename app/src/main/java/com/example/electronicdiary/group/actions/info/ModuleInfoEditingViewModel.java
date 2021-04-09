package com.example.electronicdiary.group.actions.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.ModuleInfo;

public class ModuleInfoEditingViewModel extends ViewModel {
    private final MutableLiveData<ModuleInfoFormState> moduleInfoFormState = new MutableLiveData<>();
    private final MutableLiveData<ModuleInfo> moduleInfo = new MutableLiveData<>();

    LiveData<ModuleInfoFormState> getModuleInfoFormState() {
        return moduleInfoFormState;
    }

    public MutableLiveData<ModuleInfo> getModuleInfo() {
        return moduleInfo;
    }

    public void moduleInfoEditingDataChanged(String minPoints, String maxPoints) {
        moduleInfoFormState.setValue(new ModuleInfoFormState(minPoints, maxPoints));
    }

    public void downloadModuleInfoByNumber(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        this.moduleInfo.setValue(Repository.getInstance().getModuleInfoByNumber(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId));
    }

    public void editModuleInfo(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int minPoints, int maxPoints) {
        Repository.getInstance().editModuleInfo(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId, minPoints, maxPoints);
    }
}