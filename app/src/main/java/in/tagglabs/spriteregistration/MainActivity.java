package in.tagglabs.spriteregistration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView nameText, emailText, mobileText, sampleText;
    EditText nameEdit, emailEdit, mobileEdit, sampleEdit;
    String userName, userEmail, userMobile, userSample;
    Button button;
    FileWriter fileWriter = null;
    File filename;
    private static final String FILE_HEADER = "Name,Emial,Mobile,Quantity,Date";
    BufferedWriter bufferedWriter;
    public static final String TAG = "Users.csv";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }
        final Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/bold.otf");
        final Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/regular.otf");
        nameText = findViewById(R.id.nameText);
        nameText.setTypeface(bold);
        emailText = findViewById(R.id.emailText);
        emailText.setTypeface(bold);
        mobileText = findViewById(R.id.mobileText);
        mobileText.setTypeface(bold);
        sampleText = findViewById(R.id.sampleText);
        sampleText.setTypeface(bold);
        nameEdit = findViewById(R.id.nameEditText);
        nameEdit.setTypeface(regular);
        emailEdit = findViewById(R.id.emailEditText);
        emailEdit.setTypeface(regular);
        mobileEdit = findViewById(R.id.mobileEditText);
        mobileEdit.setTypeface(regular);
        sampleEdit = findViewById(R.id.sampleEditText);
        sampleEdit.setTypeface(regular);
        button = findViewById(R.id.button);
        button.setTypeface(bold);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                userName = nameEdit.getText().toString().trim();
                userEmail = emailEdit.getText().toString().trim();
                userMobile = mobileEdit.getText().toString().trim();
                userSample = sampleEdit.getText().toString().trim();
                if (userName.isEmpty()) {
                    nameEdit.setError("Please Enter Name");
                    nameEdit.requestFocus();
                    return;
                } else if (userEmail.isEmpty() && userEmail.matches(emailPattern)) {
                    emailEdit.setError("Please Enter E-Mail");
                    emailEdit.requestFocus();
                    return;
                } else if (userMobile.isEmpty() && userMobile.length() < 10) {
                    mobileEdit.setError("Please Enter Number");
                    mobileEdit.requestFocus();
                    return;
                } else if (userSample.isEmpty()) {
                    sampleEdit.setError("Please Enter Qunatity");
                    sampleEdit.requestFocus();
                    return;
                } else if (userName != null && !userName.isEmpty() && userEmail != null && !userEmail.isEmpty() && userMobile != null && !userMobile.isEmpty() && userSample != null && !userSample.isEmpty()) {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                        final String strDate = mdformat.format(calendar.getTime());
                        filename = new File(Environment.getExternalStorageDirectory() + "/SpriteData.csv");
                        fileWriter = new FileWriter(filename.getAbsoluteFile(), true);
                        bufferedWriter = new BufferedWriter(fileWriter);
                        int i = (int) filename.length();
                        if (i != 0) {
                            bufferedWriter.write(userName + "," + userEmail + "," + userMobile + "," + userSample  + "," + strDate+ "," + "\n");
                            Intent intent = new Intent(getApplicationContext(), thankyou.class);
                            startActivity(intent);
                            finish();
                        } else if (i == 0) {
                            bufferedWriter.write(FILE_HEADER);
                            bufferedWriter.write("\n");
                            bufferedWriter.write(userName + "," + userEmail + "," + userMobile + "," + userSample + "," + strDate + ","+ "\n");
                            Intent intent = new Intent(getApplicationContext(), thankyou.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Data Not Saved :" + e);
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Writer did'nt close:" + e);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
