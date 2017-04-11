package siliconempirellc.barkorbolt_android.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;

import siliconempirellc.barkorbolt_android.Models.dogObject;
import siliconempirellc.barkorbolt_android.R;

/**
 * Created by khuramchaudhry on 4/10/17.
 * This class handles the connections and functions from a backend server to the app.
 * As of now, this class stores information internally and there is no outside server.
 */

public class backendServer {

    private Context mContext;
    private final static String[] dogBreeds = {"Pitbull", "Bulldog", "Beagle", "Boxer",
            "Great Dane"};
    private final static int[] dogImages = {R.drawable.beagle, R.drawable.boxer,
            R.drawable.bulldog, R.drawable.pitbull, R.drawable.great_dane};
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mSharedPrefEditor;

    public backendServer(Context context) {
        this.mContext = context;
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mSharedPrefEditor = mSharedPref.edit();
    }

    /**
     * Checks if the user is signed in.
     */
    public boolean isUserLoggedInBefore() {
        return mSharedPref.getBoolean("Logged_In", Constants.IS_SIGNEDIN);
    }

    /**
     * Logs the user off.
     */
    public void logUserOff() {
        mSharedPrefEditor.putBoolean("Logged_In", Constants.IS_SIGNEDIN).apply();
    }

    /**
     * Checks if the password and email given in fact belongs to a user.
     */
    public boolean isUserOnServer(String email, String password) {
        String savedEmail = mSharedPref.getString("Email", Constants.DEFAULT_EMAIL);
        String savedPassword = mSharedPref.getString("Password", Constants.DEFAULT_PASSWORD);
        boolean isUser = savedEmail.equals(email) && savedPassword.equals(password);
        mSharedPrefEditor.putBoolean("Logged_In", isUser).apply();
        return isUser;
    }

    /**
     * This retrieves user information from the server.
     */
    public dogObject getUserFromServer() {
        String name = mSharedPref.getString("Name", Constants.DEFAULT_NAME);
        String email = mSharedPref.getString("Email", Constants.DEFAULT_EMAIL);
        Date dateJoined = new Date(mSharedPref.getLong("DateJoined", Constants.DEFAULT_DATEJOINED));
        String breed = mSharedPref.getString("Breed", Constants.DEFAULT_BREED);
        String gender = mSharedPref.getString("Gender", Constants.DEFAULT_GENDER);
        String owner = mSharedPref.getString("Owner", Constants.DEFAULT_OWNER);

        return new dogObject(ContextCompat.getDrawable(mContext, R.drawable.app_logo), name, email,
                dateJoined, breed, gender, owner);
    }

    /**
     * This retrieves other dogs from the server.
     */
    public ArrayList<dogObject> getDogsFromServer() {
        ArrayList<dogObject> dogList = new ArrayList<>();
        int numDogs = (int) (Math.random()*10);
        for(int i = 0; i < numDogs; i++) {
            dogList.add(generateDog(i));
        }
        return dogList;
    }

    /**
     * This function generates a random dog.
     */
    public dogObject generateDog(int i) {
        int dogType = (int) (Math.random()*5);
        return new dogObject(ContextCompat.getDrawable(mContext, dogImages[dogType]), "dog"+i,
                "dog"+i+"@dog.com", new Date(System.currentTimeMillis()), dogBreeds[dogType],
                "Female", "dogOwner"+i);
    }

    /**
     * This save information from a new user to the server.
     */
    public void saveUserToServer(String name, String email, String password) {
        mSharedPrefEditor.putString("Name", name).apply();
        mSharedPrefEditor.putString("Email", email).apply();
        mSharedPrefEditor.putString("Password", password).apply();
    }
}
