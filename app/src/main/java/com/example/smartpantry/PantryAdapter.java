package com.example.smartpantry;
import android.view.*;import android.widget.*;import androidx.recyclerview.widget.RecyclerView;import java.util.*;
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH>{
 interface Listener{void edit(PantryItem x);void delete(PantryItem x);}
 List<PantryItem> data;Listener listener; PantryAdapter(List<PantryItem>d,Listener l){data=d;listener=l;}
 public VH onCreateViewHolder(ViewGroup p,int v){return new VH(LayoutInflater.from(p.getContext()).inflate(com.example.smartpantry.R.layout.item_pantry,p,false));}
 public void onBindViewHolder(VH h,int i){PantryItem x=data.get(i);h.name.setText(x.name);h.details.setText(x.quantity+" "+x.unit+(x.expiryDate==null||x.expiryDate.isEmpty()?"":" • Expires "+x.expiryDate));h.edit.setOnClickListener(v->listener.edit(x));h.del.setOnClickListener(v->listener.delete(x));}
 public int getItemCount(){return data.size();}
 static class VH extends RecyclerView.ViewHolder{TextView name,details;Button edit,del;VH(View v){super(v);name=v.findViewById(R.id.txtName);details=v.findViewById(R.id.txtDetails);edit=v.findViewById(R.id.btnEdit);del=v.findViewById(R.id.btnDelete);}}
}