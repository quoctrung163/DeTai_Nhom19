package detai.nhom19.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class ForgotPassActivity extends AppCompatActivity {
    Button btnResetPass;
    EditText edtEmailResetPass;

    ConstraintLayout constraintLayout;
    TextView txtTimeMsgLogin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        btnResetPass = findViewById(R.id.btnResetPassword);
        edtEmailResetPass = findViewById(R.id.edtEmailResetPass);

        constraintLayout = findViewById(R.id.container_resetPass);
        txtTimeMsgLogin = findViewById(R.id.txtTimeMsgLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(edtEmailResetPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassActivity.this, "Password sent to your email", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgotPassActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        Calendar c = Calendar.getInstance();

        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            // morning
            constraintLayout.setBackground(getDrawable(R.drawable.blur_morning));
            txtTimeMsgLogin.setText("Good Morning");

        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            // afternoon
            constraintLayout.setBackground(getDrawable(R.drawable.blur_afternoon));
            txtTimeMsgLogin.setText("Good Afternoon");

        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            // evening
            constraintLayout.setBackground(getDrawable(R.drawable.blur_evening));
            txtTimeMsgLogin.setText("Good Evening");

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            // night
            constraintLayout.setBackground(getDrawable(R.drawable.blur_night));
//            txtTimeMsg.setText(getResources().getString(R.string.email));
            txtTimeMsgLogin.setText("Good Night");

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
