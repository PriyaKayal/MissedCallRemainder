package genius.com.callremainder;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private DataStorage dataStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dataStorage = new DataStorage(this);

        showSplashScreen();
    }

    private void showSplashScreen(){
        Handler loadHandler = new Handler();

        Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                openActivity(); //this opens after 2 seconds
            }
        };

        loadHandler.postDelayed(handlerRunnable, 2000); //2000 = 2 seconds, showing splash screen for 2 seconds
    }

    private void openActivity(){
        Intent intent = null;

        if(dataStorage.hasData("username") && dataStorage.hasData("password")){
            intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
