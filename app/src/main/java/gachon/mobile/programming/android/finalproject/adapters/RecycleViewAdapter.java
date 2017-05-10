package gachon.mobile.programming.android.finalproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.RecycleViewData;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-11.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private ArrayList<RecycleViewData> mRecycleViewDataArrayList;

    public RecycleViewAdapter(ArrayList<RecycleViewData> recycleViewDataArrayList) {
        this.mRecycleViewDataArrayList = recycleViewDataArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView mImageView;
        private TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.image);
            mTextView = (TextView)view.findViewById(R.id.textview);
        }
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, int position) {
        holder.mImageView.setImageResource(mRecycleViewDataArrayList.get(position).img);
        holder.mTextView.setText(mRecycleViewDataArrayList.get(position).text);
    }

    @Override
    public int getItemCount() {
        return mRecycleViewDataArrayList.size();
    }
}
