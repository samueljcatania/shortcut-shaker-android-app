package com.example.shortcutshaker.introduction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class MyViewPagerAdapter extends PagerAdapter {

    /**
     * Instantiates a layout XML file into its corresponding View objects
     */
    private LayoutInflater layoutInflater;

    /**
     * Controls the different xml slider layouts
     */
    private int[] layouts;

    /**
     * Interface to global information about an application environment
     */
    private Context context;

    //**********************************************************************************************

    /**
     * Constructor - Gets layouts and context from Introduction class
     */
    public MyViewPagerAdapter(int[] layouts, Context context) {
        this.layouts = layouts;
        this.context = context;

    }//End of MyViewPageAdapter

    /**
     * Counts the number of layouts
     */
    @Override
    public int getCount() {

        return layouts.length;
    }//End of getCount

    /**
     * Checks if current view is from layout
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }//End of isViewFromObject

    /**
     * Instantiates next layout when user advances
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(layouts[position], container, false);
        container.addView(v);
        return v;
    }//End of instantiateItem

    /**
     * Destroys layout when user advances to next layout
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        View v = (View) object;
        container.removeView(v);
    }//End of destroyItem

}//End of class MyViewPagerAdapter
