package leesd.crossithackathon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import leesd.crossithackathon.Cpi.CpiView;
import leesd.crossithackathon.Grievance.GrievanceView;
import leesd.crossithackathon.Info.DetailActivity;
import leesd.crossithackathon.adapter.MainAdapter;
import leesd.crossithackathon.model.SlideObj;

public class MainActivity  extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private ImageView drawerMenu;

    private DrawerLayout drawer;
    private View drawerView;

    @BindArray(R.array.name) String [] poName;
    @BindArray(R.array.url) String [] poUrl;

    @BindView(R.id.main_recycler_view) RecyclerView mainRecyclerView;

    private LinearLayoutManager linearLayoutManager;
    private MainAdapter mainAdapter;
    private List<SlideObj> res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        recyclerInit();
        drawerInit();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void recyclerInit() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        res = new ArrayList<>();
        recyclerData();

        mainAdapter = new MainAdapter(this,res);
        mainRecyclerView.setAdapter(mainAdapter);
        mainRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void drawerInit(){
        drawerMenu = (ImageView)findViewById(R.id.drawer_menu);
        drawerView = (View)findViewById(R.id.drawer_view);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(drawerView);
            }
        }); //고충민원 버튼 클릭 시, 하위메뉴들이 보인다.
        findViewById(R.id.complaintState).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GrievanceView.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.survey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SurveyActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cpiState).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CpiView.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        markerInit(mMap);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String temp = marker.getSnippet();

                if(temp!= null && temp.equals("Hackathon")) {
                    return false;
                }

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("markerData",marker.getTitle());
                startActivity(intent);

                drawerMenu.setVisibility(View.INVISIBLE);

                return false;
            }
        });


        enableMyLocation();

    }


    //국민안전처: 2017년 7월 정부 조직 개편으로 행정자치부(현 행정안전부)에 흡수 · 통합되면서 폐지
    //중소기업청 -> 중소벤처기업부
    //행정자치부 -> 행정안전부
    //미래창조과학부 -> 과학기술정보통신부

    private void markerInit(GoogleMap mMap){

        LatLng police = new LatLng(37.562022, 126.967911); //경찰청
        LatLng customs = new LatLng(37.518292, 127.034441); //관세청
        LatLng education = new LatLng(37.574864, 126.975062); //교육부
        LatLng army = new LatLng(37.533553, 126.977663); //국방부
        LatLng taxService = new LatLng(36.485153, 127.260327); //국세청
        LatLng meteorological = new LatLng(37.493746, 126.917008); //기상청
        LatLng justice = new LatLng(37.426890, 126.984886); //법무부
        LatLng legislation = new LatLng(37.574986, 126.975011); //법제처
        LatLng military = new LatLng(37.505555, 126.920005); //병무청
        LatLng forest = new LatLng(36.360688, 127.384195); //산림청
        LatLng foreign = new LatLng(37.573544, 126.975065); //외교부
        LatLng procurement = new LatLng(37.499241, 127.003168); //조달청
        LatLng statistical = new LatLng(36.361292, 127.384842); //통계청
        LatLng unification = new LatLng(37.574903, 126.975337); //통일부
        LatLng patent = new LatLng(37.500016, 127.032883); //특허청
        LatLng environment = new LatLng(36.505862, 127.262755); //환경부
        LatLng prosecutor = new LatLng(37.493889, 127.005062); //대검찰청
        LatLng heritage = new LatLng(36.360307, 127.381533); //문화재청
        LatLng employment = new LatLng(36.504114, 127.267748); //고용노동부
        LatLng affairs = new LatLng(36.503216, 127.261261); //국가보훈처
        //LatLng safety = new LatLng(36.483754, 127.260743); //국민안전처 -> 폐지(행정안전부로 흡수)
        LatLng land = new LatLng(36.505293, 127.263210); //국토교통부
        LatLng financial = new LatLng(37.574881, 126.975202); //금융위원회
        LatLng strategyFinance = new LatLng(36.505986, 127.266591); //기획재정부
        LatLng rural = new LatLng(35.831162, 127.056251); //농촌진흥청
        LatLng defense = new LatLng(37.425345, 126.983599); //방위사업청
        LatLng health = new LatLng(36.502451, 127.262196); //보건복지부
        LatLng gender = new LatLng(37.574881, 126.975206); //여성가족부
        LatLng personnel = new LatLng(36.508346, 127.261185); //인사혁신처
        LatLng smBusiness = new LatLng(36.360683, 127.384153); //중소기업청 -> 중소벤처기업부
        LatLng marine = new LatLng(36.362771, 127.291499); //해양수산부
        LatLng administration = new LatLng(37.574866, 126.975052); //행정자치부 -> 행정안전부
//        LatLng temp = new LatLng();// 새만금개발청
        LatLng temp1 = new LatLng(36.504448, 127.268001);// 공정거래위원회
        LatLng temp2 = new LatLng(36.505708, 127.259834);// 국민권익위원회
        LatLng temp3 = new LatLng(36.504507, 127.265147);// 농림축산식품부
        LatLng temp4 = new LatLng(36.497736, 127.265729);// 문화체육관광부
        LatLng temp5 = new LatLng(37.427014, 126.985063);// 미래창조과학부 -> 과학기술정보통신부
        LatLng temp6 = new LatLng(37.426830, 126.984753);// 방송통신위원회
        LatLng temp7 = new LatLng(36.499703, 127.260180);// 산업통상자원부
        LatLng temp8 = new LatLng(36.637992, 127.331872);// 식품의약품안전처
        LatLng temp9 = new LatLng(37.572186, 126.977827);// 원자력안전위원회
        LatLng temp10 = new LatLng(36.505295, 127.262903);// 행정중심복합도시건설청
        LatLng temp11 = new LatLng(37.907346, 127.725517);// 강원도교육청
        LatLng temp12 = new LatLng(37.298289, 127.022363);// 경기도교육청
        LatLng temp13 = new LatLng(35.242205, 128.688084);// 경상남도교육청
        LatLng temp14 = new LatLng(36.579566, 128.513099);// 경상북도교육청
        LatLng temp15 = new LatLng(34.815891, 126.469467);// 전라남도교육청
        LatLng temp16 = new LatLng(35.804492, 127.102610);// 전라북도교육청
        LatLng temp17 = new LatLng(36.657915, 126.677721);// 충청남도교육청
        LatLng temp18 = new LatLng(36.606704, 127.479427);// 충청북도교육청
        LatLng temp19 = new LatLng(35.147638, 126.879842);// 광주광역시교육청
        LatLng temp20 = new LatLng(35.858606, 128.614706);// 대구광역시교육청
        LatLng temp21 = new LatLng(36.352714, 127.383742);// 대전광역시교육청
        LatLng temp22 = new LatLng(35.176524, 129.064831);// 부산광역시교육청
        LatLng temp23 = new LatLng(37.570098, 126.966673);// 서울특별시교육청
        LatLng temp24 = new LatLng(35.562822, 129.302554);// 울산광역시교육청
        LatLng temp25 = new LatLng(37.456419, 126.703134);// 인천광역시교육청
        LatLng temp26 = new LatLng(36.478277, 127.286449);// 세종특별자치시교육청
        LatLng temp27 = new LatLng(33.490517, 126.498014);// 제주특별자치도교육청
//        LatLng temp28 = new LatLng();// 강원도
//        LatLng temp29 = new LatLng();// 경기도
//        LatLng temp30 = new LatLng();// 경상남도
//        LatLng temp31 = new LatLng();// 경상북도
//        LatLng temp32 = new LatLng();// 전라남도
//        LatLng temp33 = new LatLng();// 전라북도
//        LatLng temp34 = new LatLng();// 충청남도
//        LatLng temp35 = new LatLng();// 충청북도
//        LatLng temp36 = new LatLng();// 광주광역시
//        LatLng temp37 = new LatLng();// 대구광역시
//        LatLng temp38 = new LatLng();// 대전광역시
//        LatLng temp39 = new LatLng();// 부산광역시
//        LatLng temp40 = new LatLng();// 서울특별시
//        LatLng temp41 = new LatLng();// 울산광역시
//        LatLng temp42 = new LatLng();// 인천광역시
//        LatLng temp43 = new LatLng();// 세종특별자치시
//        LatLng temp44 = new LatLng();// 제주특별자치도(제주시, 서귀포시 포함)



        mMap.addMarker(new MarkerOptions().position(police).title("경찰청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(customs).title("관세청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(education).title("교육부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(army).title("국방부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(taxService).title("국세청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(meteorological).title("기상청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(justice).title("법무부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(legislation).title("법제처").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(military).title("병무청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(forest).title("산림청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(foreign).title("외교부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(procurement).title("조달청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(statistical).title("통계청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(unification).title("통일부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(patent).title("특허청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(environment).title("환경부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(prosecutor).title("대검찰청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(heritage).title("문화재청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(employment).title("고용노동부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(affairs).title("국가보훈처").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        //mMap.addMarker(new MarkerOptions().position(safety).title("국민안전처").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(land).title("국토교통부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(financial).title("금융위원회").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(strategyFinance).title("기획재정부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(rural).title("농촌진흥청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(defense).title("방위사업청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(health).title("보건복지부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(gender).title("여성가족부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(personnel).title("인사혁신처").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(smBusiness).title("중소벤처기업부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(marine).title("해양수산부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(administration).title("행정안전부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp).title("새만금개발청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp1).title("공정거래위원회").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp2).title("국민권익위원회").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp3).title("농림축산식품부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp4).title("문화체육관광부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp5).title("과학기술정보통신부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp6).title("방송통신위원회").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp7).title("산업통상자원부").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp8).title("식품의약품안전처").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp9).title("원자력안전위원회").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp10).title("행정중심복합도시건설청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp11).title("강원도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp12).title("경기도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp13).title("경상남도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp14).title("경상북도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp15).title("전라남도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp16).title("전라북도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp17).title("충청남도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp18).title("충청북도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp19).title("광주광역시교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp20).title("대구광역시교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp21).title("대전광역시교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp22).title("부산광역시교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp23).title("서울특별시교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp24).title("울산광역시교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp25).title("인천광역시교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp26).title("세종특별자치시교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.addMarker(new MarkerOptions().position(temp27).title("제주특별자치도교육청").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp28).title("강원도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp29).title("경기도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp30).title("경상남도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp31).title("경상북도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp32).title("전라남도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp33).title("전라북도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp34).title("충청남도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp35).title("충천북도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp36).title("광주광역시").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp37).title("대구광역시").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp38).title("대전광역시").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp39).title("부산광역시").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp40).title("서울특별시").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp41).title("울산광역시").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp42).title("인천광역시").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp43).title("세종특별자치시").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
//        mMap.addMarker(new MarkerOptions().position(temp44).title("제주특별자치도").icon( BitmapDescriptorFactory.fromResource(R.drawable.markersmall)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(police, 10));

    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerMenu.setVisibility(View.VISIBLE);
    }

    private void recyclerData() {

        for(int i=0; i<poName.length;i++){
            SlideObj temp = new SlideObj(poName[i],poUrl[i%poUrl.length]);
            res.add(temp);
        }

    }

}
