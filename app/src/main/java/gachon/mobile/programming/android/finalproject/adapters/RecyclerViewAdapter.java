package gachon.mobile.programming.android.finalproject.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.activities.DetailActivity;
import gachon.mobile.programming.android.finalproject.models.RecyclerViewData;
import gachon.mobile.programming.android.finalproject.models.SingleData;
import gachon.mobile.programming.android.finalproject.utils.ExceptionHelper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.DisplayCustomToast;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MEDIA_TYPE_JSON;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.OKKY;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.ON_OFF_MIX;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.STACK_OVERFLOW;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.STACK_OVERFLOW_MAIN;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.getBitmapFromVectorDrawable;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<RecyclerViewData> mRecyclerViewDataArrayList;

    public RecyclerViewAdapter(final Context context, final ArrayList<RecyclerViewData> recyclerViewDataArrayList) {
        this.mContext = context;
        this.mRecyclerViewDataArrayList = recyclerViewDataArrayList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;
        private final ImageView mImageViewPhotoContent;
        private final ImageView mImageViewMore;
        private final ImageView mImageViewTags;
        private final TextView mTitle;
        private final TextView mContent;
        private final TextView mWatchCount;
        private final TextView mFavoritesCount;
        private final TextView mSubInfo;
        private final TextView mTags;
        private final CardView mCardView;

        ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.recycler_image_view);
            mImageViewPhotoContent = (ImageView) view.findViewById(R.id.recycler_image_view_photo_content);
            mTitle = (TextView) view.findViewById(R.id.recycler_text_view_title);
            mContent = (TextView) view.findViewById(R.id.recycler_text_view_content);
            mWatchCount = (TextView) view.findViewById(R.id.recycler_text_view_watch_count);
            mFavoritesCount = (TextView) view.findViewById(R.id.recycler_text_view_favorites_count);
            mSubInfo = (TextView) view.findViewById(R.id.recycler_text_view_sub_info);
            mTags = (TextView) view.findViewById(R.id.recycler_text_view_tags);
            mCardView = (CardView) view.findViewById(R.id.recycler_card_view);
            mImageViewMore = (ImageView) view.findViewById(R.id.recycler_image_view_more);
            mImageViewTags = (ImageView) view.findViewById(R.id.recycler_image_view_tags);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {
        final RecyclerViewData recyclerViewData = mRecyclerViewDataArrayList.get(position);

        setData(holder, recyclerViewData);

        holder.mCardView.setOnClickListener(v -> {
            Intent detailIntent = new Intent(mContext, DetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            detailIntent.putExtra("selectedData", recyclerViewData);
            mContext.startActivity(detailIntent);
        });

        holder.mImageViewMore.setOnClickListener(v -> {
            final PopupMenu popupMenu = new PopupMenu(mContext, v);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_content_favorites:
                        storeFavoritesContent(recyclerViewData, position);
                        break;
                    case R.id.action_copy:
                        copyToClipboard(recyclerViewData);
                        break;
                    case R.id.action_share:
                        shareToOther(recyclerViewData);
                        break;
                }
                return true;
            });
            popupMenu.inflate(R.menu.menu_sub);
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewDataArrayList.size();
    }

    private void storeFavoritesContent(final RecyclerViewData recyclerViewData, final int position) {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final String email = sharedPreferences.getString("email", null);
        final String password = sharedPreferences.getString("password", null);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("content_title", recyclerViewData.getTitle());
            jsonObject.put("content_url", recyclerViewData.getContentUrl());
            jsonObject.put("content_tag", recyclerViewData.getTags());
            jsonObject.put("content_summary", recyclerViewData.getContent());
            jsonObject.put("content_watch_count", recyclerViewData.getWatchCount());
            jsonObject.put("content_favorites_count", recyclerViewData.getFavoritesCount());
        } catch (final JSONException e) {
            DisplayCustomToast(mContext, ExceptionHelper.getApplicationExceptionMessage(e));
            return;
        }

        final Observable<SingleData> storeCategoryRx = RETROFIT_INTERFACE.StoreContentRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
        storeCategoryRx.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            String result = t.getData();
                            DisplayCustomToast(mContext, result);
                            if (result.contains("등록")) {
                                favoritesContentAdd(recyclerViewData, position);
                            } else {
                                favoritesContentRemove(mRecyclerViewDataArrayList.indexOf(recyclerViewData));
                            }

                        },
                        e -> DisplayCustomToast(mContext, ExceptionHelper.getApplicationExceptionMessage((Exception) e)))
        ;
    }

    private void copyToClipboard(final RecyclerViewData recyclerViewData) {
        final ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("text", recyclerViewData.getContentUrl()));
        DisplayCustomToast(mContext, mContext.getString(R.string.complete_to_copy));
    }

    private void shareToOther(final RecyclerViewData recyclerViewData) {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
        final String userName = sharedPreferences.getString("name", null);
        final Intent intentForShare = new Intent(Intent.ACTION_SEND);

        intentForShare.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.from_devLooks) + userName + mContext.getString(R.string.share_data) + "\n");
        intentForShare.putExtra(Intent.EXTRA_TEXT, "제목 : " + recyclerViewData.getTitle() + "\n\n" + recyclerViewData.getContentUrl());
        intentForShare.setType("text/plain");

        mContext.startActivity(Intent.createChooser(intentForShare, "공유").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setData(final RecyclerViewAdapter.ViewHolder holder, final RecyclerViewData recyclerViewData) {
        holder.mImageViewTags.setVisibility(View.GONE);
        holder.mTags.setVisibility(View.GONE);
        holder.mContent.setVisibility(View.GONE);
        holder.mImageViewPhotoContent.setVisibility(View.GONE);

        final String categoryType = recyclerViewData.getType();
        if (categoryType.equals(STACK_OVERFLOW)) {
            if (recyclerViewData.getImageUrl() != null) {
                Glide
                        .with(mContext)
                        .load(recyclerViewData.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.ic_launcher)
                                .circleCrop())
                        .into(holder.mImageView);
            }
        } else if (categoryType.equals(STACK_OVERFLOW_MAIN)) {
            if (recyclerViewData.getImageResources() != null) {
                holder.mImageView.setImageBitmap(recyclerViewData.getImageResources());
            }
        } else if (categoryType.equals(OKKY)) {
            if (recyclerViewData.getImageUrl() != null) {
                Glide
                        .with(mContext)
                        .load(recyclerViewData.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.ic_launcher)
                                .circleCrop())
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
        } else {
            holder.mImageView.setImageBitmap(recyclerViewData.getImageResources());
        }

        if (recyclerViewData.getTags() != null) {
            holder.mImageViewTags.setVisibility(View.VISIBLE);
            holder.mTags.setVisibility(View.VISIBLE);
            holder.mTags.setText(recyclerViewData.getTags());
        }

        holder.mTitle.setText(recyclerViewData.getTitle());
        if (recyclerViewData.getContent() != null) {
            holder.mContent.setVisibility(View.VISIBLE);
            holder.mContent.setText(recyclerViewData.getContent());
        }

        holder.mWatchCount.setText(recyclerViewData.getWatchCount());
        holder.mFavoritesCount.setText(recyclerViewData.getFavoritesCount());
        holder.mSubInfo.setText(recyclerViewData.getSubInfo());
    }

    private void favoritesContentRemove(final int position) {
        mRecyclerViewDataArrayList.remove(position);
        notifyItemRemoved(position);
    }

    private void favoritesContentAdd(final RecyclerViewData recyclerViewData, final int position) {
        recyclerViewData.setType("main");
        recyclerViewData.setImageResources(getBitmapFromVectorDrawable(mContext, R.drawable.ic_favorites_fill_24dp));
        recyclerViewData.setImageUrl(null);
        mRecyclerViewDataArrayList.add(position, recyclerViewData);
        notifyItemInserted(position);
    }

    public ArrayList<RecyclerViewData> add(final ArrayList<RecyclerViewData> additionalData, final int position) {
        for (RecyclerViewData data : additionalData) {
            mRecyclerViewDataArrayList.add(position, data);
        }
        notifyDataSetChanged();
        return mRecyclerViewDataArrayList;
    }
}