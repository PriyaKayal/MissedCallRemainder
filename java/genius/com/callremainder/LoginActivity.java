package genius.com.callremainder;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton loginButton, signinButton;
    private TextInputEditText userNameText, passwordText, mobileText;

    private DataStorage dataStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dataStorage = new DataStorage(LoginActivity.this);

        userNameText = (TextInputEditText) findViewById(R.id.username);
        passwordText = (TextInputEditText) findViewById(R.id.password);
//        mobileText = (TextInputEditText) findViewById(R.id.mobile_number);

        loginButton = (AppCompatButton) findViewById(R.id.login);
        signinButton = (AppCompatButton) findViewById(R.id.signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignIn();
            }
        });

    }

    private void performLogin() {
        if(userNameText.getText().toString().length() != 0 && passwordText.getText().toString().length() != 0){
            if(userNameText.getText().toString().equals(dataStorage.getData("username"))
                    && passwordText.getText().toString().equals(dataStorage.getData("password"))){
                goToHome();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Invalid Credentials", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Fields Cannot be left blank", Snackbar.LENGTH_SHORT).show();

        }
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void performSignIn() {
        if (userNameText.getText().toString().length() != 0 && passwordText.getText().toString().length() != 0) {
            dataStorage.saveData("username", userNameText.getText().toString().trim());
            dataStorage.saveData("password", passwordText.getText().toString().trim());

            Snackbar.make(findViewById(android.R.id.content), "Account Created, try login now!", Snackbar.LENGTH_SHORT).show();

            userNameText.setText("");
            passwordText.setText("");
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Fields Cannot be left blank", Snackbar.LENGTH_SHORT).show();
        }
    }
}
