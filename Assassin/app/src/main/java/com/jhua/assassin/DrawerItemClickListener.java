package com.jhua.assassin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


//What happens when a navigation object is clicked anywhere in the application
public class DrawerItemClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectItem(position, view.getContext());
	}
	
	private void selectItem(int position, Context context) {
		switch (position) {
			case 0:
				Intent target = new Intent(context, TargetActivity.class);
				context.startActivity(target);
				break;
			case 1:
				Intent stats = new Intent(context, StatsActivity.class);
				context.startActivity(stats);
				break;
			case 2:
				Intent leader = new Intent(context, RemainingLeaderboard.class);
				context.startActivity(leader);
				break;
            case 3:
                Intent friends = new Intent(context, FriendListActivity.class);
                context.startActivity(friends);
                break;
			case 4:
				Intent main = new Intent(context, MainActivity.class);
				context.startActivity(main);
				break;
			case 5:
				Intent create = new Intent(context, CreateGameActivity.class);
				context.startActivity(create);
				break;
			case 6:
				Intent logout = new Intent(context, LogoutActivity.class);
				context.startActivity(logout);
			default:
				Log.d("e", "Oops");
		}
	}

}
