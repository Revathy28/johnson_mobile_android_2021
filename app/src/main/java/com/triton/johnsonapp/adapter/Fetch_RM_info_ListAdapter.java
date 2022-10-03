package com.triton.johnsonapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.johnsonapp.Forms.InputFormFiveActivity;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activitybased.ABCustomerDetailsActivity;
import com.triton.johnsonapp.responsepojo.Fetch_rm_info_listResponse;

import java.util.List;


public class Fetch_RM_info_ListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "ABJobDetailListAdapter";
    private List<Fetch_rm_info_listResponse.DataBean> dataBeanList;
    private Context context;

    Fetch_rm_info_listResponse.DataBean currentItem;

    private int size;
    String status;
    String fromactivity,_id,job_id,job_detail_id,job_detail_no,group_id,group_detail_name,activity_id,UKEY,UKEY_DESC;

    public Fetch_RM_info_ListAdapter(Context context, List<Fetch_rm_info_listResponse.DataBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
        this.status = status ;
    }

    public void filterList(List<Fetch_rm_info_listResponse.DataBean> filterllist)
    {
        dataBeanList = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_staticdatalist, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = dataBeanList.get(position);

        if(dataBeanList.get(position).getST_MDH_SEQNO() != null && !dataBeanList.get(position).getST_MDH_SEQNO().equals("")){
            holder.txt_serv_title.setText(dataBeanList.get(position).getST_MDH_DCNO());
        }else{
            holder.txt_serv_title.setText("");

        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        _id = sharedPreferences.getString("_id", "default value");
        activity_id = sharedPreferences.getString("activity_id", "default value");
        job_detail_id = sharedPreferences.getString("job_detail_id", "default value");
        group_id = sharedPreferences.getString("group_id", "default value");
        group_detail_name = sharedPreferences.getString("group_detail_name", "default value");
        job_id = sharedPreferences.getString("job_id", "default value");
        status = sharedPreferences.getString("status", "default value");
        fromactivity = sharedPreferences.getString("fromactivity", "default value");
        UKEY = sharedPreferences.getString("UKEY", "default value");
        job_detail_no = sharedPreferences.getString("job_detail_no", "default value");
        UKEY_DESC = sharedPreferences.getString("UKEY_DESC", "default value");


        holder.cv_root.setOnClickListener(v -> {
            //Intent intent = new Intent(context, GroupListActivity.class);

            Intent intent = new Intent(context, InputFormFiveActivity.class);
                intent.putExtra("activity_id", activity_id);
                intent.putExtra("group_id", group_id);
                intent.putExtra("job_id", job_id);
                intent.putExtra("status", status);
                intent.putExtra("fromactivity", fromactivity);
                intent.putExtra("job_detail_id", job_detail_id);
                intent.putExtra("job_detail_no", job_detail_no);
                intent.putExtra("UKEY", UKEY);
                intent.putExtra("UKEY_DESC", UKEY_DESC);
                intent.putExtra("ST_MDH_SEQNO", dataBeanList.get(position).getST_MDH_SEQNO());
                context.startActivity(intent);
//
//            SharedPreferences sp1 =context.getSharedPreferences("myKey", MODE_PRIVATE);
//            SharedPreferences.Editor ed = sp1.edit();
//            ed.putInt("formtypeval", form_type);
//            Log.e("formtypeval", String.valueOf(form_type));
//            ed.commit();


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
        public TextView txt_serv_title,txt_serv_updatedat,txt_serv_createdat;
        ImageView img_next;
        CardView cv_root;

        public ViewHolderOne(View itemView) {
            super(itemView);
            txt_serv_title = itemView.findViewById(R.id.txt_serv_title);
            txt_serv_updatedat = itemView.findViewById(R.id.txt_serv_updatedat);
            txt_serv_createdat = itemView.findViewById(R.id.txt_serv_createdat);
            img_next = itemView.findViewById(R.id.img_next);
            cv_root = itemView.findViewById(R.id.cv_root);

        }

    }
}
