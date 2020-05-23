package com.example.mycostcalendar.model;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CostRepository {

    private CostDao costDao;
    private LiveData<List<Cost>> allCosts;

    CostRepository(Application application) {
        CostRoomDatabase db = CostRoomDatabase.getDatabase(application);
        costDao = db.costDao();
        allCosts = costDao.getAllCosts();
    }

    LiveData<List<Cost>> getAllCosts() {
        return allCosts;
    }

    List<Cost> getAllCostsList() {
        try {
            return new getAllAsyncTask(costDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
    }

    void insert(Cost cost) {
        new insertAsyncTask(costDao).execute(cost);
    }

    void update(Cost cost) {
        new updateAsyncTask(costDao).execute(cost);
    }

    void deleteAll() {
        new deleteAllAsyncTask(costDao).execute();
    }

    void delete(Cost cost) {
        new deleteAsyncTask(costDao).execute(cost);
    }

    LiveData<List<Cost>> getCostsByDate(Long startDate, long endDate) {
        return costDao.getCostsByDate(startDate, endDate);
    }

    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<Cost>> {
        private CostDao asyncTaskDao;

        getAllAsyncTask(CostDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<Cost> doInBackground(Void... voids) {
            return asyncTaskDao.getAllCostsList();
        }
    }

    private static class insertAsyncTask extends AsyncTask<Cost, Void, Void> {
        private CostDao asyncTaskDao;

        insertAsyncTask(CostDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Cost... costs) {
            asyncTaskDao.insert(costs[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Cost, Void, Void> {
        private CostDao asyncTaskDao;

        updateAsyncTask(CostDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Cost... costs) {
            asyncTaskDao.update(costs[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private CostDao asyncTaskDao;

        private deleteAllAsyncTask(CostDao asyncTaskDao) {
            this.asyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Cost, Void, Void> {
        private CostDao asyncTaskDao;

        private deleteAsyncTask(CostDao asyncTaskDao) {
            this.asyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Cost... costs) {
            asyncTaskDao.delete(costs[0]);
            return null;
        }
    }
}
