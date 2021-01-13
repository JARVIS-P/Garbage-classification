package db;

import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

public class User {

    private String admin;
    private String password;
    private String name;
    private String telePhoneNumber;
    private String imageId;
    private Integer recyclable;
    private Integer nonRecyclable;
    private Integer other;
    private Integer kitchen;

    public Integer getRecyclable() {
        return recyclable;
    }

    public void setRecyclable(Integer recyclable) {
        this.recyclable = recyclable;
    }

    public Integer getNonRecyclable() {
        return nonRecyclable;
    }

    public void setNonRecyclable(Integer nonRecyclable) {
        this.nonRecyclable = nonRecyclable;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public Integer getKitchen() {
        return kitchen;
    }

    public void setKitchen(Integer kitchen) {
        this.kitchen = kitchen;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelePhoneNumber() {
        return telePhoneNumber;
    }

    public void setTelePhoneNumber(String telePhoneNumber) {
        this.telePhoneNumber = telePhoneNumber;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
