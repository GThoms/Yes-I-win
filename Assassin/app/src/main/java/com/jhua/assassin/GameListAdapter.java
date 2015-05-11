/*
 * Code adapted from:
 * http://jsharkey.org/blog/2008/08/18/separating-lists-with-headers-in-android-09/
 */

package com.jhua.assassin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

//Adapter for game list
public class GameListAdapter extends BaseAdapter {

	public final Map<String,Adapter> sections = new LinkedHashMap<String,Adapter>();
	public final ArrayAdapter<String> headers;  
    public final static int TYPE_SECTION_HEADER = 0;
    private static Context context;
	  
	public GameListAdapter(Context context) {
		this.context = context;
		headers = new ArrayAdapter<String>(context, R.layout.list_header);
	}


    //Add completed, pending etc. adapters to this adapter
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
				final ImageButton rightIcon = (ImageButton) v.findViewById(R.id.right_icon);
                final ImageView leftIcon = (ImageView) v.findViewById(R.id.left_icon);
                if (sectionnum == 0) {
					rightIcon.setImageResource(R.drawable.exit);
                    leftIcon.setImageResource(R.drawable.ic_action_important);
	            	rightIcon.setOnClickListener(new currentGamesListener(context));
				} else if (sectionnum == 1) {
					// Pending games
					// If created by currentUser, set to start, else set to accept
					ParseUser user = ParseUser.getCurrentUser();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
                    query.whereEqualTo("status", "pending");
                    query.whereEqualTo("players", ParseUser.getCurrentUser());
                    final int pos = position;
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> gameList, ParseException e) {
                            if (e == null) {
                                int i = 0;
                                for ( ParseObject G : gameList ){
                                    if ((G.get("creator") != null) && (ParseUser.getCurrentUser().getUsername().equals(G.get("creator").toString()))) {
                                        rightIcon.setImageResource(R.drawable.ic_action_play);
                                        rightIcon.setOnClickListener(new pendingGamesCreatorListener(context));
                                    } else {
                                        rightIcon.setImageResource(R.drawable.ic_action_accept);
                                        rightIcon.setOnClickListener(new pendingGamesListener(context));
                                    }
                                    i++;
                                }
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                    leftIcon.setImageResource(R.drawable.pending);
					/*if (ParseUser.getCurrentUser().getUsername().toString().equals(ParseObject.get("game").get("creator").toString())) {
						rightIcon.setImageResource(R.drawable.ic_action_play);
                        leftIcon.setImageResource(R.drawable.pending);
						rightIcon.setOnClickListener(new pendingGamesCreatorListener(context));
					//}*/
					// rightIcon.setImageResource(R.drawable.accept);
					//rightIcon.setOnClickListener(new pendingGamesListener(context));
				} else {
					rightIcon.setOnClickListener(new completedGamesListener(context));
                    rightIcon.setImageResource(R.drawable.ic_action_discard);
                    leftIcon.setImageResource(R.drawable.ic_action_accept);
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
	
	public static class pendingGamesCreatorListener implements OnClickListener {
		Context context;
		public pendingGamesCreatorListener(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			showDialog('s');
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


    //Sets up dialogs for if we need them
	private static void showDialog(char type) {

		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(context);

        String positiveString = "";

		View promptView;
		if (type == 'p') { // pending
			promptView = layoutInflater.inflate(R.layout.pending_dialog, null);
            positiveString = "Join";
        }
		else if (type == 'a') { // active
			promptView = layoutInflater.inflate(R.layout.leave_dialog, null);
            positiveString = "Leave";
		} else if (type == 's') {
			promptView = layoutInflater.inflate(R.layout.start_dialog, null);
            positiveString = "Start";
		} else { // inactive
			promptView = layoutInflater.inflate(R.layout.delete_dialog, null);
            positiveString = "Delete";
        }

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(promptView);

        final String positive = positiveString;

		// setup a dialog window
		alertDialogBuilder.setCancelable(false)
				.setPositiveButton(positive, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
                        if (positive.equals("Join")) {
                            //join
                        } else if (positive.equals("Leave")) {
                            //leave
                        } else if (positive.equals("Start")) {
                            startGame();
                        } else if (positive.equals("Delete")) {
                            //delete
                        }
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

    // use the index of the click to get the game instance in pending
    private static void startGame() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
        query.whereEqualTo("status", "pending");
        query.whereEqualTo("players", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> gameList, ParseException e) {
                if (e == null) {
                    for ( ParseObject G : gameList ){
                        //I don't know how to access the specific game associated with the OnClickListener
                        //If we assume one pending game at a time, this should work? (This will start all pending games)
                        G.put("status", "current");
                        //Then go to the target activity
                        Intent intent = new Intent(context, TargetActivity.class);
                        context.startActivity(intent);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

}
