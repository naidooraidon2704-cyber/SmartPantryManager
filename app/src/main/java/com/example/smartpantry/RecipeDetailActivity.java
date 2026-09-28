package com.example.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity
        extends AppCompatActivity {

 @Override
 protected void onCreate(Bundle b) {

  super.onCreate(b);

  setContentView(
          R.layout.activity_recipe_detail
  );

  DatabaseHelper db =
          new DatabaseHelper(this);

  long id =
          getIntent().getLongExtra(
                  "id",
                  -1
          );

  Recipe r = null;

  for (Recipe x : db.getRecipes()) {

   if (x.id == id) {

    r = x;
    break;
   }
  }

  if (r == null) {

   finish();

   return;
  }

  TextView nameText =
          findViewById(
                  R.id.txtDetailName
          );

  TextView ingredientsText =
          findViewById(
                  R.id.txtIngredients
          );

  TextView methodText =
          findViewById(
                  R.id.txtMethod
          );

  nameText.setText(r.name);

  StringBuilder ingredients =
          new StringBuilder();

  ingredients.append("INGREDIENTS\n\n");

  for (RecipeIngredient i :
          r.ingredients) {

   ingredients
           .append("• ")
           .append(i.quantity)
           .append(" ")
           .append(i.unit)
           .append(" ")
           .append(i.name)
           .append("\n");
  }

  ingredientsText.setText(
          ingredients.toString()
  );

  String method =
          "METHOD\n\n" +
                  r.instructions;

  methodText.setText(method);

  findViewById(
          R.id.btnBackDetail
  ).setOnClickListener(
          v -> finish()
  );
 }
}