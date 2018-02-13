package com.quagnitia.studentattendance.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.admin.AddClassActivity;
import com.quagnitia.studentattendance.admin.ViewAllClassesActivity;
import com.quagnitia.studentattendance.models.GetAllClass;

import java.util.List;

/**
 * Created by Admin on 10-02-2018.
 */

public class ViewAllClassAdapter extends RecyclerView.Adapter<ViewAllClassAdapter.MyViewHolder> {

    private List<GetAllClass> classList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_classname;
        private ImageView img_edit,img_delete;

        public MyViewHolder(View view) {
            super(view);
            tv_classname = (TextView) view.findViewById(R.id.tv_class_code);
            img_edit=(ImageView)view.findViewById(R.id.img_edit);
            img_delete=(ImageView)view.findViewById(R.id.img_delete);
        }
    }


    public ViewAllClassAdapter(Context mContext,List<GetAllClass> classList) {
        this.mContext=mContext;
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.lay_class_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        GetAllClass getAllClass = classList.get(position);
        holder.tv_classname.setText(getAllClass.getClassabbre()+"\n"+getAllClass.getClassname());
        holder.img_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, AddClassActivity.class);
                i.putExtra("ClassName",classList.get(position).getClassname());
                i.putExtra("ClassAbbrevation",classList.get(position).getClassabbre());
                mContext.startActivity(i);
                ((ViewAllClassesActivity)mContext).finish();
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((ViewAllClassesActivity)mContext).callDeleteClassWS(position);
            }
        });
    }







    public void showAlert() {

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                LayoutInflater lf = (LayoutInflater) (mContext)
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogview = lf.inflate(R.layout.retry_dialog, null);
                TextView title = (TextView) dialogview.findViewById(R.id.title);
                title.setText("Note");
                TextView body = (TextView) dialogview
                        .findViewById(R.id.dialogBody);
                body.setText("Do you want to delete the record");
                dialog.setContentView(dialogview);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);
                dialog.show();
                TextView cancel = (TextView) dialogview
                        .findViewById(R.id.dialogCancel);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView retry = (TextView) dialogview
                        .findViewById(R.id.dialogRetry);
                retry.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });




    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}
