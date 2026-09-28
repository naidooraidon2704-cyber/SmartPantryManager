package com.example.smartpantry;
import android.content.*; import android.database.sqlite.*; import android.database.Cursor; import java.util.*;
public class DatabaseHelper extends SQLiteOpenHelper {
 private static final String DB="smart_pantry.db"; private static final int VER=1;
 public DatabaseHelper(Context c){super(c,DB,null,VER);}
 public void onCreate(SQLiteDatabase db){
  db.execSQL("CREATE TABLE pantry_items(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL,expiry_date TEXT)");
  db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,instructions TEXT NOT NULL)");
  db.execSQL("CREATE TABLE recipe_ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT,recipe_id INTEGER NOT NULL,ingredient_name TEXT NOT NULL,required_quantity REAL NOT NULL,unit TEXT NOT NULL,FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
  seed(db);
 }
 public void onUpgrade(SQLiteDatabase db,int oldV,int newV){db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");db.execSQL("DROP TABLE IF EXISTS recipes");db.execSQL("DROP TABLE IF EXISTS pantry_items");onCreate(db);}
 private void seed(SQLiteDatabase db){
  addRecipe(db,"Vegetable Fried Rice","Cook rice. Stir-fry vegetables, add rice and season. Cook until hot.",new String[][]{{"rice","2","cup"},{"carrot","1","item"},{"peas","1","cup"},{"egg","2","item"},{"soy sauce","2","tbsp"}});
  addRecipe(db,"Chicken Fried Rice","Cook chicken thoroughly. Stir-fry vegetables, rice and egg, then add soy sauce.",new String[][]{{"rice","2","cup"},{"chicken","250","g"},{"carrot","1","item"},{"peas","1","cup"},{"egg","2","item"},{"soy sauce","2","tbsp"}});
  addRecipe(db,"Spaghetti Bolognese","Brown beef with onion and garlic. Add tomato and simmer. Serve over cooked spaghetti.",new String[][]{{"spaghetti","200","g"},{"beef mince","250","g"},{"tomato","2","item"},{"onion","1","item"},{"garlic","2","item"}});
  addRecipe(db,"Chicken Pasta","Cook pasta and chicken. Combine with tomato, onion and garlic sauce.",new String[][]{{"pasta","200","g"},{"chicken","250","g"},{"tomato","2","item"},{"onion","1","item"},{"garlic","2","item"}});
  addRecipe(db,"Tuna Pasta","Cook pasta. Mix tuna, tomato, onion and garlic; combine and heat.",new String[][]{{"pasta","200","g"},{"tuna","1","can"},{"tomato","2","item"},{"onion","1","item"}});
  addRecipe(db,"Vegetable Omelette","Beat eggs, add vegetables and cook in a lightly oiled pan.",new String[][]{{"egg","3","item"},{"onion","1","item"},{"tomato","1","item"},{"pepper","1","item"}});
  addRecipe(db,"Chicken Curry","Brown chicken. Add onion, garlic and curry powder, then simmer with tomato.",new String[][]{{"chicken","300","g"},{"onion","1","item"},{"garlic","2","item"},{"tomato","2","item"},{"curry powder","2","tbsp"}});
  addRecipe(db,"Beef Curry","Brown beef, add onion, garlic, curry powder and tomato, then simmer until tender.",new String[][]{{"beef","300","g"},{"onion","1","item"},{"garlic","2","item"},{"tomato","2","item"},{"curry powder","2","tbsp"}});
  addRecipe(db,"Vegetable Curry","Cook onion and garlic, add vegetables and curry powder, then simmer.",new String[][]{{"potato","2","item"},{"carrot","2","item"},{"onion","1","item"},{"garlic","2","item"},{"curry powder","2","tbsp"}});
  addRecipe(db,"Egg Fried Rice","Stir-fry rice and vegetables. Add beaten eggs and soy sauce and cook through.",new String[][]{{"rice","2","cup"},{"egg","2","item"},{"peas","1","cup"},{"soy sauce","2","tbsp"}});
  addRecipe(db,"Chicken Stir Fry","Stir-fry chicken and vegetables until cooked. Add soy sauce and serve.",new String[][]{{"chicken","250","g"},{"carrot","1","item"},{"pepper","1","item"},{"onion","1","item"},{"soy sauce","2","tbsp"}});
  addRecipe(db,"Tomato Pasta","Cook pasta. Simmer tomato, onion and garlic; combine with pasta.",new String[][]{{"pasta","200","g"},{"tomato","3","item"},{"onion","1","item"},{"garlic","2","item"}});
  addRecipe(db,"Garlic Butter Chicken","Pan-fry chicken, add garlic and butter, then cook until golden and done.",new String[][]{{"chicken","300","g"},{"garlic","3","item"},{"butter","2","tbsp"}});
  addRecipe(db,"Potato Curry","Cook potato with onion, garlic, tomato and curry powder until tender.",new String[][]{{"potato","3","item"},{"onion","1","item"},{"garlic","2","item"},{"tomato","2","item"},{"curry powder","2","tbsp"}});
  addRecipe(db,"Chickpea Curry","Cook onion and garlic, add chickpeas, tomato and curry powder and simmer.",new String[][]{{"chickpeas","1","can"},{"onion","1","item"},{"garlic","2","item"},{"tomato","2","item"},{"curry powder","2","tbsp"}});
  addRecipe(db,"French Toast","Whisk eggs and milk. Dip bread and fry until golden.",new String[][]{{"bread","4","item"},{"egg","2","item"},{"milk","100","ml"}});
  addRecipe(db,"Pancakes","Mix flour, milk and eggs into batter. Fry portions until golden.",new String[][]{{"flour","200","g"},{"milk","250","ml"},{"egg","2","item"}});
  addRecipe(db,"Vegetable Soup","Simmer potato, carrot, onion and garlic in water until vegetables are tender.",new String[][]{{"potato","2","item"},{"carrot","2","item"},{"onion","1","item"},{"garlic","2","item"}});
  addRecipe(db,"Chicken Soup","Simmer chicken with carrot, onion, potato and garlic until cooked.",new String[][]{{"chicken","250","g"},{"carrot","2","item"},{"onion","1","item"},{"potato","2","item"},{"garlic","2","item"}});
  addRecipe(db,"Tuna Sandwich","Mix tuna with tomato and onion and place inside bread.",new String[][]{{"bread","2","item"},{"tuna","1","can"},{"tomato","1","item"},{"onion","1","item"}});
 }
 private void addRecipe(SQLiteDatabase db,String n,String ins,String[][] ing){long id=db.insert("recipes",null,cv("name",n,"instructions",ins));for(String[] x:ing){ContentValues v=new ContentValues();v.put("recipe_id",id);v.put("ingredient_name",x[0]);v.put("required_quantity",Double.parseDouble(x[1]));v.put("unit",x[2]);db.insert("recipe_ingredients",null,v);}}
 private ContentValues cv(String k1,String v1,String k2,String v2){ContentValues v=new ContentValues();v.put(k1,v1);v.put(k2,v2);return v;}
 public long insertPantry(PantryItem x){SQLiteDatabase d=getWritableDatabase();ContentValues v=new ContentValues();v.put("name",x.name);v.put("quantity",x.quantity);v.put("unit",x.unit);v.put("expiry_date",x.expiryDate);return d.insert("pantry_items",null,v);}
 public int updatePantry(PantryItem x){ContentValues v=new ContentValues();v.put("name",x.name);v.put("quantity",x.quantity);v.put("unit",x.unit);v.put("expiry_date",x.expiryDate);return getWritableDatabase().update("pantry_items",v,"id=?",new String[]{String.valueOf(x.id)});}
 public int deletePantry(long id){return getWritableDatabase().delete("pantry_items","id=?",new String[]{String.valueOf(id)});}
 public List<PantryItem> getPantry(){List<PantryItem> l=new ArrayList<>();Cursor c=getReadableDatabase().query("pantry_items",null,null,null,null,null,"name COLLATE NOCASE");while(c.moveToNext())l.add(new PantryItem(c.getLong(c.getColumnIndexOrThrow("id")),c.getString(c.getColumnIndexOrThrow("name")),c.getDouble(c.getColumnIndexOrThrow("quantity")),c.getString(c.getColumnIndexOrThrow("unit")),c.getString(c.getColumnIndexOrThrow("expiry_date"))));c.close();return l;}
 public PantryItem getPantry(long id){for(PantryItem x:getPantry())if(x.id==id)return x;return null;}
 public List<Recipe> getRecipes(){List<Recipe> l=new ArrayList<>();Cursor c=getReadableDatabase().query("recipes",null,null,null,null,null,"name");while(c.moveToNext()){long id=c.getLong(c.getColumnIndexOrThrow("id"));Recipe r=new Recipe(id,c.getString(c.getColumnIndexOrThrow("name")),c.getString(c.getColumnIndexOrThrow("instructions")));Cursor q=getReadableDatabase().query("recipe_ingredients",null,"recipe_id=?",new String[]{String.valueOf(id)},null,null,null);while(q.moveToNext())r.ingredients.add(new RecipeIngredient(q.getString(q.getColumnIndexOrThrow("ingredient_name")),q.getDouble(q.getColumnIndexOrThrow("required_quantity")),q.getString(q.getColumnIndexOrThrow("unit"))));q.close();l.add(r);}c.close();return l;}
}