package com.triton.johnson_tap_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;


public class JobFindListAdapter4 extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "PetBreedTypesListAdapter";
    private Context context;
    JobnoFindResponse.DataBean currentItem;
    private List<JobnoFindResponse.DataBean> breedTypedataBeanList;
    private PetBreedTypeSelectListener petBreedTypeSelectListener;

    public JobFindListAdapter4(Context context, List<JobnoFindResponse.DataBean> breedTypedataBeanList, PetBreedTypeSelectListener petBreedTypeSelectListener ) {
        this.context = context;
        this.breedTypedataBeanList = breedTypedataBeanList;
        this.petBreedTypeSelectListener = petBreedTypeSelectListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_breedtype_cardview, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint("LogNotTimber")
    private void initLayoutOne(ViewHolderOne holder, final int position) {
        currentItem = breedTypedataBeanList.get(position);

        Intent intent = new Intent("message_subject_intent4");
        intent.putExtra("cust4" , currentItem.getCUSNAME());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        if(currentItem.getJOBNO() != null){
            holder.txt_breedtype.setText(currentItem.getCUSNAME());

        }

    }
    @Override
    public int getItemCount() {
        return breedTypedataBeanList.size();
    }

    public void filterList(List<JobnoFindResponse.DataBean> breedTypedataBeanListFiltered) {
        breedTypedataBeanList = breedTypedataBeanListFiltered;
        Log.w(TAG,"breedTypedataBeanList : "+new Gson().toJson(breedTypedataBeanList));

        notifyDataSetChanged();
    }




    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_breedtype;
        public LinearLayout ll_root;

        public ViewHolderOne(View itemView) {
            super(itemView);

            txt_breedtype = itemView.findViewById(R.id.txt_breedtype);
            ll_root = itemView.findViewById(R.id.ll_root);

        }

    }
}
