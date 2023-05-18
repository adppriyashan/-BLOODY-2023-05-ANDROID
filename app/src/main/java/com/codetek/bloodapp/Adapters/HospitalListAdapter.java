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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;
import com.codetek.bloodapp.Views.ManageHospitals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.ViewHolder>{

    ArrayList<Hospital> dataList;
    Context context;

    public ProgressDialog progress;
    RequestQueue queue;

    public HospitalListAdapter(Context context,ArrayList<Hospital> dataList) {
        this.context=context;
        this.dataList = dataList;

        queue = Volley.newRequestQueue(context);
        progress=new ProgressDialog(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hospital_record_name,hospital_record_address,hospital_record_phone1,hospital_record_phone2,hospital_record_phone3,hospital_record_status;
        public ImageView hospital_record_delete_btn;
        public CardView hospital_record_cons_view;
        public ViewHolder(View view) {
            super(view);
            hospital_record_name =  view.findViewById(R.id.hospital_record_name);
            hospital_record_address =  view.findViewById(R.id.hospital_record_address);
            hospital_record_phone1 =  view.findViewById(R.id.hospital_record_phone1);
            hospital_record_phone2 =  view.findViewById(R.id.hospital_record_phone2);
            hospital_record_phone3 =  view.findViewById(R.id.hospital_record_phone3);
            hospital_record_status =  view.findViewById(R.id.hospital_record_status);
            hospital_record_delete_btn =  view.findViewById(R.id.hospital_record_delete_btn);
            hospital_record_cons_view=view.findViewById(R.id.hospital_record_cons_view);
        }
    }

    @NonNull
    @Override
    public HospitalListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_hospital_record, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalListAdapter.ViewHolder holder, int position) {
        Hospital data=dataList.get(position);
        holder.hospital_record_name.setText(data.getName());
        holder.hospital_record_address.setText(data.getAddress());
        holder.hospital_record_phone1.setText(data.getPhone1());
        holder.hospital_record_phone2.setText(data.getPhone2());
        holder.hospital_record_phone3.setText(data.getPhone3());

        if(Utils.getUser().getUsertype()==1 || Utils.getUser().getUsertype()==2){
            holder.hospital_record_status.setText(data.getStatus()==1?"ACTIVE":"INACTIVE");
            holder.hospital_record_status.setTextColor(data.getStatus()==1? ContextCompat.getColor(holder.itemView.getContext(), R.color.green) :ContextCompat.getColor(holder.itemView.getContext(), R.color.primary));

            holder.hospital_record_cons_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(holder.itemView.getContext(), ManageHospitals.class);
                    intent.putExtra("id",data.getId());
                    intent.putExtra("hospitalName",data.getName() );
                    intent.putExtra("hospitalDesc",data.getAddress() );
                    intent.putExtra("hospitalPhone1",data.getPhone1() );
                    intent.putExtra("hospitalPhone2",data.getPhone2() );
                    intent.putExtra("hospitalPhone3",data.getPhone3() );
                    intent.putExtra("hospitalLng",data.getLng() );
                    intent.putExtra("hospitalLtd",data.getLtd() );

                    intent.putExtra("hospital_aplus",data.getAplus() );
                    intent.putExtra("hospital_aneg",data.getAneg() );
                    intent.putExtra("hospital_bplus",data.getBplus() );
                    intent.putExtra("hospital_bneg",data.getBneg() );
                    intent.putExtra("hospital_oplus",data.getOplus() );
                    intent.putExtra("hospital_oneg",data.getOneg() );
                    intent.putExtra("hospital_abplus",data.getAbplus() );
                    intent.putExtra("hospital_abneg",data.getAbneg() );
                    intent.putExtra("district",data.getDistrict());

                    holder.itemView.getContext().startActivity(intent);
                }
            });

            holder.hospital_record_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringRequest sr = new StringRequest(Request.Method.POST, Routes.DELETE_HOSPITAL, new Response.Listener<String>() {
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
            holder.hospital_record_status.setVisibility(View.GONE);
            holder.hospital_record_delete_btn.setVisibility(View.GONE);
            holder.hospital_record_phone3.setPadding(0,0,0,24);
        }
//
//        holder.liveView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                context.startActivity(new Intent(context, DeviceLive.class).putExtra("device", device.id ).putExtra("status", device.status  ));
//            }
//        });
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
