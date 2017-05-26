package gachon.mobile.programming.android.finalproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<RecyclerViewData> mRecyclerViewDataArrayList;

    public RecyclerViewAdapter(Context context , ArrayList<RecyclerViewData> recyclerViewDataArrayList) {
        this.mContext = context;
        this.mRecyclerViewDataArrayList = recyclerViewDataArrayList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTitle;
        private TextView mContent;

        ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.main_image_view);
            mTitle = (TextView)view.findViewById(R.id.main_text_view_title);
            mContent = (TextView)view.findViewById(R.id.main_text_view_content);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        RecyclerViewData recyclerViewData  = mRecyclerViewDataArrayList.get(position);
        if (recyclerViewData.getImageResources() != null) {
            holder.mImageView.setImageBitmap(recyclerViewData.getImageResources());
        } else if (recyclerViewData.getImageUrl() != null) {
            Glide.with(mContext).load(recyclerViewData.getImageUrl()).into(holder.mImageView);
        }

        holder.mTitle.setText(recyclerViewData.getTitle());
        holder.mContent.setText(recyclerViewData.getContent());
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewDataArrayList.size();
    }
}
