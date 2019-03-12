package com.example.linkedin_electiva;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static EditText username;
    private static EditText password;
    private static TextView attempt;
    private static Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void siguiente(View view){
        Intent siguiente = new Intent(this, ProfileActivity.class);
        startActivity(siguiente);
    }
}
