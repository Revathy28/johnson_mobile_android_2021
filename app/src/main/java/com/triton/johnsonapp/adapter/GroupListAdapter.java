package com.triton.johnsonapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.johnsonapp.Fetch_RM_Info_ListActivity;
import com.triton.johnsonapp.Forms.ImageBasedInputFormActivity;
import com.triton.johnsonapp.Forms.InputFormFiveActivity;
import com.triton.johnsonapp.Forms.InputValueFormListActivity;
import com.triton.johnsonapp.Forms.JointInspectorInputFormActivity;
import com.triton.johnsonapp.Forms.RowBasedInputFormActivity;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.activity.SubGroupListActivity;
import com.triton.johnsonapp.responsepojo.GroupDetailManagementResponse;

import java.util.List;


public class GroupListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "GroupListAdapter";
    private List<GroupDetailManagementResponse.DataBean> dataBeanList;
    private Context context;

    GroupDetailManagementResponse.DataBean currentItem;

    private int size;
    String activity_id;
    String job_id;
    String status;
    int form_type;
    String Ukey;
    String fromactivity;

    public GroupListAdapter(Context context, List<GroupDetailManagementResponse.DataBean> dataBeanList,
                            String activity_id, String job_id,String status,String fromactivity) {
        this.context = context;
        this.dataBeanList = dataBeanList;
        this.activity_id = activity_id;
        this.job_id = job_id;
        this.status = status;
        this.fromactivity = fromactivity;

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


        if(dataBeanList.get(position).getGroup_detail_name() != null && !dataBeanList.get(position).getGroup_detail_name().equals("")){
            holder.txt_serv_title.setText(dataBeanList.get(position).getGroup_detail_name());
        }else{
            holder.txt_serv_title.setText("");

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

       // Log.d("grrrrr", dataBeanList.get(position).get_id());

        holder.cv_root.setOnClickListener(v -> {

            if(dataBeanList.get(position).getSub_group_status()!=null&&dataBeanList.get(position).getSub_group_status().equals("true")){
                Intent intent = new Intent(context, SubGroupListActivity.class);
                intent.putExtra("activity_id",activity_id);
                intent.putExtra("job_id",job_id);
                intent.putExtra("group_id",dataBeanList.get(position).get_id());
                intent.putExtra("group_detail_name",dataBeanList.get(position).getGroup_detail_name());
                intent.putExtra("status",status);
                intent.putExtra("fromactivity",fromactivity);
                context.startActivity(intent);

               // Log.d("grrrrr", dataBeanList.get(position).get_id());
            }

            else {

                if(dataBeanList.get(position).getForm_type()!=null){

                    if(dataBeanList.get(position).getForm_type().equals("1")){
                        form_type= Integer.parseInt(dataBeanList.get(position).getForm_type());
                        Ukey=dataBeanList.get(position).getActivity_ukey();
                        Intent intent = new Intent(context, InputValueFormListActivity.class);
                        intent.putExtra("activity_id",dataBeanList.get(position).getActivity_id());
                        intent.putExtra("job_id",job_id);
                        intent.putExtra("UKEY",Ukey);
                        Log.e("UKEYnulll",dataBeanList.get(position).getActivity_ukey());
                        intent.putExtra("group_id",dataBeanList.get(position).get_id());
                        intent.putExtra("group_detail_name",dataBeanList.get(position).getGroup_detail_name());
                        intent.putExtra("subgroup_id","");
                        intent.putExtra("status",status);
                        intent.putExtra("form_type",form_type);
                        intent.putExtra("fromactivity",fromactivity);
                        context.startActivity(intent);

                        SharedPreferences sp =context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdi1 = sp.edit();
                        myEdi1.putInt("form_type", form_type);
                        Log.e("form_typeee", String.valueOf(form_type));
                        myEdi1.commit();


                        Log.d("gerrrrr", dataBeanList.get(position).get_id());
                    }
                    else if(dataBeanList.get(position).getForm_type().equals("2")){
                        Intent intent = new Intent(context, ImageBasedInputFormActivity.class);
                        intent.putExtra("activity_id",dataBeanList.get(position).getActivity_id());
                        intent.putExtra("job_id",job_id);
                        intent.putExtra("group_id",dataBeanList.get(position).get_id());
                        intent.putExtra("group_detail_name",dataBeanList.get(position).getGroup_detail_name());
                        intent.putExtra("subgroup_id","");
                        intent.putExtra("status",status);
                        intent.putExtra("fromactivity",fromactivity);
                        context.startActivity(intent);

                        Log.d("act_gr", dataBeanList.get(position).get_id());
                    }
                    else if(dataBeanList.get(position).getForm_type().equals("3")){
                        Intent intent = new Intent(context, RowBasedInputFormActivity.class);
                        intent.putExtra("activity_id",dataBeanList.get(position).getActivity_id());
                        intent.putExtra("job_id",job_id);
                        intent.putExtra("group_id",dataBeanList.get(position).get_id());
                        intent.putExtra("group_detail_name",dataBeanList.get(position).getGroup_detail_name());
                        intent.putExtra("subgroup_id","");
                        intent.putExtra("status",status);
                        intent.putExtra("fromactivity",fromactivity);
                        context.startActivity(intent);

                        Log.d("act_gr1", dataBeanList.get(position).get_id());
                    }
                    else if(dataBeanList.get(position).getForm_type().equals("4")){



                            Intent intent=new Intent(context,SubGroupListActivity.class);
                            Ukey=dataBeanList.get(position).getActivity_ukey();
                            intent.putExtra("UKEY",Ukey);
                            Log.e("UKEYnulll",dataBeanList.get(position).getActivity_ukey());
                            intent.putExtra("activity_id",activity_id);
                            intent.putExtra("job_id",job_id);
                            intent.putExtra("group_id",dataBeanList.get(position).get_id());
                            intent.putExtra("group_detail_name",dataBeanList.get(position).getGroup_detail_name());
                            intent.putExtra("status",status);
                            intent.putExtra("fromactivity",fromactivity);
                            context.startActivity(intent);

                        Log.d("act_gr2", dataBeanList.get(position).get_id());

                      /*
                            form_type= Integer.parseInt(dataBeanList.get(position).getForm_type());
                            Intent intent = new Intent(context, InputValueFormListActivity.class);
                            intent.putExtra("activity_id",dataBeanList.get(position).getActivity_id());
                            intent.putExtra("job_id",job_id);
                            intent.putExtra("UKEY",dataBeanList.get(position).getActivity_ukey());
                            Log.e("UKEY",dataBeanList.get(position).getActivity_ukey());
                            intent.putExtra("group_id",dataBeanList.get(position).get_id());
                            intent.putExtra("group_detail_name",dataBeanList.get(position).getGroup_detail_name());
                            intent.putExtra("subgroup_id","");
                            intent.putExtra("status",status);
                            intent.putExtra("form_type",form_type);
                            fromactivity="SubGroupListActivity";
                            intent.putExtra("fromactivity",fromactivity);
                            Log.w(TAG,"fromactivityy"+fromactivity);
                            context.startActivity(intent);
                        }*/


                       /* Intent intent = new Intent(context, InputValueFormListActivity.class);
                        intent.putExtra("_id",dataBeanList.get(position).get_id());
                        intent.putExtra("activity_id",dataBeanList.get(position).getActivity_id());
                        intent.putExtra("job_detail_id",dataBeanList.get(position).getJob_detail_id());
                        intent.putExtra("group_id",dataBeanList.get(position).get_id());
                        intent.putExtra("group_detail_name",dataBeanList.get(position).getGroup_detail_name());
                        intent.putExtra("job_id",job_id);
                        intent.putExtra("subgroup_id","");
                        intent.putExtra("status",status);
                        intent.putExtra("fromactivity",fromactivity);
                        context.startActivity(intent);*/
                    }
                    else if(dataBeanList.get(position).getForm_type().equals("5")){
                        Intent intent = new Intent(context, Fetch_RM_Info_ListActivity.class);
                        intent.putExtra("_id",dataBeanList.get(position).get_id());
                        intent.putExtra("activity_id",dataBeanList.get(position).getActivity_id());
                        intent.putExtra("job_detail_id",dataBeanList.get(position).getJob_detail_id());
                        intent.putExtra("group_id",dataBeanList.get(position).get_id());
                        intent.putExtra("group_detail_name",dataBeanList.get(position).getGroup_detail_name());
                        intent.putExtra("job_id",job_id);
                        intent.putExtra("status",status);
                        intent.putExtra("fromactivity",fromactivity);
                        context.startActivity(intent);
                        Log.d("act_gr3", dataBeanList.get(position).get_id());
                    }
                }

            }


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
