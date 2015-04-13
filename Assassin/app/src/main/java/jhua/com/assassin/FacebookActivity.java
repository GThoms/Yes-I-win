/**
 * http://stackoverflow.com/questions/11177009/how-to-use-dpi-scaling-factor-float-to-set-margins-int
 */
package jhua.com.assassin;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FacebookActivity extends ActionBarActivity {

    private Button login;
    Typeface font_light;
    Typeface font_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        font_light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        font_reg = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        TextView notlog = (TextView) findViewById(R.id.fb_text);
        notlog.setTypeface(font_light);

        login = (Button) findViewById(R.id.login_button);
        login.setTypeface(font_reg);

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) login.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(68);
                    lp.topMargin = px(12);
                    login.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(0);
                    lp.height = px(80);
                    login.setLayoutParams(lp);
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do fb log in stuff
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_facebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int px(float dips)
    {
        float DP = getResources().getDisplayMetrics().density;
        return Math.round(dips * DP);
    }
}
