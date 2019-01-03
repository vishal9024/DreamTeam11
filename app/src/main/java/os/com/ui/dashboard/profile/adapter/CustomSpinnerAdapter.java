package os.com.ui.dashboard.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import os.com.R;

import java.util.List;

/**
 * Created by pawan tanwar on 8/14/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private List<String> asr;

    public CustomSpinnerAdapter(Context context, List<String> asr) {
        this.asr = asr;
        activity = context;
    }

    public int getCount() {
        return asr.size();
    }

    public Object getItem(int i) {
        return asr.get(i);
    }

    public void updateList(List<String> asr) {
        if (this.asr.size()>0)
            this.asr.clear();
        this.asr = asr;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.custom_spinner_layout, parent, false);
        }

        TextView txt=convertView.findViewById(R.id.txtItem);

        //TextView txt = new TextView(activity);
        // txt.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);
        //txt.setPadding(16, 16, 16, 16);
        // txt.setTextSize(activity.getResources().getDimension(R.dimen._5ssp));
//        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown, 0);
        txt.setText(asr.get(position));
//        txt.setCompoundDrawablePadding(10);
        //txt.setTextColor(Color.parseColor("#767efd"));
        return convertView;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {

        if (view == null) {
            view = LayoutInflater.from(activity).
                    inflate(R.layout.custom_spinner_layout, viewgroup, false);
        }


        TextView txt=view.findViewById(R.id.txtItem);

        //TextView txt = new TextView(activity);
       // txt.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);
        //txt.setPadding(16, 16, 16, 16);
       // txt.setTextSize(activity.getResources().getDimension(R.dimen._5ssp));
//        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown, 0);
        txt.setText(asr.get(i));
//        txt.setCompoundDrawablePadding(10);
        //txt.setTextColor(Color.parseColor("#767efd"));
        return view;
    }

}