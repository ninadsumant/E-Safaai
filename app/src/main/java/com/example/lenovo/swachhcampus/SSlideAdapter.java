package com.example.lenovo.swachhcampus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SSlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    // list of images
    public int[] lst_images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
    };
    // list of titles
    public String[] lst_title = {
            "Garbage Issues",
            "Water Leakage Issues",
            "Pot Holes Issues"
    }   ;
    // list of descriptions
    public String[] lst_description = {
            "Swachh Bharat Abhiyan",
            "Clean India Initiative",
            "Clean Pune, Green Pune"
    };

    // list of background colors
    public int[]  lst_backgroundcolor = {
            Color.rgb(55,55,55),
            Color.rgb(239,85,85),
            Color.rgb(110,49,89)

    };


    public SSlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst_title.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.societyslide,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelinearlayout);
        ImageView imgslide = (ImageView)  view.findViewById(R.id.slideimg);
        TextView txttitle= (TextView) view.findViewById(R.id.txttitle);
        TextView description = (TextView) view.findViewById(R.id.txtdescription);
        layoutslide.setBackgroundColor(lst_backgroundcolor[position]);
        imgslide.setImageResource(lst_images[position]);
        txttitle.setText(lst_title[position]);
        description.setText(lst_description[position]);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0)
                {
                    Toast.makeText(context, "Swipe for More!", Toast.LENGTH_SHORT).show();
                }
                if(position==1)
                {
                    Toast.makeText(context, "Click on the image!", Toast.LENGTH_SHORT).show();
                }
                if(position==2)
                {
                    Toast.makeText(context, "Click on the image!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0)
                {
                   Intent i = new Intent(context, SocietyComplaint.class);
                  context.startActivity(i);
                    //Toast.makeText(context, "slide 1 is clicked", Toast.LENGTH_SHORT).show();
                }
                else if(position==1)
                {
                    Intent i=new Intent(context,SocietyComplaint.class);
                    context.startActivity(i);
                    //Toast.makeText(context, "slide 2 is clicked", Toast.LENGTH_SHORT).show();
                }
                else if(position==2)
                {
                    Intent i = new Intent(context,SocietyComplaint.class);
                    context.startActivity(i);
                    //Toast.makeText(context, "slide 3 is clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }


}
