package com.triton.johnsonapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.triton.johnsonapp.Forms.InputValueFormListActivity;
import com.triton.johnsonapp.R;
import com.triton.johnsonapp.interfaces.GetDateTimeListener;
import com.triton.johnsonapp.interfaces.GetDigitalSignUploadAddListener;
import com.triton.johnsonapp.interfaces.GetDigitalSignUploadClearListener;
import com.triton.johnsonapp.interfaces.GetDigitalSignUploadListener;
import com.triton.johnsonapp.interfaces.GetFileUploadListener;
import com.triton.johnsonapp.interfaces.GetNumberListener;
import com.triton.johnsonapp.interfaces.GetSpinnerListener;
import com.triton.johnsonapp.interfaces.GetStringListener;
import com.triton.johnsonapp.interfaces.GetTextAreaListener;
import com.triton.johnsonapp.interfaces.GetInputFieldListener;
import com.triton.johnsonapp.responsepojo.GetFieldListResponse;

import java.util.ArrayList;
import java.util.List;


public class FieldListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "FieldListAdapter";
    private List<GetFieldListResponse.DataBean> dataBeanList;
    private Context context;
    public String Ukey;
    public String label;
    GetFieldListResponse.DataBean currentItem;

    private int size;
    final int color = R.color.Light_grey;
    int ITEMS_PER_PAGE;

    int TOTAL_NUM_ITEMS;

    GetStringListener getStringListener;

    GetTextAreaListener getTextAreaListener;

    GetSpinnerListener getSpinnerListener;

    GetNumberListener getNumberListener;

    GetDateTimeListener getDateTimeListener;

    GetFileUploadListener getFileUploadListener;

    GetDigitalSignUploadListener getDigitalSignUploadListener;

    GetDigitalSignUploadAddListener getDigitalSignUploadAddListener;

    GetDigitalSignUploadClearListener getDigitalSignUploadClearListener;

    GetInputFieldListener getInputFieldListener;

    String digitalSignatureServerUrlImagePath;

    int currentPage;

    int check = 0;
    private List<?> arrayListdropdown;
    private String userrole;
    private String key;

    public FieldListAdapter(Context context, List<GetFieldListResponse.DataBean> dataBeanList, int ITEMS_PER_PAGE, int TOTAL_NUM_ITEMS, GetStringListener getStringListener, GetTextAreaListener getTextAreaListener, GetSpinnerListener getSpinnerListener, GetNumberListener getNumberListener, GetDateTimeListener getDateTimeListener,
                            GetFileUploadListener getFileUploadListener, GetDigitalSignUploadListener getDigitalSignUploadListener, GetDigitalSignUploadAddListener getDigitalSignUploadAddListener, GetDigitalSignUploadClearListener getDigitalSignUploadClearListener, GetInputFieldListener getInputFieldListener, int currentPage, String userrole, String key) {
        this.context = context;
        this.dataBeanList = dataBeanList;
        this.ITEMS_PER_PAGE = ITEMS_PER_PAGE;
        this.TOTAL_NUM_ITEMS = TOTAL_NUM_ITEMS;
        this.getStringListener = getStringListener;
        this.getTextAreaListener = getTextAreaListener;
        this.getSpinnerListener = getSpinnerListener;
       this.getNumberListener = getNumberListener ;
        this.getDateTimeListener = getDateTimeListener;
        this.getFileUploadListener = getFileUploadListener;
        this.getDigitalSignUploadListener = getDigitalSignUploadListener;
        this.getDigitalSignUploadAddListener = getDigitalSignUploadAddListener;
        this.getDigitalSignUploadClearListener = getDigitalSignUploadClearListener;
        this.currentPage = currentPage;
        this.getInputFieldListener = getInputFieldListener;
        this.userrole = userrole;
        this.key = key;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fieldlist, parent, false);
        return new ViewHolderOne(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        initLayoutOne((ViewHolderOne) holder, position);

    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = dataBeanList.get(position);
        Log.w(TAG, "CURRENTITEM-----" + currentItem.getField_length());

        int startItem = currentPage * ITEMS_PER_PAGE + position;
/*if(key.equalsIgnoreCase("OP-ACT8"))
{

}*/
//        Log.w(TAG,"currentItem startItem "+startItem);
//
//        Log.w(TAG,"currentItem POS "+position);
//        Log.w(TAG,"currentItem Field_name "+position+" "+currentItem.getField_name());
//
//        Log.w(TAG,"currentItem.getField_value() ---->"+currentItem.getField_value());


        if (currentItem.getField_name() != null && !currentItem.getField_name().equals("")) {
            holder.txt_field_title.setText(currentItem.getField_name());
        } else {
            holder.txt_field_title.setText("");

        }


        if (currentItem.getField_comments() != null && !currentItem.getField_comments().equals("")) {
            holder.txt_field_comments.setText(currentItem.getField_comments());
        } else {
            holder.txt_field_comments.setText("");

        }

        if (currentItem.getField_type() != null && !currentItem.getField_type().equals("")) {

            if (currentItem.getField_type().equals("String")) {

                holder.edt_string.setVisibility(View.VISIBLE);

                if (currentItem.getField_value() != null && !currentItem.getField_value().equals("")) {

                    holder.edt_string.setText(currentItem.getField_value());
                }

                holder.edt_string.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        getStringListener.getStringListener(holder.edt_textarea, s.toString(), startItem, currentItem.getField_length());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            } else if (currentItem.getField_type().equals("Textarea")) {

                holder.edt_textarea.setVisibility(View.VISIBLE);

                if (currentItem.getField_value() != null && !currentItem.getField_value().equals("")) {

                    holder.edt_textarea.setText(currentItem.getField_value());
                }

                holder.edt_textarea.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        getTextAreaListener.getTextAreaListener(holder.edt_textarea, s.toString(), startItem, currentItem.getField_length());

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            } else if (currentItem.getField_type().equals("Number")) {

                holder.edt_number.setVisibility(View.VISIBLE);

                if (currentItem.getField_value() != null && !currentItem.getField_value().equals("")) {

                    holder.edt_number.setText(currentItem.getField_value());
                } else {

                    holder.edt_number.setText("");
                }


                holder.edt_number.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        getNumberListener.getNumberListener(holder.edt_number, s.toString(), startItem, currentItem.getField_length());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            } else if (currentItem.getField_type().equals("Dropdown")) {

                //Log.w(TAG,"responsemessage : "+InputValueFormListActivity.responsemessage );

                if (InputValueFormListActivity.responsemessage != null && InputValueFormListActivity.responsemessage.equalsIgnoreCase("Joininspection")) {


                    if (key != null && key.equalsIgnoreCase("OP-ACT8")) {
                        arrayListdropdown = currentItem.getDrop_down();
                        Log.w(TAG,"currentItem.getDrop_down() : "+ arrayListdropdown);

                        holder.ll_dropdown.setVisibility(View.VISIBLE);
                        holder.cv_root.setVisibility(View.VISIBLE);
                        ArrayList<String> arrayList = new ArrayList<>();

                        arrayList.add("Select Value");

                        for (int i = 0; i < currentItem.getDrop_down().size(); i++) {
                            String string = currentItem.getDrop_down().get(i).toString();
                            Log.w(TAG, "spr string-->" + string);
                            arrayList.add(string);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        //Log.w(TAG,"currentItem.getDrop_down

                        if (currentItem.getField_value() != null && !currentItem.getField_value().isEmpty()) {
                            //if(currentItem.getField_comments()!=null && !currentItem.getField_comments().isEmpty()){
                            Log.w(TAG, "Dropdown if--->");

                            String compareValue = currentItem.getField_value();
                            holder.spr_dropdown.setAdapter(adapter);
                            if (compareValue != null) {
                                int spinnerPosition = adapter.getPosition(compareValue);
                                holder.spr_dropdown.setSelection(spinnerPosition);
                                Log.w(TAG, "Dropdown if--->"+ spinnerPosition);
                            }
                            holder.spr_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                    if (++check > 1) {

                                        //Log.w(TAG,"currentItem POS "+startItem);

                                        //Log.w(TAG,"currentItem POS "+parent.getItemAtPosition(pos));

                                        getSpinnerListener.getSpinnerListener(holder.spr_dropdown, startItem, parent.getItemAtPosition(pos).toString(), currentItem.getField_length());

                                    }
                                    String Valuee = holder.spr_dropdown.getSelectedItem().toString();
                                    Log.e("val", Valuee);
                                        if(Valuee.equals("OK"))
                                        {
                                           // holder.cv_root.setEnabled(false);
                                            holder.spr_dropdown.setEnabled(false);
                                            holder.cv_root.setCardBackgroundColor(Color.LTGRAY);
                                            holder.edit.setVisibility(View.VISIBLE);

                                            holder.edit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    holder.spr_dropdown.setEnabled(true);
                                                    holder.cv_root.setCardBackgroundColor(Color.WHITE);
                                                    holder.edit.setVisibility(View.GONE);

                                                    ArrayList<String> arrayList = new ArrayList<>();

                                                    arrayList.add("Select Value");

                                                    for (int i = 0; i < currentItem.getDrop_down().size(); i++) {
                                                        String string = currentItem.getDrop_down().get(i).toString();
                                                        Log.w(TAG, "spr string-->" + string);
                                                        arrayList.add(string);
                                                    }

                                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayList);
                                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    holder.spr_dropdown.setAdapter(adapter);
                                                }
                                            });
                                        }
                                    SharedPreferences sharedPref = context.getSharedPreferences("name", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("Valuee", Valuee);
                                    editor.commit();


                                    // Toast.makeText(context.getApplicationContext(),val, Toast.LENGTH_SHORT).show();

                                }

                                public void onNothingSelected(AdapterView<?> parent) {


                                }
                            });

                        } else {
                            Log.w(TAG, "Dropdown else--->");
                            holder.spr_dropdown.setAdapter(adapter);
                            holder.spr_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                    if (++check > 1) {

                                        //Log.w(TAG,"currentItem POS "+startItem);

                                        //Log.w(TAG,"currentItem POS "+parent.getItemAtPosition(pos));

                                        getSpinnerListener.getSpinnerListener(holder.spr_dropdown, startItem, parent.getItemAtPosition(pos).toString(), currentItem.getField_length());

                                    }
                                    String val = holder.spr_dropdown.getSelectedItem().toString();
                                    Log.e("val", val);


/*
                                        Toast.makeText(context.getApplicationContext(),val, Toast.LENGTH_SHORT).show();
*/
                                }

                                public void onNothingSelected(AdapterView<?> parent) {


                                }
                            });

                        }

                     /*
                        else{
                            holder.cv_root.setVisibility(View.GONE);

                        }*/

                    } else {
                        holder.cv_root.setVisibility(View.VISIBLE);
                        arrayListdropdown = currentItem.getDrop_down();
                        Log.w(TAG, "currentItem.getDrop_down() : " + new Gson().toJson(currentItem.getDrop_down()));

                        holder.ll_dropdown.setVisibility(View.VISIBLE);

                        ArrayList<String> arrayList = new ArrayList<>();

                        arrayList.add("Select Value");

                        for (int i = 0; i < currentItem.getDrop_down().size(); i++) {
                            String string = currentItem.getDrop_down().get(i).toString();
                            Log.w(TAG, "spr string-->" + string);
                            arrayList.add(string);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        Log.w(TAG, "currentItem.getDrop_down() Field_comments : " + currentItem.getField_comments() + " Field_value : " + currentItem.getField_value());

                        if (currentItem.getField_value() != null && !currentItem.getField_value().isEmpty()) {
                            //if(currentItem.getField_comments()!=null && !currentItem.getField_comments().isEmpty()){
                            Log.w(TAG, "Dropdown if--->");

                            String compareValue = currentItem.getField_value();
                            holder.spr_dropdown.setAdapter(adapter);
                            if (compareValue != null) {
                                int spinnerPosition = adapter.getPosition(compareValue);
                                holder.spr_dropdown.setSelection(spinnerPosition);

                            }
                            holder.spr_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                    if (++check > 1) {

                                        Log.w(TAG, "currentItem POS " + startItem);

                                        Log.w(TAG, "currentItem POS " + parent.getItemAtPosition(pos));

                                        getSpinnerListener.getSpinnerListener(holder.spr_dropdown, startItem, parent.getItemAtPosition(pos).toString(), currentItem.getField_length());

                                    }

                                    // Toast.makeText(context.getApplicationContext(),val, Toast.LENGTH_SHORT).show();

                                }

                                public void onNothingSelected(AdapterView<?> parent) {


                                }
                            });

                        } else {
                            //Log.w(TAG,"Dropdown else--->");
                            holder.spr_dropdown.setAdapter(adapter);
                            holder.spr_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                    if (++check > 1) {

                                        //Log.w(TAG,"currentItem POS "+startItem);

                                        //Log.w(TAG,"currentItem POS "+parent.getItemAtPosition(pos));

                                        getSpinnerListener.getSpinnerListener(holder.spr_dropdown, startItem, parent.getItemAtPosition(pos).toString(), currentItem.getField_length());

                                    }

                                    //Toast.makeText(context.getApplicationContext(),val, Toast.LENGTH_SHORT).show();
                                }

                                public void onNothingSelected(AdapterView<?> parent) {


                                }
                            });

                        }
                    }

                } else {
                    holder.cv_root.setVisibility(View.VISIBLE);
                    arrayListdropdown = currentItem.getDrop_down();
                    Log.w(TAG, "currentItem.getDrop_down() : " + new Gson().toJson(currentItem.getDrop_down()));

                    holder.ll_dropdown.setVisibility(View.VISIBLE);

                    ArrayList<String> arrayList = new ArrayList<>();

                    arrayList.add("Select Value");

                    for (int i = 0; i < currentItem.getDrop_down().size(); i++) {
                        String string = currentItem.getDrop_down().get(i).toString();
                        Log.w(TAG, "spr string-->" + string);
                        arrayList.add(string);

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    Log.w(TAG, "currentItem.getDrop_down() Field_comments : " + currentItem.getField_comments() + " Field_value : " + currentItem.getField_value());

                    if (currentItem.getField_value() != null && !currentItem.getField_value().isEmpty()) {
                        //if(currentItem.getField_comments()!=null && !currentItem.getField_comments().isEmpty()){
                        Log.w(TAG, "Dropdown if--->");

                        String compareValue = currentItem.getField_value();
                        holder.spr_dropdown.setAdapter(adapter);
                        if (compareValue != null) {
                            int spinnerPosition = adapter.getPosition(compareValue);
                            holder.spr_dropdown.setSelection(spinnerPosition);

                        }
                        holder.spr_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                if (++check > 1) {

                                    Log.w(TAG, "currentItem POS " + startItem);

                                    Log.w(TAG, "currentItem POS " + parent.getItemAtPosition(pos));

                                    getSpinnerListener.getSpinnerListener(holder.spr_dropdown, startItem, parent.getItemAtPosition(pos).toString(), currentItem.getField_length());

                                }

                                //Toast.makeText(context.getApplicationContext(),val, Toast.LENGTH_SHORT).show();


                            }

                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });

                    } else {
                        Log.w(TAG, "Dropdown else--->");
                        holder.spr_dropdown.setAdapter(adapter);
                        holder.spr_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                if (++check > 1) {

                                    Log.w(TAG, "currentItem POS " + startItem);

                                    Log.w(TAG, "currentItem POS " + parent.getItemAtPosition(pos));


                                    getSpinnerListener.getSpinnerListener(holder.spr_dropdown, startItem, parent.getItemAtPosition(pos).toString(), currentItem.getField_length());

                                }

                                // Toast.makeText(context.getApplicationContext(),label1, Toast.LENGTH_SHORT).show();
                            }

                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });

                    }
                }
            } else if (currentItem.getField_type().equals("Date&time")) {

                holder.edt_datetime.setVisibility(View.VISIBLE);

                if (currentItem.getField_value() != null && !currentItem.getField_value().equals("")) {
                    holder.edt_datetime.setText(currentItem.getField_value());


                }

                holder.edt_datetime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDateTimeListener.getDateTimeListener(holder.edt_datetime, startItem, currentItem.getField_length());
                    }
                });


            } else if (currentItem.getField_type().equals("File upload")) {

                holder.ll_file_upload.setVisibility(View.VISIBLE);

                Log.w(TAG, "currentItem.getField_value() ---->" + currentItem.getField_value());

                if (currentItem.getField_value() != null && !currentItem.getField_value().equals("")) {
                    if (currentItem.getField_value().equalsIgnoreCase("File upload")) {
                        holder.img_file_upload.setVisibility(View.GONE);
                        holder.cv_image.setVisibility(View.GONE);
                    }
                    String uploadimagepath = currentItem.getField_value();
                    if (uploadimagepath != null && !uploadimagepath.isEmpty()) {

                        holder.img_file_upload.setVisibility(View.VISIBLE);
                        holder.cv_image.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(uploadimagepath)
                                .into(holder.img_file_upload);

                    }


                } else {
                    holder.img_file_upload.setVisibility(View.GONE);
                    holder.cv_image.setVisibility(View.GONE);
                }


                holder.img_close.setVisibility(View.VISIBLE);

                holder.img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataBeanList.get(position).setField_value("");
                        holder.img_file_upload.setVisibility(View.GONE);
                        holder.cv_image.setVisibility(View.GONE);
                        notifyDataSetChanged();


                    }
                });

                holder.ll_file_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getFileUploadListener.getFileUploadListener(holder.ll_file_upload, startItem, holder.img_file_upload, holder.img_close, currentItem.getField_length(), holder.cv_image);


                    }
                });

            } else if (currentItem.getField_type().equals("Signature")) {

                holder.lldigitalsignature.setVisibility(View.VISIBLE);

              /*  holder.llheaderdigitalsignature.setVisibility(View.VISIBLE);

                holder.ivdigitalsignature.setVisibility(View.VISIBLE);*/

                if (currentItem.getField_value() != null && !currentItem.getField_value().equals("")) {

                    //holder.llheaderdigitalsignature.setVisibility(View.VISIBLE);

                    holder.ivdigitalsignature.setVisibility(View.VISIBLE);

                    digitalSignatureServerUrlImagePath = currentItem.getField_value();

                    Log.w(TAG, "digitalSignatureServerUrlImagePath---- " + digitalSignatureServerUrlImagePath);

                    holder.ivdigitalsignature.setVisibility(View.VISIBLE);
                    if (digitalSignatureServerUrlImagePath != null && !digitalSignatureServerUrlImagePath.isEmpty()) {
                        // dataBeanList.get(position).setField_value("");

                        Log.w(TAG, "digitalSignatureServerUrlImagePath--->" + digitalSignatureServerUrlImagePath);

                        Glide
                                .with(context)
                                .load(digitalSignatureServerUrlImagePath)
                                .apply(new RequestOptions().override(600, 200))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.ivdigitalsignature);


                    } else {
                        Glide.with(context)
                                .load(R.drawable.digital_signature)
                                .into(holder.ivdigitalsignature);

                    }


                }

                holder.lldigitalsignature.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.w(TAG, "currentItem POS DS" + startItem);

                        getDigitalSignUploadListener.getDigitalSignUploadListener(holder.llheaderdigitalsignature, holder.ivdigitalsignature, holder.mSignaturePad, holder.mSaveButton, holder.mClearButton, startItem, currentItem.getField_length());


                    }
                });


                holder.mSaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.w(TAG, "currentItem POS DS1" + startItem);


                        getDigitalSignUploadAddListener.getDigitalSignUploadAddListener(holder.llheaderdigitalsignature, holder.ivdigitalsignature, holder.mSignaturePad, holder.mSaveButton, holder.mClearButton, startItem, currentItem.getField_length());


                    }
                });

                holder.mClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.w(TAG, "currentItem POS DS" + startItem);

                        getDigitalSignUploadClearListener.getDigitalSignUploadClearListener(holder.llheaderdigitalsignature, holder.ivdigitalsignature, holder.mSignaturePad, holder.mSaveButton, holder.mClearButton, startItem, currentItem.getField_length());


                    }
                });
            } else if (currentItem.getField_type().equals("Lift")) {

                if (currentItem.getField_length() != null && !currentItem.getField_length().equals("")) {

                    //Log.w(TAG,"currentItem POS Lift"+startItem);
                    //Log.w(TAG,"currentItem POS Lift"+currentItem.getField_name()+" "+currentItem.get_id());
                    //String s = new String(currentItem.getField_length());
                    //int size = Integer.parseInt(s);
                    //Log.w(TAG,"size"+size);
                    holder.rv_liftinputlist.setVisibility(View.VISIBLE);


                    getInputFieldListener.getInputFieldListener(holder.rv_liftinputlist, startItem, currentItem.getField_length(), currentItem.getLift_list());
                }
            }
        }

        if (currentItem.getField_length() != null && !currentItem.getField_length().equals("")) {
            holder.txt_field_length.setText("* Field Length " + currentItem.getField_length());
        } else {
            holder.txt_field_length.setText("");

        }
    }

    @Override
    public int getItemCount() {
        return Math.min(dataBeanList.size(), ITEMS_PER_PAGE);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_field_title, txt_field_comments, txt_field_length;
        public EditText edt_string, edt_textarea, edt_number, edt_datetime;
        public LinearLayout ll_dropdown, ll_file_upload, lldigitalsignature, llheaderdigitalsignature;
        public Spinner spr_dropdown;
        public ImageView img_spinner, img_file_upload, img_close, ivdigitalsignature,edit;
        public Button mClearButton, mSaveButton;
        SignaturePad mSignaturePad;
        RecyclerView rv_liftinputlist;
        CardView cv_image, cv_root;

        String lable;

        public ViewHolderOne(View itemView) {
            super(itemView);
            txt_field_title = itemView.findViewById(R.id.txt_field_title);
            txt_field_comments = itemView.findViewById(R.id.txt_field_comments);
            edt_string = itemView.findViewById(R.id.edt_string);
            edt_textarea = itemView.findViewById(R.id.edt_textarea);
            edt_number = itemView.findViewById(R.id.edt_number);
            ll_dropdown = itemView.findViewById(R.id.ll_dropdown);
            spr_dropdown = itemView.findViewById(R.id.spr_dropdown);
            lable = String.valueOf(spr_dropdown);
            edit = itemView.findViewById(R.id.edit);
            img_spinner = itemView.findViewById(R.id.img_spinner);
            edt_datetime = itemView.findViewById(R.id.edt_datetime);
            ll_file_upload = itemView.findViewById(R.id.ll_file_upload);
            img_file_upload = itemView.findViewById(R.id.img_file_upload);
            img_close = itemView.findViewById(R.id.img_close);
            ivdigitalsignature = itemView.findViewById(R.id.ivdigitalsignature);
            mClearButton = itemView.findViewById(R.id.clear_button);
            mSaveButton = itemView.findViewById(R.id.save_button);
            lldigitalsignature = itemView.findViewById(R.id.lldigitalsignature);
            llheaderdigitalsignature = itemView.findViewById(R.id.llheaderdigitalsignature);
            mSignaturePad = itemView.findViewById(R.id.signaturePad);
            ivdigitalsignature = itemView.findViewById(R.id.ivdigitalsignature);
            txt_field_length = itemView.findViewById(R.id.txt_field_length);
            cv_image = itemView.findViewById(R.id.cv_image);
            rv_liftinputlist = itemView.findViewById(R.id.rv_liftinputlist);
            cv_root = itemView.findViewById(R.id.cv_root);

        }

    }

}





