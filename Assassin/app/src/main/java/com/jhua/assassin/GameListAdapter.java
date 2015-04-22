/*
 * Code adapted from:
 * http://jsharkey.org/blog/2008/08/18/separating-lists-with-headers-in-android-09/
 */

package com.jhua.assassin;

import java.util.LinkedHashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

public class GameListAdapter extends BaseAdapter {

	public final Map<String,Adapter> sections = new LinkedHashMap<String,Adapter>();
	public final ArrayAdapter<String> headers;  
    public final static int TYPE_SECTION_HEADER = 0;
    private static Context context;
	  
	public GameListAdapter(Context context) {
		this.context = context;
		headers = new ArrayAdapter<String>(context, R.layout.list_header);
	}
	
	public void addSection(String section, Adapter adapter) {
		this.headers.add(section);
		this.sections.put(section, adapter);		
	}

	@Override
	public Object getItem(int position) {
		for(Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if(position == 0) return section;
            if(position < size) return adapter.getItem(position - 1);
  
            // otherwise jump into next section
            position -= size;
        }
        return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        int sectionnum = 0;
        for(Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;
              
            // check if position inside this section
            if(position == 0) {
            	View v = headers.getView(sectionnum, convertView, parent);
            	//Set three sections' colors
            	if (sectionnum == 0) {
            		v.setBackgroundColor(context.getResources().getColor(R.color.green));
            	} else if (sectionnum == 1) {
            		v.setBackgroundColor(context.getResources().getColor(R.color.orange));
            	} else {
            		v.setBackgroundColor(context.getResources().getColor(R.color.red));
            	}
            	return v;
            }
            
            if(position < size) {
            	View v = adapter.getView(position - 1, convertView, parent);
				ImageButton rightIcon = (ImageButton) v.findViewById(R.id.right_icon);
				if (sectionnum == 0) {
	            	rightIcon.setOnClickListener(new currentGamesListener(context));
				} else if (sectionnum == 1) {
					rightIcon.setOnClickListener(new pendingGamesListener(context));
				} else {
					rightIcon.setOnClickListener(new completedGamesListener(context));
				}
            	return v;
            }
  
            // otherwise jump into next section  
            position -= size;  
            sectionnum++;  
        }        
        return null;  
    }

	public int getCount() {  
        // total together all sections, plus one for each section header  
        int total = 0;  
        for(Adapter adapter : this.sections.values())  
            total += adapter.getCount() + 1;  
        return total;  
    }
	
	public int getViewTypeCount() {  
        // assume that headers count as one, then total all sections  
        int total = 1;  
        for(Adapter adapter : this.sections.values())  
            total += adapter.getViewTypeCount();  
        return total;  
    }
	
	public boolean isEnabled(int position) {  
        return (getItemViewType(position) != TYPE_SECTION_HEADER);  
    }
	
	public int getItemViewType(int position) {  
        int type = 1;  
        for(Object section : this.sections.keySet()) {  
            Adapter adapter = sections.get(section);  
            int size = adapter.getCount() + 1;  
              
            // check if position inside this section   
            if(position == 0) return TYPE_SECTION_HEADER;  
            if(position < size) return type + adapter.getItemViewType(position - 1);  
  
            // otherwise jump into next section  
            position -= size;  
            type += adapter.getViewTypeCount();  
        }  
        return -1;  
    }
	
	// Listeners:
	public static class currentGamesListener implements OnClickListener {
		Context context;
		public currentGamesListener(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			showDialog('a');
		}
	}
	
	public static class pendingGamesListener implements OnClickListener {
		Context context;
		public pendingGamesListener(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			showDialog('p');
		}
	}

	public static class completedGamesListener implements OnClickListener {
		Context context;
		public completedGamesListener(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			showDialog('i');
		}
	}

	private static void showDialog(char type) {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(context);

		View promptView;
		if (type == 'p') { // pending
			promptView = layoutInflater.inflate(R.layout.pending_dialog, null);
		}
		else if (type == 'a') { // active
			promptView = layoutInflater.inflate(R.layout.leave_dialog, null);
		}
		else { // inactive
			promptView = layoutInflater.inflate(R.layout.delete_dialog, null);
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(promptView);

		// setup a dialog window
		alertDialogBuilder.setCancelable(false)
				.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

}
