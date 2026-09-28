package com.example.smartpantry;
import android.view.*;import android.widget.*;import androidx.recyclerview.widget.RecyclerView;import java.util.*;
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH>{
 interface Listener{void open(Recipe r);}List<Recipe> data;Listener listener;RecipeAdapter(List<Recipe>d,Listener l){data=d;listener=l;}
 public VH onCreateViewHolder(ViewGroup p,int v){return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_recipe,p,false));}
 public void onBindViewHolder(VH h,int i){Recipe r=data.get(i);h.name.setText(r.name);h.itemView.setOnClickListener(v->listener.open(r));}
 public int getItemCount(){return data.size();}static class VH extends RecyclerView.ViewHolder{TextView name;VH(View v){super(v);name=v.findViewById(R.id.txtRecipeName);}}
}