package mbccjlkn.whatsonthemenu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    // CustomExpandableAdapter()
    // pre: none
    // post: creates a new CustomExpandableListAdapter
    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetail){
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    // getChild()
    // pre: none
    // post: returns the child at the specified group index and child index in the expandableViewList
    public Object getChild(int listPosition, int expandedListPosition){
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }


    @Override
    // getChildId()
    // pre: none
    // post: returns the the id of the specified index of the expandableViewList
    public long getChildId(int listPosition, int expandedListPosition){
        return expandedListPosition;
    }

    @Override
    // getChildView()
    // pre: none
    // post: sets the view for the childs view in the expandableViewList
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.exp_menu_item, null);
        }

        TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);

        expandedListTextView.setText(expandedListText);

        return convertView;
    }

    @Override
    // getChildrenCount
    // pre: none
    // post: retirns the number of children for the specified expandableViewList index
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    // getGroup()
    // pre: none
    // post: returns the name of the specified expandableViewList index
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    // getGroupCount()
    // pre: none
    // post: returns the number of groups on the expandableViewList
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    // getGroupId()
    // pre: none
    // post: returns the position in the list
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    // getGroupView()
    // pre: none
    // post: sets the expandableViewLists GroupView
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.menu_category, null);
        }

        TextView listTitleTextView =  convertView.findViewById(R.id.listTitle);

        listTitleTextView.setTypeface(null, Typeface.BOLD);

        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    // hasStableIDS()
    // pre: none
    // post: returns false
    public boolean hasStableIds() {
        return false;
    }

    @Override
    // isChildSelectable()
    // pre: none
    // post: returns true
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
