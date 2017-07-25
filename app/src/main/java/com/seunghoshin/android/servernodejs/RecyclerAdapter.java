package com.seunghoshin.android.servernodejs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SeungHoShin on 2017. 7. 25..
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    LayoutInflater inflater;
    List<Bbs> data;

    public RecyclerAdapter(Context context, List<Bbs> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Bbs bbs = data.get(position);
        holder.setTitle(bbs.title);
        holder.setDate(bbs.date);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDate;

        public Holder(View v) {
            super(v);
            textTitle = (TextView) v.findViewById(R.id.textTitle);
            textDate = (TextView) v.findViewById(R.id.textDate);
        }


        public void setTitle(String title) {
            textTitle.setText(title);
        }

        public void setDate(String date) {
            textDate.setText(date);
        }
    }

}
