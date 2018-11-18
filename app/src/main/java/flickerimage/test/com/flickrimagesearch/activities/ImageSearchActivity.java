package flickerimage.test.com.flickrimagesearch.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import flickerimage.test.com.flickrimagesearch.R;
import flickerimage.test.com.flickrimagesearch.adapter.ImageSearchAdapter;
import flickerimage.test.com.flickrimagesearch.apiservice.ImageSearchRequest;
import flickerimage.test.com.flickrimagesearch.apiservice.RequestCallBack;
import flickerimage.test.com.flickrimagesearch.apiservice.RequestManager;
import flickerimage.test.com.flickrimagesearch.apiservice.ServerReqestTypes;
import flickerimage.test.com.flickrimagesearch.modelobjects.FlickerPhoto;
import flickerimage.test.com.flickrimagesearch.utility.GridEndlessRecyclerViewScrollListener;

/**
 * This class helps to search for the user input and dispalys the search results from API
 */
public class ImageSearchActivity extends SuperActivity implements RequestCallBack,GridEndlessRecyclerViewScrollListener.DataLoader {


    private static final String TAG = ImageSearchActivity.class.getSimpleName();
    private static int STORAGE_PERMISSION_REQUEST_CODE = 111;
    private static final int GRID_SPAN_COUNT = 3;
    private RecyclerView searchRecyclerView;
    private ImageSearchAdapter imageSearchAdapter;
    private ProgressBar progressBar;
    private GridLayoutManager mGridLayoutManager;
    private GridEndlessRecyclerViewScrollListener scrollListener;
    private int pageNo = 0;
    boolean isPermissionsGranted;
    private String searchedTextString;
    private boolean isNoMoreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUi();

        requestStoragePermissions();
    }


    private void initUi()
    {
        setContentView(R.layout.activity_image_search);

        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        progressBar = findViewById(R.id.searchProgressbar);

        mGridLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = imageSearchAdapter.getItemViewType(position);
                if (type == ImageSearchAdapter.FOOTER_VIEW)
                    return GRID_SPAN_COUNT;
                else
                    return 1;

            }
        });
        searchRecyclerView.setLayoutManager(mGridLayoutManager);

        scrollListener = new GridEndlessRecyclerViewScrollListener(mGridLayoutManager,this);
        searchRecyclerView.addOnScrollListener(scrollListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem searchViewMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchViewMenuItem.getActionView();
        searchView.setQueryHint("Search");

        ImageView v = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);

        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do your search

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchTextStr) {
                if (isPermissionsGranted) {

                    if(isOnline()) {
                        if (searchTextStr != null) {
                            if (searchTextStr.length() > 0) {
                                pageNo = 0;
                                isNoMoreData = false;
                                if (imageSearchAdapter != null) {
                                    imageSearchAdapter.clearList();
                                }
                                if (scrollListener != null) {
                                    scrollListener.reset();
                                }

                                searchedTextString = searchTextStr;
                                doSearchImages(searchTextStr);
                            } else {
                                if (imageSearchAdapter != null)
                                    imageSearchAdapter.clearList();
                                isNoMoreData = false;
                                RequestManager.getInstance().clearAllRequests();

                            }
                        }
                    }else{
                       Toast.makeText(getApplicationContext(),"Network Unavailable, please try again later!",Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Takes the search value and hit the server api.
     * @param searchText
     */
    private void doSearchImages(String searchText) {

        pageNo = pageNo+1;

        Log.v(TAG, "#999 requested page number is->"+searchText+":"+String.valueOf(pageNo));

        //show progress bar
        if (pageNo == 1) {
           showLoader();
        }else{
            imageSearchAdapter.addLoadingFooter();
        }

        ImageSearchRequest searchRequest = new ImageSearchRequest(searchText, pageNo, this);
        RequestManager.getInstance().addRequest(searchRequest);
    }



    private void showDataInRecyclerView(List<FlickerPhoto> dataList) {
        if (dataList != null) {
            if (imageSearchAdapter == null) {

                imageSearchAdapter = new ImageSearchAdapter(this, dataList);
                searchRecyclerView.setAdapter(imageSearchAdapter);
            } else {
                if (pageNo == 1) {
                    imageSearchAdapter.addListToExistingList(dataList, true);
                    searchRecyclerView.scrollToPosition(0);
                } else
                    imageSearchAdapter.addListToExistingList(dataList, false);
            }
        }
    }


    private void requestStoragePermissions() {

        String[] permissionArry = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!isPermissionsGranted(permissionArry)) {
            ActivityCompat.requestPermissions(this, permissionArry, STORAGE_PERMISSION_REQUEST_CODE);
        } else
            isPermissionsGranted=true;
    }

    private boolean isPermissionsGranted(String[] permissionsArry) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            for (String permission : permissionsArry) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //PERMISSIONS GRANTED
                isPermissionsGranted=true;
            }
            else
            {
                //NOT GRANTED
                isPermissionsGranted = false;
                finish();
            }
        }
    }


    //Server response callbacks
    @Override
    public void onRequestSuccessFull(ServerReqestTypes serverReqestType, Object result,int pageNumber) {

        Log.v(TAG, "#999 request success for page number is->"+String.valueOf(pageNumber));

        if(pageNumber ==1) {
            hideLoader();
        }
        if (serverReqestType == ServerReqestTypes.IMAGE_SEARCH) {
            isNoMoreData = false;
            List<FlickerPhoto> dataList = (List<FlickerPhoto>) result;
            showDataInRecyclerView(dataList);
        }

    }

    @Override
    public void onRequestFailure(ServerReqestTypes serverReqestType, String errorMsg,int pageNumber) {
        Log.v(TAG, "#999 request failure for page number is->"+String.valueOf(pageNumber));
        if(pageNumber == 1) {
           hideLoader();
        }
        else{
            imageSearchAdapter.removeLoadingFooter();
            pageNo = pageNo-1;
            isNoMoreData = true;
        }
    }

    private void showLoader(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader(){
        progressBar.setVisibility(View.GONE);
    }
    @Override
    public boolean onLoadMore() {

        if(!isOnline()){
           return  false;
        }
        if(isNoMoreData){
            return false;
        }
        doSearchImages(searchedTextString);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.getInstance().stop();
    }

}
