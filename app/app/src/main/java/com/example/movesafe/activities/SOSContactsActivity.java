package com.example.movesafe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movesafe.R;

import java.util.ArrayList;

public class SOSContactsActivity extends AppCompatActivity {

    EditText firstNameEditText;
    EditText secondNameEditText;
    EditText thirdNameEditText;
    EditText firstContactEditText;
    EditText secondContactEditText;
    EditText thirdContactEditText;
    Button confirmButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soscontacts);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("SOS Contacts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firstNameEditText = findViewById(R.id.sosContactsViewFirstNameEditText);
        secondNameEditText = findViewById(R.id.sosContactsViewSecondNameEditText);
        thirdNameEditText = findViewById(R.id.sosContactsViewThirdNameEditText);
        firstContactEditText = findViewById(R.id.sosContactsViewFirstContactEditText);
        secondContactEditText = findViewById(R.id.sosContactsViewSecondContactEditText);
        thirdContactEditText = findViewById(R.id.sosContactsViewThirdContactEditText);

        confirmButton = findViewById(R.id.sosContactsViewConfirmButton);
        confirmButton.setOnClickListener(this::handleClickConfirmButton);

        cancelButton = findViewById(R.id.sosContactsViewCancelButton);
        cancelButton.setOnClickListener(this::handleClickCancelButton);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void handleClickConfirmButton(View view) {
        String firstNameText = firstNameEditText.getText().toString();
        String secondNameText = secondNameEditText.getText().toString();
        String thirdNameText = thirdNameEditText.getText().toString();
        String firstContactText = firstContactEditText.getText().toString();
        String secondContactText = secondContactEditText.getText().toString();
        String thirdContactText = thirdContactEditText.getText().toString();

        ArrayList<Pair<String, String>> details = new ArrayList<>();
        if (!firstNameText.isEmpty()) {
            if (firstContactText.isEmpty()) {
                Toast.makeText(this, "Please provide a contact number for " + firstNameText, Toast.LENGTH_SHORT).show();
            } else if (firstContactText.length() != 10) {
                Toast.makeText(this, "Please provide a valid contact number for " + firstNameText, Toast.LENGTH_SHORT).show();
            } else {
                details.add(new Pair<>(firstNameText, firstContactText));
            }
        }

        if (!secondNameText.isEmpty()) {
            if (secondContactText.isEmpty()) {
                Toast.makeText(this, "Please provide a contact number for " + secondNameText, Toast.LENGTH_SHORT).show();
            } else if (secondContactText.length() != 10) {
                Toast.makeText(this, "Please provide a valid contact number for " + secondNameText, Toast.LENGTH_SHORT).show();
            } else {
                details.add(new Pair<>(secondNameText, secondContactText));
            }
        }

        if (!thirdNameText.isEmpty()) {
            if (thirdContactText.isEmpty()) {
                Toast.makeText(this, "Please provide a contact number for " + thirdNameText, Toast.LENGTH_SHORT).show();
            } else if (thirdContactText.length() != 10) {
                Toast.makeText(this, "Please provide a valid contact number for " + thirdNameText, Toast.LENGTH_SHORT).show();
            } else {
                details.add(new Pair<>(thirdNameText, thirdContactText));
            }
        }

        if (details.isEmpty()) {
            Toast.makeText(this, "Please provide at least one contact", Toast.LENGTH_SHORT).show();
        } else {
//            send data to server
            finish();
        }
    }

    private void handleClickCancelButton(View view) {
        finish();
    }
}