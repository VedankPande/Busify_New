package com.mitapp.busify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class sign_up extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText Name,Stop,Phone;
    FirebaseDatabase database;
    DatabaseReference reference;
    String bus_id, stop;
    private static final String USER_DETAILS = "User Details";
    private static final String SELECTED_BUS= "Selected Bus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_sign_up);
        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Spinner bus_spinner = findViewById(R.id.signup_activity_bus_spinner);
        ArrayAdapter<CharSequence> bus_adapter = ArrayAdapter.createFromResource(sign_up.this,
                R.array.buses_list, android.R.layout.simple_spinner_item);
        bus_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bus_spinner.setAdapter(bus_adapter);
        bus_spinner.setOnItemSelectedListener(this);


        Name = findViewById(R.id.signup_activity_name);
        Stop = findViewById(R.id.signup_activity_layout_stop);
        Phone = findViewById(R.id.signup_activity_phone);

        ImageButton register = findViewById(R.id.goto_mainactivity);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference();
                String name = Name.getText().toString();
                String stop = Stop.getText().toString();
                String phone = Phone.getText().toString();
                User user = new User(name, stop, bus_id, phone);
                reference.child("users").child(""+name).setValue(user);
                SharedPreferences sharedPreferences = getSharedPreferences(USER_DETAILS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SELECTED_BUS,bus_id);
                editor.apply();
                setRegisteredBoolean();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), login_activity.class));
            }

        });
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        bus_id = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void setRegisteredBoolean()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("system global variables",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Registered",true);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (signInAccount != null)
        {
            moveTaskToBack(true);
        }
        else {
            super.onBackPressed();
        }
    }
}
