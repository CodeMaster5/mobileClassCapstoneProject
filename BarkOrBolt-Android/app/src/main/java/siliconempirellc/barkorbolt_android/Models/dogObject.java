package siliconempirellc.barkorbolt_android.Models;

import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.Date;

/**
 * Created by khuramchaudhry on 4/9/17.
 * Data Model for the dogUser.
 */

public class dogObject {

    Drawable dogImage;
    String name;
    String email;
    Date dateJoined;
    String breed;
    String gender;
    String ownerName;

    public dogObject(Drawable dogImage, String name, String email, Date dateJoined, String breed,
                     String gender, String ownerName) {
        this.dogImage = dogImage;
        this.name = name;
        this.email = email;
        this.dateJoined = dateJoined;
        this.breed = breed;
        this.gender = gender;
        this.ownerName = ownerName;
    }

    public Drawable getDogImage() {
        return this.dogImage;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public Date getDateJoined() {
        return this.dateJoined;
    }

    public String getBreed() {
        return this.breed;
    }

    public String getGender() {
        return this.gender;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

}
