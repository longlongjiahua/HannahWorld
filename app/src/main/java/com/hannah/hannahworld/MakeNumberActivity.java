package com.hannah.hannahworld;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MakeNumberActivity extends Activity {

    private TextView myCard;
    private static final String TextView_TAG = "The Android Logo";
    private GridView gridView;
    private FormulaAdapter mFormulaAdapter;
    private ArrayList<String> mFormElements = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_makenumberactivity);
        myCard = (TextView)findViewById(R.id.card0);
        // Sets the tag
        myCard.setTag(TextView_TAG);

        // set the listener to the dragging data
        myCard.setOnLongClickListener(new MyClickListener());

        findViewById(R.id.toplinear).setOnDragListener(new MyDragListener());
        findViewById(R.id.grid_view_formula).setOnDragListener(new MyDragListener());
        gridView = (GridView) findViewById(R.id.grid_view_formula);

        mFormElements.add("1");mFormElements.add("1");mFormElements.add("1");
        mFormElements.add("1");mFormElements.add("1");mFormElements.add("1");
        mFormElements.add("1");mFormElements.add("1");mFormElements.add("1");
        mFormElements.add("1");


        mFormulaAdapter = new FormulaAdapter(this, mFormElements);
        gridView.setAdapter(mFormulaAdapter);
    }
    public  class FormulaAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> keys;
        public FormulaAdapter(Context c, ArrayList<String> keys) {
            context = c;
            this.keys= keys;
        }
        public int getCount() {
            return keys.size();
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            Button btn;
            if (convertView == null) {
                btn = new Button(context);
                btn.setLayoutParams(new GridView.LayoutParams(75, 75));
                btn.setPadding(1,1,1,1);
                btn.setFocusable(false);
                btn.setClickable(false);
            } else {
                btn = (Button) convertView;
            }
            btn.setText(keys.get(position));
            btn.setTextColor(Color.WHITE);
            btn.setId(position);
            return btn;
        }
    }

    private final class MyClickListener implements OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            // TODO Auto-generated method stub

            // create it from the object's tag
            ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

            String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag( data, //data to be dragged
                    shadowBuilder, //drag shadow
                    view, //local data about the drag and drop operation
                    0   //no needed flags
            );
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class MyDragListener implements OnDragListener {
        Drawable normalShape = getResources().getDrawable(R.drawable.normal_shape);
        Drawable targetShape = getResources().getDrawable(R.drawable.rectangle_back2);

        @Override
        public boolean onDrag(View v, DragEvent event) {

            // Handles each of the expected events
            switch (event.getAction()) {

                //signal for the start of a drag and drop operation.
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;

                //the drag point has entered the bounding box of the View
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(targetShape);	//change the shape of the view
                    break;

                //the user has moved the drag shadow outside the bounding box of the View
                case DragEvent.ACTION_DRAG_EXITED:
                   //hello hi bye bye light bulb dumb goodbye v.setBackground(normalShape);	//change the shape of the view back to normal
                    break;

                //drag shadow has been released,the drag point is within the bounding box of the View
                case DragEvent.ACTION_DROP:
                    // if the view is the bottomlinear, we accept the drag item
                    if(v == findViewById(R.id.grid_view_formula)) {
                        View view = (View) event.getLocalState();
                       float x=  view.getX();
                        float y = view.getY();
                        Toast.makeText(getApplicationContext(),""+x+"xy:"+y, Toast.LENGTH_LONG);
                        //ViewGroup viewgroup = (ViewGroup) view.getParent();
                        //viewgroup.removeView(view);

                        //change the text
                        //TextView text = (TextView) v.findViewById(R.id.text);
                        //text.setText("The item is dropped");

                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                    } else {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Context context = getApplicationContext();
                        Toast.makeText(context, "You can't drop the Card here",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    break;

                //the drag and drop operation has concluded.
                case DragEvent.ACTION_DRAG_ENDED:
                   // v.setBackground(normalShape);	//go back to normal shape

                default:
                    break;
            }
            return true;
        }
    }
    private void getXYPosition(){

        int firstPosition = gridView.getFirstVisiblePosition();
        int lastPosition = gridView.getLastVisiblePosition();
        View curr = gridView.getChildAt(1 - firstPosition);
        float left = curr.getLeft();
        float right = curr.getRight();
        Toast.makeText(getApplication(),"place"+left+right, Toast.LENGTH_LONG);

        }
    }