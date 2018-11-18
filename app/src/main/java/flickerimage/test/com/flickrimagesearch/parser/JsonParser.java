package flickerimage.test.com.flickrimagesearch.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import flickerimage.test.com.flickrimagesearch.modelobjects.FlickerPhoto;
import flickerimage.test.com.flickrimagesearch.utility.Constants;

/**
 * This handles the JSON data parsing
 */
public class JsonParser {


    public List<FlickerPhoto>parseImageSearchResponse(JSONObject dataObj)
    {
        List<FlickerPhoto> dataList = new ArrayList<>();
        try
        {
            JSONObject resultObject = dataObj.getJSONObject(Constants.KEY_PHOTOS);
            JSONArray jsonArray = resultObject.getJSONArray(Constants.KEY_PHOTO);

            for(int i=0; i<jsonArray.length();i++)
            {

                JSONObject jsonObject = jsonArray.getJSONObject(i);


                FlickerPhoto flickerPhoto = new FlickerPhoto(jsonObject.getString(Constants.KEY_ID),
                                                    jsonObject.getString(Constants.KEY_TITLE),
                                                    jsonObject.getString(Constants.KEY_SECRET),
                                                    jsonObject.getString(Constants.KEY_SERVER),
                                                    jsonObject.getInt(Constants.KEY_FARM));
                dataList.add(flickerPhoto);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }

}
