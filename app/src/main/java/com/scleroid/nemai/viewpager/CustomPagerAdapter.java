package com.scleroid.nemai.viewpager;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.scleroid.nemai.fragment.MoviesFragment;

/**
 * Created by anupamchugh on 26/12/15.
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return new MoviesFragment();
    }

   /* @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObject modelObject = ModelObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }*/

    @Override
    public int getCount() {
        return ModelObject.values().length;
    }

    /*   @Override
       public boolean isViewFromObject(View view, Object object) {
           return view == object;
       }
   */
    @Override
    public CharSequence getPageTitle(int position) {
        ModelObject customPagerEnum = ModelObject.values()[position];

        return mContext.getString(customPagerEnum.getTitleResId());
    }


   /*@Override
    public float getPageWidth(int position) {
        return(0.5f);
    }
   */


}
