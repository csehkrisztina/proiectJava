package com.example.mycostcalendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycostcalendar.helper.DateFormatHelper;
import com.example.mycostcalendar.model.Cost;
import com.example.mycostcalendar.model.CostViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CostCalendar extends AppCompatActivity implements OnEditCostListener {

    public static final int ADD_COST_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_COST_ACTIVITY_REQUEST_CODE = 2;

    private CostListAdapter adapter;
    private CostViewModel costViewModel;

    private TextView textViewEmpty;
    private TextView total;
    private Spinner yearSpinner;
    private Spinner monthSpinner;

    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setUpToolbar();

        currentDate = DateFormatHelper.calendarFirstDayOfMonth(null).getTime();

        costViewModel = ViewModelProviders.of(this).get(CostViewModel.class);
//        costViewModel.deleteAll();

        RecyclerView recyclerView = initializeRecyclerView();

        setUpViews();

        ItemTouchHelper itemTouchHelper = buildItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(recyclerView);

        setUpFab();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Cost cost = data.getParcelableExtra(Constants.COST_EXTRA.getValue());

            if (requestCode == ADD_COST_ACTIVITY_REQUEST_CODE) {
                costViewModel.insert(cost);
            }
            if (requestCode == EDIT_COST_ACTIVITY_REQUEST_CODE) {
                costViewModel.update(cost);
            }
        }
    }

    @Override
    public void editCost(Cost cost, int index) {
        Intent editCostIntent = new Intent(CostCalendar.this, AddCost.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.COST_EXTRA.getValue(), cost);
        bundle.putBoolean(Constants.IS_EDIT.getValue(), true);

        editCostIntent.putExtras(bundle);

        startActivityForResult(editCostIntent, EDIT_COST_ACTIVITY_REQUEST_CODE);
    }

    private void setUpViews() {
        textViewEmpty = findViewById(R.id.empty_label);
        total = findViewById(R.id.calendar_total);

        setUpYearSpinner();
        setUpMonthSpinner();
    }

    private void setUpMonthSpinner() {
        monthSpinner = findViewById(R.id.month_spinner);

        // Set up listener
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedMonth = position;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.set(Calendar.MONTH, selectedMonth);

                currentDate = calendar.getTime();

                observeCosts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set up selected item
        int currentMonthPosition = DateFormatHelper.getMonthFromDate(currentDate);
        monthSpinner.setSelection(currentMonthPosition - 1);
    }

    private void setUpYearSpinner() {
        yearSpinner = findViewById(R.id.year_spinner);

        // Set up adapter
        ArrayAdapter<String> yearSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getYearSpinnerContent());
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearSpinnerAdapter);

        // Set up listener
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.set(Calendar.YEAR, Integer.parseInt(selectedYear));

                currentDate = calendar.getTime();

                observeCosts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set up selected item
        int currentYearPosition = yearSpinnerAdapter.getPosition(DateFormatHelper.getYearFromDate(currentDate));
        yearSpinner.setSelection(currentYearPosition);
    }

    private List<String> getYearSpinnerContent() {
        Set<String> years = new HashSet<>();
        years.add(DateFormatHelper.getYearFromDate(currentDate));

        List<Cost> allCosts = costViewModel.getAllCostsList();
        for (Cost cost : allCosts) {
            String year = DateFormatHelper.getYearFromDate(cost.getDate());
            years.add(year);
        }
        return new ArrayList<>(years);
    }

    private void setUpFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCostIntent = new Intent(CostCalendar.this, AddCost.class);
                addCostIntent.putExtra(Constants.IS_EDIT.getValue(), false);
                startActivityForResult(addCostIntent, ADD_COST_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private RecyclerView initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new CostListAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        return recyclerView;
    }

    private void observeCosts() {
        long startDate = DateFormatHelper.getFirstDayOfMonthTmp(currentDate);
        long endDate = DateFormatHelper.getLastDayOfMonthTmp(currentDate);

        costViewModel.getCostsByDate(startDate, endDate).observe(this, new Observer<List<Cost>>() {
            @Override
            public void onChanged(@Nullable final List<Cost> costs) {
                adapter.setCosts(costs);
                if (adapter.isCostsEmpty()) {
                    textViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    textViewEmpty.setVisibility(View.INVISIBLE);
                }
                total.setText(String.valueOf(adapter.getCostsTotal()));
            }
        });
    }

    private ItemTouchHelper buildItemTouchHelper() {
        return new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Cost cost = adapter.getCostAtPosition(position);
                        costViewModel.delete(cost);
                    }
                }
        );
    }
}