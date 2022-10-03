package com.triton.johnsonapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activity.ActivityStatusActivity;
import com.triton.johnsonapp.responsepojo.ActivityGetListNumberResponse;
import com.triton.johnsonapp.responsepojo.ActivityListManagementResponse;
import com.triton.johnsonapp.responsepojo.JobNoManagementResponse;

import java.util.List;


public class ActivityBasedListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "ActivityBasedListAdapter";
    private List<ActivityGetListNumberResponse.DataBean> dataBeanList;
    private Context context;

    ActivityGetListNumberResponse.DataBean currentItem;

    private int size;

    public ActivityBasedListAdapter(Context context, List<ActivityGetListNumberResponse.DataBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;


    }

    public void filterList(List<ActivityGetListNumberResponse.DataBean> filterllist)
    {
        dataBeanList = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activitybased_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = dataBeanList.get(position);

        if(dataBeanList.get(position).getUKEY_DESC()!= null && !dataBeanList.get(position).getUKEY_DESC().isEmpty()){
            holder.txt_serv_title.setText(dataBeanList.get(position).getUKEY_DESC());
        }else{
            holder.txt_serv_title.setText("");

        }
        if(dataBeanList.get(position).getNEW_ACTIVITY()!= 0 ){
            holder.txt_noofnew.setText(": "+dataBeanList.get(position).getNEW_ACTIVITY());
        }else{
            holder.txt_noofnew.setText(": 0");

        }
        if(dataBeanList.get(position).getWIP_ACTIVITY()!= 0 ){
            holder.txt_noofpending.setText(": "+dataBeanList.get(position).getWIP_ACTIVITY());
        }else{
            holder.txt_noofpending.setText(": 0");

        }

      /*  if(currentItem.getService_update_at() != null && !currentItem.getService_update_at().equals("")){
            holder.txt_serv_updatedat.setText("Updated at :"+ currentItem.getService_update_at());
        }else{
            holder.txt_serv_updatedat.setText("Updated at : ");

        }

        if(currentItem.getService_created_at() != null && !currentItem.getService_created_at().equals("")){
            holder.txt_serv_createdat.setText("Created at : "+currentItem.getService_created_at());
        }else{
            holder.txt_serv_createdat.setText("Created at : ");

        }*/



        holder.cv_root.setOnClickListener(v -> {

            Intent intent = new Intent(context, ActivityStatusActivity.class);
          //  Intent intent = new Intent(context, ActivityJobListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("new_count",dataBeanList.get(position).getNEW_ACTIVITY());
            intent.putExtra("pause_count",dataBeanList.get(position).getWIP_ACTIVITY());
            intent.putExtra("activity_id",dataBeanList.get(position).get_id());
            intent.putExtra("UKEY",dataBeanList.get(position).getUKEY());
            intent.putExtra("UKEY_DESC",dataBeanList.get(position).getUKEY_DESC());
            intent.putExtra("form_type",dataBeanList.get(position).getForm_type());
            context.startActivity(intent);

             Log.d("id_dddddd",dataBeanList.get(position).get_id());


        });
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_serv_title,txt_serv_updatedat,txt_serv_createdat,txt_noofpending,txt_noofnew;
        ImageView img_next;
        CardView cv_root;

        public ViewHolderOne(View itemView) {
            super(itemView);
            txt_serv_title = itemView.findViewById(R.id.txt_serv_title);
            txt_serv_updatedat = itemView.findViewById(R.id.txt_serv_updatedat);
            txt_serv_createdat = itemView.findViewById(R.id.txt_serv_createdat);
            img_next = itemView.findViewById(R.id.img_next);
            cv_root = itemView.findViewById(R.id.cv_root);
            txt_noofpending = itemView.findViewById(R.id.txt_noofpending);
            txt_noofnew = itemView.findViewById(R.id.txt_noofnew);


        }




    }




}
