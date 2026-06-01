package de.app.kfzscanner.Datenbank;

import androidx.annotation.NonNull;

public class Car {

    public String licensePlate;
    public String manufacture;
    public String person;
    public String unit;
    public boolean isElectric;

    public Car(String licensePlate, String manufacture, String person, String unit, boolean isElectric) {
        this.licensePlate = licensePlate;
        this.manufacture = manufacture;
        this.person = person;
        this.unit = unit;
        this.isElectric = isElectric;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}