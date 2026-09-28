package com.example.smartpantry;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

 DatabaseHelper db;
 long id = -1;

 EditText name;
 EditText qty;
 EditText unit;
 EditText expiry;

 @Override
 protected void onCreate(Bundle b) {

  super.onCreate(b);

  setContentView(R.layout.activity_add_edit);

  db = new DatabaseHelper(this);

  name = findViewById(R.id.edtName);
  qty = findViewById(R.id.edtQuantity);
  unit = findViewById(R.id.edtUnit);
  expiry = findViewById(R.id.edtExpiry);

  // Load existing ingredient when editing
  if (getIntent().hasExtra("id")) {

   id = getIntent().getLongExtra("id", -1);

   PantryItem x = db.getPantry(id);

   if (x != null) {

    ((TextView) findViewById(R.id.txtTitle))
            .setText("Edit Ingredient");

    name.setText(x.name);
    qty.setText(String.valueOf(x.quantity));
    unit.setText(x.unit);
    expiry.setText(x.expiryDate);
   }
  }

  findViewById(R.id.btnSave)
          .setOnClickListener(v -> save());

  findViewById(R.id.btnCancel)
          .setOnClickListener(v -> finish());
 }

 private void save() {

  String n = name.getText()
          .toString()
          .trim()
          .replaceAll("\\s+", " ");

  String q = qty.getText()
          .toString()
          .trim();

  String u = unit.getText()
          .toString()
          .trim()
          .replaceAll("\\s+", " ");

  String e = expiry.getText()
          .toString()
          .trim()
          .replace(" ", "");

  // Validate ingredient name
  if (n.isEmpty()) {

   name.setError(
           "Ingredient name is required"
   );

   name.requestFocus();

   return;
  }

  // Validate quantity
  if (q.isEmpty()) {

   qty.setError(
           "Quantity is required"
   );

   qty.requestFocus();

   return;
  }

  double amount;

  try {

   amount = Double.parseDouble(q);

   if (amount <= 0) {

    qty.setError(
            "Quantity must be greater than 0"
    );

    qty.requestFocus();

    return;
   }

  } catch (NumberFormatException ex) {

   qty.setError(
           "Enter a valid number"
   );

   qty.requestFocus();

   return;
  }

  // Validate unit
  if (u.isEmpty()) {

   unit.setError(
           "Unit is required"
   );

   unit.requestFocus();

   return;
  }

  // Validate expiry date
  if (!e.isEmpty()) {

   if (!isValidDate(e)) {

    expiry.setError(
            "Enter a valid date such as 2026-12-25"
    );

    expiry.requestFocus();

    return;
   }
  }

  PantryItem x = new PantryItem(
          id,
          n,
          amount,
          u,
          e
  );

  if (id < 0) {

   db.insertPantry(x);

  } else {

   db.updatePantry(x);
  }

  Toast.makeText(
          this,
          id < 0
                  ? "Ingredient added successfully"
                  : "Ingredient updated successfully",
          Toast.LENGTH_SHORT
  ).show();

  finish();
 }

 private boolean isValidDate(String date) {

  SimpleDateFormat format =
          new SimpleDateFormat(
                  "yyyy-MM-dd",
                  Locale.getDefault()
          );

  format.setLenient(false);

  try {

   Date parsedDate = format.parse(date);

   return parsedDate != null;

  } catch (ParseException ex) {

   return false;
  }
 }
}