package com.codetek.bloodapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.bloodapp.Models.DB.BloodRequest;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.ViewHolder>{

    ArrayList<BloodRequest> dataList;
    Context context;

    public ProgressDialog progress;
    RequestQueue queue;

    public BloodRequestAdapter(Context context,ArrayList<BloodRequest> dataList) {
        this.context=context;
        this.dataList = dataList;

        queue = Volley.newRequestQueue(context);
        progress=new ProgressDialog(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView blood_record_type,blood_record_requested_by,blood_record_contact;
        public ImageView blood_record_delete_btn;
        public ViewHolder(View view) {
            super(view);
            blood_record_type =  view.findViewById(R.id.blood_record_type);
            blood_record_requested_by =  view.findViewById(R.id.blood_record_requested_by);
            blood_record_contact =  view.findViewById(R.id.blood_record_contact);
            blood_record_delete_btn =  view.findViewById(R.id.blood_record_delete_btn);
        }
    }

    @NonNull
    @Override
    public BloodRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_request_blood_record, parent, false);
        BloodRequestAdapter.ViewHolder viewHolder = new BloodRequestAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BloodRequestAdapter.ViewHolder holder, int position) {
        BloodRequest data=dataList.get(position);
        holder.blood_record_type.setText(Utils.getBloodTypeNameList()[data.getBloodtype()]);
        holder.blood_record_requested_by.setText(data.getName());
        holder.blood_record_contact.setText(data.getTel());

        if(Utils.getUser().getUsertype()==1 || Utils.getUser().getUsertype()==2){


            holder.blood_record_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringRequest sr = new StringRequest(Request.Method.POST, Routes.DELETE_BR, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress.hide();
                            removeAt(position);
                            Toast.makeText(context,"Process Completed Successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getMessage());
                            progress.hide();
                            Toast.makeText(context, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String,String> dataDelete= new HashMap<>();
                            dataDelete.put("id",String.valueOf(data.getId()));
                            return dataDelete;
                        }
                    };
                    queue.add(sr);
                }
            });

        }else{
            holder.blood_record_delete_btn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void removeAt(int position) {
        System.out.println(position);
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }
}
