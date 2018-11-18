package flickerimage.test.com.flickrimagesearch.apiservice;

import android.os.Handler;

import org.json.JSONObject;

import java.util.List;

import flickerimage.test.com.flickrimagesearch.modelobjects.FlickerPhoto;
import flickerimage.test.com.flickrimagesearch.parser.JsonParser;
import flickerimage.test.com.flickrimagesearch.utility.Constants;

/**
 * This class handle Search API calls
 */
public class ImageSearchRequest extends BaseRequest{

    private int pageNumber;
    private String searchStr;
    private Handler handler;
    private RequestCallBack callBackListener;

    public ImageSearchRequest(String searchStr, int pageNumber,RequestCallBack callBackListener)
    {
        this.searchStr = searchStr;
        this.pageNumber = pageNumber;
        this.callBackListener = callBackListener;
        this.handler = new Handler();
    }

    private List<FlickerPhoto> getSearchdata(String searchStr, int pageNumber)
    {

        List<FlickerPhoto> dataList =null;
        try
        {
            StringBuilder url = new StringBuilder(Constants.HOST_URL);
            url =url.append(Constants.SEARCH_API_URL);
            url =url.append(Constants.AMPERSAND_SYMBOL);
            url =url.append(Constants.KEY_TEXT);
            url =url.append(Constants.EQUAL_SYMBOL);
            url =url.append(searchStr);
            url =url.append(Constants.AMPERSAND_SYMBOL);
            url =url.append(Constants.KEY_PAGE);
            url =url.append(Constants.EQUAL_SYMBOL);
            url =url.append(String.valueOf(pageNumber));

            RequestEntity requestEnity = new RequestEntity(url.toString(), RequestEntity.RequestTypes.GET);
            Object obj =  doGetServerRequest(requestEnity);
            if(obj instanceof JSONObject){
                JSONObject resultObj = (JSONObject)obj;
                JsonParser jsonParser = new JsonParser();
                dataList = jsonParser.parseImageSearchResponse(resultObj);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return dataList;
    }

    @Override
    public void run() {
        if(!Thread.currentThread().isInterrupted()) {

            final List<FlickerPhoto> dataList = getSearchdata(this.searchStr, this.pageNumber);

            if(!Thread.currentThread().isInterrupted())
            {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (dataList != null && dataList.size() > 0) {
                            callBackListener.onRequestSuccessFull(ServerReqestTypes.IMAGE_SEARCH, dataList,pageNumber);
                        } else {
                            callBackListener.onRequestFailure(ServerReqestTypes.IMAGE_SEARCH, Constants.API_ERROR_MSG,pageNumber);
                        }
                    }
                });
            }
        }
    }
}
