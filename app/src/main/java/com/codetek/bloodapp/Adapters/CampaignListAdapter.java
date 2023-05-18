package com.codetek.bloodapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.codetek.bloodapp.Models.DB.Campaign;
import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;
import com.codetek.bloodapp.Views.ManageCampaign;
import com.codetek.bloodapp.Views.ManageHospitals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CampaignListAdapter extends RecyclerView.Adapter<CampaignListAdapter.ViewHolder>{

    ArrayList<Campaign> dataList;
    Context context;

    public ProgressDialog progress;
    RequestQueue queue;

    public CampaignListAdapter(Context context,ArrayList<Campaign> dataList) {
        this.context=context;
        this.dataList = dataList;
        queue = Volley.newRequestQueue(context);
        progress=new ProgressDialog(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView campaignRecordName,campaignRecordInfo,campaignRecordDate;
        public ImageView campaignRecordDeleteBtn;
        public CardView campaignRecordCardView;
        public ViewHolder(View view) {
            super(view);
            campaignRecordName =  view.findViewById(R.id.campaignRecordName);
            campaignRecordInfo =  view.findViewById(R.id.campaignRecordInfo);
            campaignRecordDate =  view.findViewById(R.id.campaignRecordDate);
            campaignRecordDeleteBtn =  view.findViewById(R.id.campaignRecordDeleteBtn);
            campaignRecordCardView =  view.findViewById(R.id.campaignRecordCardView);
        }
    }

    @NonNull
    @Override
    public CampaignListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_campaign_record, parent, false);
        CampaignListAdapter.ViewHolder viewHolder = new CampaignListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignListAdapter.ViewHolder holder, int position) {
        Campaign data=dataList.get(position);
        holder.campaignRecordName.setText(data.getName());
        holder.campaignRecordInfo.setText(data.getInfo());
        holder.campaignRecordDate.setText(data.getDate());

        if(Utils.getUser().getUsertype()==1 || Utils.getUser().getUsertype()==2){
            holder.campaignRecordCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(holder.itemView.getContext(), ManageCampaign.class);
                    intent.putExtra("id",data.getId());
                    intent.putExtra("name",data.getName());
                    intent.putExtra("info",data.getInfo());
                    intent.putExtra("lng",data.getLng());
                    intent.putExtra("ltd",data.getLtd());
                    intent.putExtra("date",data.getDate());

                    System.out.println(data.getDate());
                    System.out.println(data.getLng());
                    System.out.println(data.getLtd());

                    holder.itemView.getContext().startActivity(intent);
                }
            });

            holder.campaignRecordDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringRequest sr = new StringRequest(Request.Method.POST, Routes.DELETE_CAMPAIGN, new Response.Listener<String>() {
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
            holder.campaignRecordDeleteBtn.setVisibility(View.GONE);
            holder.campaignRecordCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", data.getLtd(), data.getLng());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
                }
            });
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
