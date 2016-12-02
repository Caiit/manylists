package com.example.cait.lagrand_pset5;

import android.content.Intent;
import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

class ToDoExpandableListAdapter extends BaseExpandableListAdapter {


    private final ArrayList<Group> groups;
    private LayoutInflater inflater;
    private Activity activity;

    ToDoExpandableListAdapter(Activity act, ArrayList<Group> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).subItems.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ToDoItem todo = (ToDoItem) getChild(groupPosition, childPosition);
        final String children = todo.getTitle();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.todo_subitems, null);
        }
        TextView text = (TextView) convertView.findViewById(R.id.subItemText);
        text.setText(children);
        final View finalConvertView = convertView;
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todo.getCompleted()) {
                    todo.setCompleted(false);
                    finalConvertView.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    todo.setCompleted(true);
                    finalConvertView.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }

                // Connect to the database
                ToDoManager dbManager = ToDoManager.getInstance(activity);
                dbManager.updateToDoItem(todo);
            }
        });

        // Set background colour
        if (todo.getCompleted()) {
            convertView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).subItems.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.todo_group, null);
        }
        Group group = (Group) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);

        // On long click to edit page
        ExpandableListView eListView = (ExpandableListView) activity.findViewById(R.id.expandableListView);
        eListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

                    Group group = (Group) getGroup(position);
                    Intent toEdit = new Intent(activity, EditActivity.class);
                    toEdit.putExtra("id", group.id);
                    toEdit.putExtra("title", group.string);
                    activity.startActivity(toEdit);
                    return true;
                }
                return false;
            }
        });


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}