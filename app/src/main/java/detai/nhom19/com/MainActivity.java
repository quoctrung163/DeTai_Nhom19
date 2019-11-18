package detai.nhom19.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText emailId, password, repassword;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    ConstraintLayout constraintLayout;
    TextView txtTimeMsg;

    TextView txtTiengViet;
    TextView txtTiengAnh;
    TextView txtTiengNhat;

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.edtEmailSignUp);
        password = findViewById(R.id.edtPassSignUp);
        repassword = findViewById(R.id.edtRePassSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.txtSignUp);

        txtTiengViet = findViewById(R.id.txtTiengViet);
        txtTiengAnh = findViewById(R.id.txtTiengAnh);
        txtTiengNhat = findViewById(R.id.txtTiengNhat);


        constraintLayout = findViewById(R.id.container_Signup);
        txtTimeMsg = findViewById(R.id.txtTimeMsgSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String rePwd = repassword.getText().toString();


                String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                CharSequence inputStr = email;
                Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputStr);


                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();

                } else if (!(matcher.matches())) {
                    emailId.setError("Email format error");
                    emailId.requestFocus();

                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();

                } else if (pwd.length() < 6) {
                    password.setError("Please enter your password > 6 character");
                    password.requestFocus();

                } else if (rePwd.isEmpty()) {
                    repassword.setError("Please enter your password");
                    repassword.requestFocus();

                } else if (!(rePwd.equalsIgnoreCase(pwd))) {
                    repassword.setError("Wrong re-password");
                    repassword.requestFocus();

                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();

                } else if (!(email.isEmpty() && pwd.isEmpty())) {

                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            emailId.setText("");
                                            password.setText("");
                                            repassword.setText("");
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(MainActivity.this, VerifyActivity.class));
                                        } else {
                                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                emailId.setError("Email has already exists");
                                emailId.requestFocus();
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Error Ocurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            }
        });

        txtTiengViet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("vi");
            }
        });

        txtTiengAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
            }
        });

        txtTiengNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("ja");
            }
        });

        Calendar c = Calendar.getInstance();

        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            // morning
            constraintLayout.setBackground(getDrawable(R.drawable.morning));
            txtTimeMsg.setText("Good Morning");

        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            // afternoon
            constraintLayout.setBackground(getDrawable(R.drawable.afternoon));
            txtTimeMsg.setText("Good Afternoon");

        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            // evening
            constraintLayout.setBackground(getDrawable(R.drawable.evening));
            txtTimeMsg.setText("Good Evening");

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            // night
            constraintLayout.setBackground(getDrawable(R.drawable.night));
            txtTimeMsg.setText("Good Night");

        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
