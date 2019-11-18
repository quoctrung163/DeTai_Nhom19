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
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;

    ConstraintLayout constraintLayout;
    TextView txtTimeMsg;

    TextView txtTiengViet;
    TextView txtTiengAnh;
    TextView txtTiengNhat;
    TextView txtForGotPass;

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        finish();
        startActivity(refresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.edtEmailSignIn);
        password = findViewById(R.id.edtPassSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUp = findViewById(R.id.txtSignUp);

        txtTiengViet = findViewById(R.id.txtTiengViet);
        txtTiengAnh = findViewById(R.id.txtTiengAnh);
        txtTiengNhat = findViewById(R.id.txtTiengNhat);

        txtForGotPass = findViewById(R.id.txtForgotPass);

        constraintLayout = findViewById(R.id.container_login);
        txtTimeMsg = findViewById(R.id.txtTimeMsgLogin);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();

                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();

                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();

                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Wrong Email or Password, Please Login Again", Toast.LENGTH_SHORT).show();

                            } else {
                                if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                    Intent intToHome = new Intent(LoginActivity.this, DashboardModern.class);
                                    startActivity(intToHome);
                                } else {
                                    emailId.setText("");
                                    password.setText("");
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(LoginActivity.this, VerifyActivity.class));
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Error Ocurred!", Toast.LENGTH_SHORT).show();
                }

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

        txtTiengViet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("vi");
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intSignUp);
            }
        });

        txtForGotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
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
//            txtTimeMsg.setText(getResources().getString(R.string.email));
            txtTimeMsg.setText("Good Night");

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, DashboardModern.class));
            finish();
        }
    }
}
