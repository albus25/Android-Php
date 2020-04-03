package com.example.webserviceapi2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class RecyclerData extends RecyclerView.Adapter<RecyclerData.ViewHolder> {
    private String Tag_studentID = "studentID";
    private String Tag_studentName = "studentName";
    private String Tag_gender = "gender";
    private String Tag_birthDate = "birthDate";
    private String Tag_phoneNumber = "phoneNumber";
    private String Tag_cityID = "cityID";
    private String Tag_cityName = "cityName";

    private Context context;
    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    private ItemClickListener clickListener;
    private LayoutInflater layoutInflaters;

    public RecyclerData(Context context, ArrayList<HashMap<String, String>> list) {
//        this.context = context;
        this.layoutInflaters = LayoutInflater.from(context);
        this.list = list;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflaters.inflate(R.layout.displaystudents,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String,String> map = list.get(position);

        holder.tv1.setText(map.get(Tag_studentID));
        holder.tv2.setText(map.get(Tag_studentName));
        holder.tv3.setText(map.get(Tag_gender));
        holder.tv4.setText(map.get(Tag_birthDate));
        holder.tv5.setText(map.get(Tag_phoneNumber));
        holder.tv6.setText(map.get(Tag_cityName));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv1,tv2,tv3,tv4,tv5,tv6;
        public ViewHolder(View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.txtStudentID);
            tv2 = itemView.findViewById(R.id.txtStudentName);
            tv3 = itemView.findViewById(R.id.txtGender);
            tv4 = itemView.findViewById(R.id.txtBDt);
            tv5 = itemView.findViewById(R.id.txtPNo);
            tv6 = itemView.findViewById(R.id.txtCityName);
            itemView.setOnClickListener(this);
        }

        HashMap<String,String> getItem(int id)
        {
            return list.get(id);
        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
