package com.qtctek.aladin.view.new_post.map_information.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.qtctek.aladin.R;
import com.qtctek.aladin.helper.ToastHelper;
import com.qtctek.aladin.view.new_post.activity.NewPostActivity;
import com.qtctek.aladin.view.new_post.map_information.adapter.PlaceAutocompleteAdapter;
import com.qtctek.aladin.view.post_news.activity.MainActivity;
import com.qtctek.aladin.view.post_news.fragment.MapPostNewsFragment;

import java.util.ArrayList;

/**
 * Created by anuj.sharma on 4/6/2016.
 */
public class SearchPlaceFragment extends Fragment implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,OnClickListener {

    private NewPostActivity mActivity;

    GoogleApiClient mGoogleApiClient;

    private RecyclerView mRecyclerView;
    PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    public EditText edtSearch;
    private ImageView mImvClear;
    private ImageView mImvBack;
    private RelativeLayout mRlBottom;
    public static ProgressBar PROGRESS_BAR_SEARCH_PLACE;

    private View mView;

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_place, container, false);

        mActivity = (NewPostActivity) getActivity();

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .build();;

        initViews();
        handleStart();

        return mView;
    }


    /*
   Initialize Views
    */
    private void initViews(){
        mRecyclerView = mView.findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        this.mRlBottom = mView.findViewById(R.id.rl_bottom);
        edtSearch = (EditText)mView.findViewById(R.id.edt_search);
        mImvClear = (ImageView)mView.findViewById(R.id.imv_clear);
        this.mImvBack = mView.findViewById(R.id.imv_back);
        this.PROGRESS_BAR_SEARCH_PLACE = mView.findViewById(R.id.progress_bar_search_place);

        mImvClear.setOnClickListener(this);
        mImvBack.setOnClickListener(this);
        this.mRlBottom.setVisibility(View.GONE);
    }

    private void handleStart(){

        this.edtSearch.requestFocus();
        this.edtSearch.setText("");

        mAdapter = new PlaceAutocompleteAdapter(mActivity, R.layout.item_place_search,
                mGoogleApiClient, BOUNDS_INDIA, null, this);



        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mImvClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    mImvClear.setVisibility(View.GONE);

                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    PROGRESS_BAR_SEARCH_PLACE.setVisibility(View.VISIBLE);
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    mActivity.getToastHelper().toast(getResources().getString(R.string.error_connect), ToastHelper.LENGTH_SHORT);
                    removeFragment();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        MapInformationFragment mapInformationFragment = (MapInformationFragment) mActivity.getSupportFragmentManager().getFragments().get(1);

        switch (v.getId()){
            case R.id.imv_clear:
                edtSearch.setText("");
                if(mAdapter!=null){
                    mAdapter.clearList();
                }
                break;
            case R.id.imv_back:
                this.edtSearch.setText("");

                mActivity.getKeyboardHelper().hideKeyboard(edtSearch);
                mapInformationFragment.flSearch.setVisibility(View.GONE);
                break;
            case R.id.ll_for_rent:
                ((MainActivity)getActivity()).viewPaper.setCurrentItem(0);
                MapPostNewsFragment.ON_EVENT_FOR_MAP_POST_NEWS.onNearBySearchForRent();

                mActivity.getKeyboardHelper().hideKeyboard(edtSearch);
                mapInformationFragment.flSearch.setVisibility(View.GONE);
                break;
            case R.id.ll_for_sale:
                ((MainActivity)getActivity()).viewPaper.setCurrentItem(0);
                MapPostNewsFragment.ON_EVENT_FOR_MAP_POST_NEWS.onNearBySearchForSale();

                mActivity.getKeyboardHelper().hideKeyboard(edtSearch);
                mapInformationFragment.flSearch.setVisibility(View.GONE);
                break;
        }

    }



    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {
        if(mResultList!=null){
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if(places.getCount()==1){

                            try{
                                MapInformationFragment mapInformationFragment = (MapInformationFragment) mActivity.getSupportFragmentManager().getFragments().get(1);

                                mActivity.getKeyboardHelper().hideKeyboard(edtSearch);
                                mapInformationFragment.moveCameraToLocationClick(places.get(0).getLatLng().latitude,
                                        places.get(0).getLatLng().longitude);
                            }
                            catch (Exception e){
                                mActivity.getToastHelper().toast("Lỗi xử lí!!!", ToastHelper.LENGTH_SHORT);
                                MapInformationFragment mapInformationFragment = (MapInformationFragment) mActivity.getSupportFragmentManager().getFragments().get(1);

                                mActivity.getKeyboardHelper().hideKeyboard(edtSearch);
                                mapInformationFragment.flSearch.setVisibility(View.GONE);
                            }

                            edtSearch.setText("");
                        }else {
                            mActivity.getToastHelper().toast("Lỗi xử lí!!!", ToastHelper.LENGTH_SHORT);
                        }
                    }
                });
            }
            catch (Exception e){

            }

        }
    }

    private void removeFragment(){
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}