package com.svitlasystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.svitlasystem.networking.ScheduledService;
import com.svitlasystem.schedule.PollReceiver;
import com.svitlasystem.ui.map.page_adapter.PageFragmentAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PollReceiver.scheduleWork(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        PageFragmentAdapter adapter = new PageFragmentAdapter(this,
                getFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, ScheduledService.class));
    }


}
