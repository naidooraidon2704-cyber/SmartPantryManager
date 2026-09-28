package com.example.smartpantry;
import java.util.*;
public class Recipe { public long id; public String name,instructions; public List<RecipeIngredient> ingredients=new ArrayList<>();
 public Recipe(long id,String name,String instructions){this.id=id;this.name=name;this.instructions=instructions;}
}
class RecipeIngredient { public String name,unit; public double quantity; RecipeIngredient(String n,double q,String u){name=n;quantity=q;unit=u;} }