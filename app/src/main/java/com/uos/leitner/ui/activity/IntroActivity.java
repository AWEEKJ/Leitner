package com.uos.leitner.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.uos.leitner.R;

public class IntroActivity extends Activity {

    private ViewPager mPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mPager = (ViewPager) findViewById(R.id.intro_pager);
        mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
    }

    private View.OnClickListener mButtonClick = new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId()) {
                case R.id.sign_in:
                    //finish();
                    Intent intent1 = new Intent(getApplication(), IntroSigninActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.login_button:
                    //finish();
                    Intent intent2 = new Intent(getApplication(), IntroLoginActivity.class);
                    startActivity(intent2);
                    break;

            }
        }
    };

    private class PagerAdapterClass extends PagerAdapter {

        private LayoutInflater mInflater;

        public PagerAdapterClass(Context c){
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(View pager, int position) {
            View v = null;
            if(position==0){
                v = mInflater.inflate(R.layout.page_intro1, null);
            } else if(position==1){
                v = mInflater.inflate(R.layout.page_intro2, null);
            } else{
                v = mInflater.inflate(R.layout.page_intro3, null);
                v.findViewById(R.id.sign_in).setOnClickListener(mButtonClick);
                v.findViewById(R.id.login_button).setOnClickListener(mButtonClick);
            }

            ((ViewPager)pager).addView(v, 0);

            return v;
        }

        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager)pager).removeView((View)view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
        @Override public Parcelable saveState() { return null; }
        @Override public void startUpdate(View arg0) {}
        @Override public void finishUpdate(View arg0) {}
    }
}
