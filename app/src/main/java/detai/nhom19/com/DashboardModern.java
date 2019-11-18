package detai.nhom19.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;


public class DashboardModern extends AppCompatActivity {
    LinearLayout linearLayout;
    ScrollView scrollViewHomePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_modern);

        linearLayout = findViewById(R.id.linearLogOut);
        scrollViewHomePage = findViewById(R.id.scrollViewHomePage);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(DashboardModern.this, LoginActivity.class);
                startActivity(intToMain);
            }
        });

        Calendar c = Calendar.getInstance();

        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            // morning
            scrollViewHomePage.setBackground(getDrawable(R.drawable.blur_morning));

        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            // afternoon
            scrollViewHomePage.setBackground(getDrawable(R.drawable.blur_afternoon));

        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            // evening
            scrollViewHomePage.setBackground(getDrawable(R.drawable.blur_evening));

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            // night
            scrollViewHomePage.setBackground(getDrawable(R.drawable.blur_night));
//            txtTimeMsg.setText(getResources().getString(R.string.email));

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
