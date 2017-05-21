package gachon.mobile.programming.android.finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gachon.mobile.programming.android.finalproject.R;
import gachon.mobile.programming.android.finalproject.models.ChildMenuData;
import gachon.mobile.programming.android.finalproject.models.GroupMenuData;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-21.
 */

public class ExpandableMenuAdapter extends BaseExpandableListAdapter {
    private ArrayList<GroupMenuData> mGroupMenuDataArrayList;

    public ExpandableMenuAdapter(ArrayList<GroupMenuData> groupMenuDataArrayList) {
        this.mGroupMenuDataArrayList = groupMenuDataArrayList;
    }

    @Override
    public int getGroupCount() {
        return mGroupMenuDataArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroupMenuDataArrayList.get(groupPosition).getChildMenuDataArrayList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupMenuDataArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroupMenuDataArrayList.get(groupPosition).getChildMenuDataArrayList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.nav_group_menu, null);
        }

        GroupMenuData groupMenuData = mGroupMenuDataArrayList.get(groupPosition);

        ImageView groupImageView = (ImageView) convertView.findViewById(R.id.group_image_view);
        TextView groupTextView = (TextView) convertView.findViewById(R.id.group_text_view);
        if (groupMenuData.getGroupResourceIcon() != null) {
            groupImageView.setImageResource(groupMenuData.getGroupResourceIcon());
        }
        groupTextView.setText(groupMenuData.getGroupTitle());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.nav_child_menu, null);
        }

        GroupMenuData groupMenuData = mGroupMenuDataArrayList.get(groupPosition);
        ChildMenuData childMenuData = groupMenuData.getChildMenuDataArrayList().get(childPosition);

        ImageView childImageView = (ImageView) convertView.findViewById(R.id.child_image_view);
        TextView childTextView = (TextView) convertView.findViewById(R.id.child_text_view);
        if (childMenuData.getChildResourceIcon() != null) {
            childImageView.setImageResource(childMenuData.getChildResourceIcon());
        }
        childTextView.setText(childMenuData.getChildTitle());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
