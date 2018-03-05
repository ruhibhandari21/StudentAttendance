package com.quagnitia.studentattendance.adapters;

/**
 * Created by admin on 2/16/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.models.AllFilesModel;
import com.quagnitia.studentattendance.teacher.ViewAllUploads;
import com.quagnitia.studentattendance.utils.PreferencesManager;

import java.util.List;


/**
 * Created by Admin on 10-02-2018.
 */

public class ViewAllFilesAdapter extends RecyclerView.Adapter<ViewAllFilesAdapter.MyViewHolder> {

    private List<AllFilesModel> classList;
    private Context mContext;
    private PreferencesManager preferencesManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_imagename;
        private ImageView img_delete;

        public MyViewHolder(View view) {
            super(view);
            tv_imagename = (TextView) view.findViewById(R.id.tv_imagename);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            preferencesManager=PreferencesManager.getInstance(mContext);
        }
    }


    public ViewAllFilesAdapter(Context mContext, List<AllFilesModel> classList) {
        this.mContext = mContext;
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.lay_allfiles, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AllFilesModel getAllClass = classList.get(position);
        holder.tv_imagename.setText(getAllClass.getImagename());


        if(preferencesManager.getRole().equals("3"))
        {
            holder.img_delete.setVisibility(View.INVISIBLE);
            holder.img_delete.setEnabled(false);
        }
        else
        {
            holder.img_delete.setVisibility(View.VISIBLE);
            holder.img_delete.setEnabled(true);
        }



        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewAllUploads) mContext).callDeleteClassWS(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewAllUploads) mContext).callViewFileWS(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return classList.size();
    }
}

