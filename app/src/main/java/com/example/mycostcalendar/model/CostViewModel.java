package com.example.mycostcalendar.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CostViewModel extends AndroidViewModel {

    private CostRepository repository;

    private LiveData<List<Cost>> allCosts;

    public CostViewModel(Application application) {
        super(application);
        repository = new CostRepository(application);
        allCosts = repository.getAllCosts();
    }

    public LiveData<List<Cost>> getAllCosts() {
        return allCosts;
    }

    public List<Cost> getAllCostsList() {
        return repository.getAllCostsList();
    }

    public void insert(Cost cost) {
        repository.insert(cost);
    }

    public void update(Cost cost) {
        repository.update(cost);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void delete(Cost cost) {
        repository.delete(cost);
    }

    public LiveData<List<Cost>> getCostsByDate(long startDate, long endDate) {
        return repository.getCostsByDate(startDate, endDate);
    }
}
