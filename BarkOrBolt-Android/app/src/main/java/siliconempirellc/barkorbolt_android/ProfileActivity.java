package siliconempirellc.barkorbolt_android;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import siliconempirellc.barkorbolt_android.Models.dogObject;
import siliconempirellc.barkorbolt_android.Utils.PermissionUtils;
import siliconempirellc.barkorbolt_android.Utils.backendServer;

/**
 * Created by khuramchaudhry on 4/9/17.
 * This activity display the user profile.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1, PERMISSIONS_REQUEST = 2;

    private backendServer server;
    private dogObject user;
    private ImageView userImage;
    private ImageButton backButton, cameraButton, menuButton;
    private TextView nameText, emailText, dateJoinedText, breedText, genderText, ownerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
    }

    /**
     * Initializing values, listeners, and other stuff.
     * */
    public void initialize() {

        server = new backendServer(ProfileActivity.this);

        userImage = (ImageView) findViewById(R.id.profile_photo);
        backButton = (ImageButton) findViewById(R.id.backButton);
        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        menuButton = (ImageButton) findViewById(R.id.drop_down_option_menu);
        nameText = (TextView) findViewById(R.id.userName);
        emailText = (TextView) findViewById(R.id.userEmail);
        dateJoinedText = (TextView) findViewById(R.id.userDateJoined);
        breedText = (TextView) findViewById(R.id.dogBreed);
        genderText = (TextView) findViewById(R.id.userGender);
        ownerText = (TextView) findViewById(R.id.dogOwner);
        user = server.getUserFromServer();

        //Sets the fields.
        if(user != null) {
            userImage.setImageBitmap(getCircularBitmap(getImage()));
            nameText.setText(user.getName());
            emailText.setText(user.getEmail());
            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
            long dateInLong = user.getDateJoined().getTime();
            String date = dateFormat.format(dateInLong);
            dateJoinedText.setText("Date Joinded: " + date);
            breedText.setText("Dog Breed: " + user.getBreed());
            genderText.setText("Dog Gender: " + user.getGender());
            ownerText.setText("Owner: "+ user.getOwnerName());
        }

        //Setting up Listeners.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(askPermission()) {
                    selectImage();
                }
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });
    }

    /**
     * This function brings up a dialog for user to how choose his/her new profile image.
     * */
    private void selectImage() {
        CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        final CharSequence[] options = items;

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("How would you like to take a image?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    cameraIntent();

                } else if (options[item].equals("Choose from Library")) {
                    galleryIntent();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * Function to grab a image from a gallery.
     * */
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    /**
     * Function to grab from the camera.
     * */
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * Function to handle result of picture taken.
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext()
                            .getContentResolver(), data.getData());
                    saveImage(thumbnail);
                    userImage.setImageBitmap(getCircularBitmap(thumbnail));
                } catch (IOException e) {
                    e.printStackTrace(); }
            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                saveImage(thumbnail);
                userImage.setImageBitmap(getCircularBitmap(thumbnail));
            }
            Log.d(TAG, "Successful image capture");
        }
    }

    /**
     * This function ask the required permissions.
     * */
    public boolean askPermission() {
        return PermissionUtils.requestPermission(this, PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * This function checks if the permissions are granted.
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!PermissionUtils.permissionGranted(requestCode, PERMISSIONS_REQUEST, grantResults)) {
            Toast.makeText(ProfileActivity.this, "You must grant the app permissions!",
                    Toast.LENGTH_LONG).show();
        } else {
            selectImage();
        }
    }

    /**
     * This function shows the menu to edit the name and the email.
     * */
    private void showMenu() {
        PopupMenu popup = new PopupMenu(this, menuButton);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_profile, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(
                    android.view.MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_name:
                        showDialog();
                        return true;

                    case R.id.action_edit_email:
                        showDialog();
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * This function takes in a bitmap and return a circular version of it.
     * */
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        if(bitmap == null) {
            return null;
        }
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(),
                    Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * This function shows a dialog telling the user the feature is not available.
     * */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Sorry")
                .setMessage("This feature is not available.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * This function gets the profile Image saved on the device if there is one.
     */
    public Bitmap getImage() {
        Bitmap bitmap;
        File imgFile = new File(Environment.getExternalStorageDirectory() +
                "/Bark_App_Data/profileImage.jpg");
        if(imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * Save image to the device. The image is saved in a folder called Bark_App_Data and the image
     * is named profileImage.jpg.
     */
    public void saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File mainDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Bark_App_Data");
        if (!mainDirectory.exists() && !mainDirectory.mkdirs()) {
                Toast.makeText(ProfileActivity.this, "Please allow the app to save/read data.",
                        Toast.LENGTH_LONG).show();
        } else {

            File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Bark_App_Data/", "profileImage.jpg");
            FileOutputStream fo;
            try {
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                Log.d(TAG, "File Successfully Saved!!");
            } catch (FileNotFoundException e) {
                Log.d(TAG, e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
