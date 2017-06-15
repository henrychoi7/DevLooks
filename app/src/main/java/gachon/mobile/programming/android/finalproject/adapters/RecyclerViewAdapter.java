package gachon.mobile.programming.android.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.activities.DetailActivity;
import gachon.mobile.programming.android.finalproject.activities.SubActivity;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.OKKY;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.ON_OFF_MIX;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.STACK_OVERFLOW;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<RecyclerViewData> mRecyclerViewDataArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<RecyclerViewData> recyclerViewDataArrayList) {
        this.mContext = context;
        this.mRecyclerViewDataArrayList = recyclerViewDataArrayList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;
        private final ImageView mImageViewPhotoContent;
        private final TextView mTitle;
        private final TextView mContent;
        private final CardView mCardView;

        ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.recycler_image_view);
            mImageViewPhotoContent = (ImageView) view.findViewById(R.id.recycler_image_view_photo_content);
            mTitle = (TextView) view.findViewById(R.id.recycler_text_view_title);
            mContent = (TextView) view.findViewById(R.id.recycler_text_view_content);
            mCardView = (CardView) view.findViewById(R.id.recycler_card_view);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        final RecyclerViewData recyclerViewData = mRecyclerViewDataArrayList.get(position);
        String categoryType = recyclerViewData.getType();
        if (categoryType.equals(STACK_OVERFLOW)) {
            if (recyclerViewData.getImageUrl() != null) {
                Glide
                        .with(mContext)
                        .load(recyclerViewData.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.ic_launcher)
                                .centerCrop())
                        .into(holder.mImageView);
            }
        } else if (categoryType.equals(OKKY)) {
            if (recyclerViewData.getImageUrl() != null) {
                Glide
                        .with(mContext)
                        .load(recyclerViewData.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.ic_launcher)
                                .centerCrop())
                        .into(holder.mImageView);
            }
        } else if (categoryType.equals(ON_OFF_MIX)) {
            if (recyclerViewData.getImageResources() != null) {
                holder.mImageView.setImageBitmap(recyclerViewData.getImageResources());
            }

            if (recyclerViewData.getImageUrl() != null) {
                holder.mImageViewPhotoContent.setVisibility(View.VISIBLE);
                Glide
                        .with(mContext)
                        .load(recyclerViewData.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.ic_launcher)
                                .fitCenter())
                        .into(holder.mImageViewPhotoContent);
            }
        }

        holder.mTitle.setText(recyclerViewData.getTitle());
        if (recyclerViewData.getContent() != null) {
            holder.mContent.setVisibility(View.VISIBLE);
            holder.mContent.setText(recyclerViewData.getContent());
        }

        holder.mCardView.setOnClickListener(v -> {
            Intent detailIntent = new Intent(mContext, DetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            detailIntent.putExtra("selectedUrl", recyclerViewData.getContentUrl());
            detailIntent.putExtra("selectedType", recyclerViewData.getType());
            mContext.startActivity(detailIntent);
        });
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewDataArrayList.size();
    }
}
