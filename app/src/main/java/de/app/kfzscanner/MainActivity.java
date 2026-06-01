package de.app.kfzscanner;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.app.kfzscanner.Datenbank.Car;


public class MainActivity extends AppCompatActivity {

    private EditText etLicensePlate;
    private Button btnSearch;
    private TableLayout tableLayout;
    private TextView tvLicensePlate;
    private TextView tvManufacture;
    private TextView tvPerson;
    private TextView tvUnit;
    private TextView tvElectric;
    private Button btnImport;
    private ArrayList<Car> database = new ArrayList<>();
    private static final int PICK_CSV_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etLicensePlate = findViewById(R.id.etLicensePlate);
        btnSearch = findViewById(R.id.btnSearch);
        tableLayout = findViewById(R.id.tableLayout);
        tvLicensePlate = findViewById(R.id.tvLicensePlate);
        tvManufacture = findViewById(R.id.tvManufacture);
        tvPerson = findViewById(R.id.tvPerson);
        tvUnit = findViewById(R.id.tvUnit);
        tvElectric = findViewById(R.id.tvElectric);
        btnImport = findViewById(R.id.btnImport);

        etLicensePlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent();
            }
        });

        btnImport = findViewById(R.id.btnImport);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/comma-separated-values"); // oder "*/*" für alle Dateitypen
        startActivityForResult(intent, PICK_CSV_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CSV_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                importDataFromCSV(uri);
            }
        }
    }

    private void importDataFromCSV(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length >= 6) {
                    String licensePlate = values[0] + " " + values[1];
                    String brand = values[2];
                    String person = values[3] + " " + values[4];
                    String unit = values[5];
                    boolean isElectric = false;
                    if (values.length == 7) {
                        isElectric = values[6].trim().equalsIgnoreCase("x");
                    }
                    Car car = new Car(licensePlate, brand, person, unit, isElectric);
                    database.add(car);
                }


            }
            reader.close();
        } catch (
                java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void clickEvent() {
        String licensePlate = etLicensePlate.getText().toString();
        etLicensePlate.setText(null);
        searchDatabase(licensePlate);

        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            etLicensePlate.clearFocus();
        }
    }

//private void searchDatabase(String licensePlate) {
//    String manufacture = "Toyota";
//    String person = "John Doe";
//    String unit = "1234";
//    boolean isElectric = true;
//    tableLayout.setVisibility(View.VISIBLE);
//    tvLicensePlate.setText(licensePlate);
//    tvManufacture.setText(manufacture);
//    tvPerson.setText(person);
//    tvUnit.setText(unit);
//    tvElectric.setText(isElectric ? "Ja" : "Nein");
//}

    private void searchDatabase(String licensePlate) {
        Car car = findCarByLicensePlate(database, licensePlate);
        if (car != null) {
            tableLayout.setVisibility(View.VISIBLE);
            tvLicensePlate.setText(car.licensePlate);
            tvManufacture.setText(car.manufacture);
            tvPerson.setText(car.person);
            tvUnit.setText(car.unit);
            tvElectric.setText(car.isElectric ? "Ja" : "Nein");
        } else {
            tableLayout.setVisibility(View.GONE);
        }
    }

    public Car findCarByLicensePlate(List<Car> cars, String licensePlate) {
        return cars.stream()
                .filter(car -> car.getLicensePlate().replaceAll("\\s+", "").equalsIgnoreCase(licensePlate.replaceAll("\\s+", "")))
                .findFirst()
                .orElse(null);
    }

}