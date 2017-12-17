package leesd.crossithackathon.Info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leesd.crossithackathon.Fb.FbObject;
import leesd.crossithackathon.R;
import leesd.crossithackathon.model.DetailUserReview;

/**
 * Created by cmtyx on 2017-12-17.
 */

public class DetailChatActivity extends AppCompatActivity implements ReviewItemAdapter.OnReviewItemRemoveListener {
    @BindView(R.id.view_pager_review) ViewPager reviewPager;
    @BindView(R.id.tab_layout_detail_review) TabLayout tabLayout;
    @BindView(R.id.tab_layout_detail_review_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tab_layout_detail_review_submit) TextView submit;
    @BindView(R.id.detail_review_back) ImageView back;
    private ReviewItemAdapter reviewItemAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TabAdapter_Detail_Review pagerAdapterReview;
    private String markerData;
    private List<DetailUserReview> res;

    public static String userReviewContents;
    public static float userReviewRating;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chat);
        ButterKnife.bind(this);
        reviewTabInit();
        reviewRecyclerInit();
    }

    private void reviewSubmit(String review, float rating) {
        String time = new SimpleDateFormat("yy/MM/dd").format(Calendar.getInstance().getTime());
        String key = FbObject.database.getReference().child("Review").child(markerData).push().getKey();
        final DetailUserReview detailUserReview = new DetailUserReview(
                FbObject.firebaseAuth.getCurrentUser().getDisplayName(),
                review,rating,time,key,FbObject.firebaseAuth.getCurrentUser().getUid());

        FbObject.database.getReference()
                .child("Review")
                .child(markerData)
                .child(key)
                .setValue(detailUserReview).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               dataTransaction(detailUserReview.getUserStarRating());
            }
        });
    }

    private void dataTransaction(final float rating) {
        FbObject.database.getReference().child("Rating").child(markerData).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ReviewTotalRating reviewTotalRating = mutableData.getValue(ReviewTotalRating.class);
                if(reviewTotalRating == null) {
                    Log.d("qwe","null1");
                    reviewTotalRating = new ReviewTotalRating(rating,1);
                } else {
                    reviewTotalRating.setRating(reviewTotalRating.getRating() + rating);
                    reviewTotalRating.setTotal(reviewTotalRating.getTotal() + 1);
                }
                mutableData.setValue(reviewTotalRating);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"평가가 제출되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReviewData() {
        FbObject.database.getReference("Review")
                .child(markerData).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                res.clear();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()) {
                    DetailUserReview temp = iterator.next().getValue(DetailUserReview.class);
                    res.add(0,temp);
                }
                reviewItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void reviewRecyclerInit() {
        res = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        reviewItemAdapter = new ReviewItemAdapter(this,res,this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewItemAdapter);
        getReviewData();
    }

    private void reviewTabInit() {
        userReviewContents = "";
        userReviewRating = 0;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        markerData = getIntent().getStringExtra("markerData");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userReviewContents !="" && userReviewRating != 0) {
                    reviewSubmit(userReviewContents,userReviewRating);
                } else {
                    Toast.makeText(getApplicationContext(),"평가를 남겨주세요!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        pagerAdapterReview = new TabAdapter_Detail_Review(getSupportFragmentManager(), tabLayout.getTabCount());
        reviewPager.setAdapter(pagerAdapterReview);
        reviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(reviewPager,true);

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                reviewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void removeItem(DetailUserReview item) {
        if(item.getUserKey().equals(FbObject.firebaseAuth.getCurrentUser().getUid())) {
            final float rating = item.getUserStarRating();
            FbObject.database.getReference().child("Review").child(markerData).child(item.getKey()).removeValue();
            FbObject.database.getReference().child("Rating").child(markerData).runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    ReviewTotalRating reviewTotalRating = mutableData.getValue(ReviewTotalRating.class);
                    reviewTotalRating.setRating(reviewTotalRating.getRating() - rating);
                    reviewTotalRating.setTotal(reviewTotalRating.getTotal() - 1);
                    mutableData.setValue(reviewTotalRating);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    Toast.makeText(getApplicationContext(), "삭제가 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}