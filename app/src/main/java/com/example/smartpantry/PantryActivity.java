package com.example.smartpantry;
import android.app.*;import android.content.*;import android.os.*;import android.view.*;import androidx.appcompat.app.AppCompatActivity;import androidx.recyclerview.widget.*;import java.util.*;
public class PantryActivity extends AppCompatActivity{
 DatabaseHelper db;RecyclerView rv;PantryAdapter adapter;
 protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_pantry);db=new DatabaseHelper(this);rv=findViewById(R.id.recyclerPantry);rv.setLayoutManager(new LinearLayoutManager(this));
 findViewById(R.id.btnAdd).setOnClickListener(v->startActivity(new Intent(this,AddEditIngredientActivity.class)));
 findViewById(R.id.btnRecipes).setOnClickListener(v->startActivity(new Intent(this,SuggestedRecipesActivity.class)));
 findViewById(R.id.btnSettings).setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));load();}
 protected void onResume(){super.onResume();if(db!=null)load();}
 void load() {
  List<PantryItem> l = db.getPantry();

  adapter = new PantryAdapter(l, new PantryAdapter.Listener() {

   @Override
   public void edit(PantryItem x) {
    Intent i = new Intent(
            PantryActivity.this,
            AddEditIngredientActivity.class
    );
    i.putExtra("id", x.id);
    startActivity(i);
   }

   @Override
   public void delete(PantryItem x) {

    new AlertDialog.Builder(PantryActivity.this)
            .setTitle("Delete Ingredient")
            .setMessage(
                    "Are you sure you want to remove \"" +
                            x.name +
                            "\" from your pantry?\n\n" +
                            "This action cannot be undone."
            )
            .setPositiveButton("Delete", (dialog, which) -> {
             db.deletePantry(x.id);
             load();
            })
            .setNegativeButton("Cancel", null)
            .show();
   }
  });

  rv.setAdapter(adapter);
 }
}