package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SuggestedRecipesActivity extends AppCompatActivity {

 private DatabaseHelper db;
 private RecyclerView recyclerView;
 private TextView messageText;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_recipes);

  db = new DatabaseHelper(this);

  recyclerView = findViewById(R.id.recyclerRecipes);
  messageText = findViewById(R.id.txtRecipeMessage);

  View btnBack = findViewById(R.id.btnBack);
  if (btnBack != null) {
   btnBack.setOnClickListener(v -> finish());
  }

  recyclerView.setLayoutManager(new LinearLayoutManager(this));

  loadSuggestedRecipes();
 }

 /**
  * Loads ONLY recipes that the user can make completely.
  *
  * IMPORTANT:
  * A recipe is shown only when EVERY required ingredient
  * is present in the pantry in a sufficient quantity.
  */
 private void loadSuggestedRecipes() {

  List<Recipe> allRecipes = db.getRecipes();
  List<PantryItem> pantry = db.getPantry();

  List<Recipe> matchingRecipes = new ArrayList<>();

  for (Recipe recipe : allRecipes) {

   if (canMakeRecipe(recipe, pantry)) {
    matchingRecipes.add(recipe);
   }
  }

  RecipeAdapter adapter = new RecipeAdapter(matchingRecipes, recipe -> {
   Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
   intent.putExtra("id", recipe.id);
   startActivity(intent);
  });
  recyclerView.setAdapter(adapter);

  if (matchingRecipes.isEmpty()) {

   messageText.setText(
           "No recipes match your pantry yet.\n\n" +
                   "Add more ingredients to make more recipes."
   );

   messageText.setVisibility(TextView.VISIBLE);
   recyclerView.setVisibility(RecyclerView.GONE);

  } else {

   messageText.setText(
           "Only recipes you can make right now are shown."
   );

   messageText.setVisibility(TextView.VISIBLE);
   recyclerView.setVisibility(RecyclerView.VISIBLE);
  }
 }

 /**
  * STRICT MATCHING RULE
  *
  * A recipe can only be suggested when:
  *
  * 1. Every required ingredient exists in the pantry.
  * 2. The pantry quantity is at least the required quantity.
  * 3. Units are compatible or convertible.
  *
  * Partial matches are NEVER accepted.
  */
 private boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantry) {

  for (RecipeIngredient required : recipe.ingredients) {

   double availableQuantity = 0;

   for (PantryItem pantryItem : pantry) {

    String pantryName = normalizeIngredientName(pantryItem.name);
    String requiredName = normalizeIngredientName(required.name);

    if (!pantryName.equals(requiredName)) {
     continue;
    }

    if (!unitCompatible(pantryItem.unit, required.unit)) {
     continue;
    }

    availableQuantity += convertQuantity(
            pantryItem.quantity,
            pantryItem.unit,
            required.unit
    );
   }

   /*
    * If the ingredient does not exist OR there is not
    * enough of it, the ENTIRE recipe fails.
    */
   if (availableQuantity + 0.000001 < required.quantity) {
    return false;
   }
  }

  /*
   * Every required ingredient passed.
   * Therefore the recipe can be made completely.
   */
  return true;
 }

 /**
  * Makes simple ingredient variations match.
  *
  * Examples:
  * tomato     -> tomato
  * tomatoes   -> tomato
  * potato     -> potato
  * potatoes   -> potato
  * onion      -> onion
  * onions     -> onion
  */
 private String normalizeIngredientName(String name) {

  if (name == null) {
   return "";
  }

  String value = name
          .toLowerCase(Locale.ROOT)
          .trim()
          .replaceAll("\\s+", " ");

  // Remove punctuation that could cause trivial mismatches.
  value = value.replaceAll("[^a-z0-9 ]", "");

  // Common plural: tomatoes -> tomato
  if (value.endsWith("ies") && value.length() > 3) {
   value = value.substring(0, value.length() - 3) + "y";
  }

  // potatoes -> potato
  else if (value.endsWith("oes") && value.length() > 3) {
   value = value.substring(0, value.length() - 2);
  }

  // dishes -> dish
  else if (value.endsWith("ses")
          || value.endsWith("xes")
          || value.endsWith("zes")
          || value.endsWith("ches")
          || value.endsWith("shes")) {

   value = value.substring(0, value.length() - 2);
  }

  // General plural: eggs -> egg
  else if (value.endsWith("s")
          && !value.endsWith("ss")
          && value.length() > 2) {

   value = value.substring(0, value.length() - 1);
  }

  return value;
 }

 /**
  * Checks whether two units can be compared.
  */
 private boolean unitCompatible(String pantryUnit, String requiredUnit) {

  String pantry = normalizeUnit(pantryUnit);
  String required = normalizeUnit(requiredUnit);

  if (pantry.equals(required)) {
   return true;
  }

  // Weight conversion
  if ((pantry.equals("g") || pantry.equals("kg"))
          && (required.equals("g") || required.equals("kg"))) {
   return true;
  }

  // Volume conversion
  if ((pantry.equals("ml") || pantry.equals("l"))
          && (required.equals("ml") || required.equals("l"))) {
   return true;
  }

  return false;
 }

 /**
  * Converts the pantry quantity into the recipe's required unit.
  */
 private double convertQuantity(
         double quantity,
         String fromUnit,
         String toUnit) {

  String from = normalizeUnit(fromUnit);
  String to = normalizeUnit(toUnit);

  if (from.equals(to)) {
   return quantity;
  }

  // kg -> g
  if (from.equals("kg") && to.equals("g")) {
   return quantity * 1000.0;
  }

  // g -> kg
  if (from.equals("g") && to.equals("kg")) {
   return quantity / 1000.0;
  }

  // L -> ml
  if (from.equals("l") && to.equals("ml")) {
   return quantity * 1000.0;
  }

  // ml -> L
  if (from.equals("ml") && to.equals("l")) {
   return quantity / 1000.0;
  }

  /*
   * This should only be reached when the units are already
   * considered compatible. Returning zero is safer than
   * incorrectly treating incompatible units as equal.
   */
  return 0;
 }

 /**
  * Normalises common ways of writing units.
  */
 private String normalizeUnit(String unit) {

  if (unit == null) {
   return "";
  }

  String value = unit
          .toLowerCase(Locale.ROOT)
          .trim();

  switch (value) {

   case "gram":
   case "grams":
   case "g":
    return "g";

   case "kilogram":
   case "kilograms":
   case "kg":
    return "kg";

   case "millilitre":
   case "millilitres":
   case "milliliter":
   case "milliliters":
   case "ml":
    return "ml";

   case "litre":
   case "litres":
   case "liter":
   case "liters":
   case "l":
    return "l";

   case "piece":
   case "pieces":
   case "item":
   case "items":
   case "pc":
    return "item";

   case "tablespoon":
   case "tablespoons":
   case "tbsp":
    return "tbsp";

   case "teaspoon":
   case "teaspoons":
   case "tsp":
    return "tsp";

   case "cup":
   case "cups":
    return "cup";

   default:
    return value;
  }
 }
}