package gachon.mobile.programming.android.finalproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsengvn.typekit.Typekit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.activities.SubActivity;
import gachon.mobile.programming.android.finalproject.enums.CategoryMenuEnum;
import gachon.mobile.programming.android.finalproject.enums.ExpandableMenuEnum;
import gachon.mobile.programming.android.finalproject.models.NavigationMenuData;
import gachon.mobile.programming.android.finalproject.models.SingleData;
import gachon.mobile.programming.android.finalproject.utils.ExceptionHelper;
import gachon.mobile.programming.android.finalproject.views.MainActivityView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.CUSTOM_FONT;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.MEDIA_TYPE_JSON;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.PREF_ID;
import static gachon.mobile.programming.android.finalproject.utils.ApplicationClass.RETROFIT_INTERFACE;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public class ExpandableMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NavigationMenuData> mNavigationMenuDataArrayList;
    private final Context mContext;
    private final MainActivityView mMainActivityView;
    private final int ENUM_GROUP = ExpandableMenuEnum.GROUP.getTypeValue();
    //private final int ENUM_CHILD = ExpandableMenuEnum.CHILD.getTypeValue();

    public ExpandableMenuAdapter(final Context context, final MainActivityView mainActivityView, final ArrayList<NavigationMenuData> navigationMenuDataArrayList) {
        this.mContext = context;
        this.mMainActivityView = mainActivityView;
        this.mNavigationMenuDataArrayList = navigationMenuDataArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View convertView = inflater.inflate(R.layout.nav_expandable_menu, parent, false);
        return new ListNavigationViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final NavigationMenuData navigationMenuData = mNavigationMenuDataArrayList.get(position);
        if (navigationMenuData.getType() == ENUM_GROUP) {
            final ListNavigationViewHolder navigationViewHolder = (ListNavigationViewHolder) holder;
            navigationViewHolder.navigationTextView.setText(navigationMenuData.getTitle());
            navigationViewHolder.navigationTextView.setTypeface(Typekit.createFromAsset(mContext, CUSTOM_FONT));
            if (navigationMenuData.getImageResource() != null) {
                navigationViewHolder.navigationImageView.setImageResource(navigationMenuData.getImageResource());
            }

            setCategoryFavorites(navigationMenuData.isFavorite(), navigationViewHolder);

            navigationViewHolder.navigationCategoryFavoritesImageView.setOnClickListener(v -> {
                final boolean isSelected = (boolean) v.getTag();
                final String favoritesCode = CategoryMenuEnum.findValueByName(navigationViewHolder.navigationTextView.getText().toString(), mContext);

                final SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_ID, Activity.MODE_PRIVATE);
                final String email = sharedPreferences.getString("email", null);
                final String password = sharedPreferences.getString("password", null);
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);
                    jsonObject.put("favorites_code", favoritesCode);
                    jsonObject.put("is_selected", !isSelected);
                } catch (final JSONException e) {
                    mMainActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage(e));
                    return;
                }

                final Observable<SingleData> storeCategoryRx = RETROFIT_INTERFACE.StoreCategoryRx(RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()));
                storeCategoryRx.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(t -> {
                                    if (t.isSuccess()) {
                                        setCategoryFavorites(!isSelected, navigationViewHolder);
                                    } else {
                                        mMainActivityView.showCustomToast(t.getData());
                                    }
                                },
                                e -> mMainActivityView.showCustomToast(ExceptionHelper.getApplicationExceptionMessage((Exception) e)));
            });

            navigationViewHolder.navigationTextView.setOnClickListener(v -> {
                if (navigationMenuData.getInvisibleChildren() == null) {
                    navigationMenuData.setInvisibleChildren(new ArrayList<>());
                    final int selectedPosition = mNavigationMenuDataArrayList.indexOf(navigationMenuData);
                    while (mNavigationMenuDataArrayList.size() > selectedPosition + 1 && mNavigationMenuDataArrayList.get(selectedPosition + 1).getType() != ENUM_GROUP) {
                        removeChild(navigationMenuData, selectedPosition + 1);
                    }
                    navigationViewHolder.navigationImageView.setImageResource(R.drawable.ic_contracted_24dp);
                } else {
                    final int selectedPosition = mNavigationMenuDataArrayList.indexOf(navigationMenuData);
                    int index = selectedPosition + 1;
                    for (NavigationMenuData data : navigationMenuData.getInvisibleChildren()) {
                        addChild(data, index);
                        index++;
                    }
                    navigationViewHolder.navigationImageView.setImageResource(R.drawable.ic_expanded_24dp);
                    navigationMenuData.setInvisibleChildren(null);
                }
            });
        } else {
            final ListNavigationViewHolder navigationViewHolder = (ListNavigationViewHolder) holder;
            final String childMenuTitle = navigationMenuData.getTitle();
            navigationViewHolder.navigationTextView.setText(childMenuTitle);
            navigationViewHolder.navigationTextView.setTypeface(Typekit.createFromAsset(mContext, CUSTOM_FONT));
            if (navigationMenuData.getImageResource() != null) {
                navigationViewHolder.navigationImageView.setImageResource(navigationMenuData.getImageResource());
                final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(50, 10, 30, 10);
                navigationViewHolder.navigationImageView.setLayoutParams(layoutParams);
            }

            navigationViewHolder.navigationTextView.setOnClickListener(v -> {
                final Intent subIntent = new Intent(mContext, SubActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                subIntent.putExtra("child_title", childMenuTitle);
                subIntent.putExtra("group_value", navigationMenuData.getType());
                mContext.startActivity(subIntent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mNavigationMenuDataArrayList.size();
    }

    private static class ListNavigationViewHolder extends RecyclerView.ViewHolder {
        final TextView navigationTextView;
        final ImageView navigationImageView;
        final ImageView navigationCategoryFavoritesImageView;

        private ListNavigationViewHolder(View convertView) {
            super(convertView);
            navigationTextView = (TextView) convertView.findViewById(R.id.nav_text_view);
            navigationImageView = (ImageView) convertView.findViewById(R.id.nav_image_view);
            navigationCategoryFavoritesImageView = (ImageView) convertView.findViewById(R.id.nav_category_favorites_image_view);
        }

    }

    private void addChild(final NavigationMenuData navigationMenuData, final int position) {
        mNavigationMenuDataArrayList.add(position, navigationMenuData);
        notifyItemInserted(position);
    }

    private void removeChild(final NavigationMenuData navigationMenuData, final int position) {
        navigationMenuData.getInvisibleChildren().add(mNavigationMenuDataArrayList.remove(position));
        notifyItemRemoved(position);
    }

    private void setCategoryFavorites(final boolean isSelected, final ListNavigationViewHolder navigationViewHolder) {
        if (isSelected) {
            navigationViewHolder.navigationCategoryFavoritesImageView.setTag(true);
            navigationViewHolder.navigationCategoryFavoritesImageView.setImageResource(R.drawable.ic_favorites_fill_24dp);
        } else {
            navigationViewHolder.navigationCategoryFavoritesImageView.setTag(false);
            navigationViewHolder.navigationCategoryFavoritesImageView.setImageResource(R.drawable.ic_favorites_empty_24dp);
        }
    }
}