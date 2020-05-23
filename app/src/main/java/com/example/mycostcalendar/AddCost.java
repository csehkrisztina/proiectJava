package com.example.mycostcalendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycostcalendar.helper.DateFormatHelper;
import com.example.mycostcalendar.model.Cost;

import java.util.Calendar;
import java.util.Date;

public class AddCost extends AppCompatActivity {

    private EditText titleEditText;
    private EditText priceEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;
    private Button saveButton;

    private Cost cost;
    private boolean isEdit;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);

        setUpTextViews();
        initCost();
        addDateOnClickListener();
        setUpSaveButton();
        setUpRequiredFieldsListener();
    }

    private void initCost() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            isEdit = extras.getBoolean(Constants.IS_EDIT.getValue());

            if (isEdit) {
                setTitle(R.string.edit_cost_title);
                cost = intent.getParcelableExtra(Constants.COST_EXTRA.getValue());
                titleEditText.setText(cost.getTitle());
                priceEditText.setText(String.valueOf(cost.getPrice()));
                descriptionEditText.setText(cost.getDescription());
                dateEditText.setText(DateFormatHelper.formatDate(cost.getDate()));
            } else {
                setTitle(R.string.add_cost_title);
                dateEditText.setText(DateFormatHelper.formatDate(new Date()));
            }
        }
    }

    private void setUpSaveButton() {
        saveButton = findViewById(R.id.addCost_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent replyIntent = new Intent();

                if (TextUtils.isEmpty(titleEditText.getText()) || TextUtils.isEmpty(priceEditText.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String title = titleEditText.getText().toString();
                    double price = Double.parseDouble(priceEditText.getText().toString());
                    String description = descriptionEditText.getText().toString();
                    Date date = DateFormatHelper.formatStringToDate(dateEditText.getText().toString());

                    if (isEdit) {
                        cost.setTitle(title);
                        cost.setPrice(price);
                        cost.setDescription(description);
                        cost.setDate(date);
                    } else {
                        cost = new Cost(title, price, description, date);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.COST_EXTRA.getValue(), cost);
                    bundle.putBoolean(Constants.IS_EDIT.getValue(), isEdit);
                    replyIntent.putExtras(bundle);

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    private void setUpTextViews() {
        titleEditText = findViewById(R.id.addCost_title);
        priceEditText = findViewById(R.id.addCost_price);
        descriptionEditText = findViewById(R.id.addCost_description);
        dateEditText = findViewById(R.id.addCost_date);
        dateEditText.setInputType(InputType.TYPE_NULL);
    }

    private void setUpRequiredFieldsListener() {
        if (titleEditText.length() == 0 || priceEditText.length() == 0) {
            saveButton.setEnabled(false);
        }

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (titleEditText.length() == 0 || priceEditText.length() == 0)
                    saveButton.setEnabled(false);
                else
                    saveButton.setEnabled(true);
            }
        });

        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (titleEditText.length() == 0 || priceEditText.length() == 0)
                    saveButton.setEnabled(false);
                else
                    saveButton.setEnabled(true);
            }
        });

    }

    private void addDateOnClickListener() {
        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String dateFromEditText = dateEditText.getText().toString();
                Date dateFromString = DateFormatHelper.formatStringToDate(dateFromEditText);

                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFromString);

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AddCost.this,
                        R.style.SpinnerDatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateEditText.setText(DateFormatHelper.formatDate(dayOfMonth, month + 1, year));
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }
}
