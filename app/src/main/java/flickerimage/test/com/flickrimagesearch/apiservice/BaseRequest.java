package flickerimage.test.com.flickrimagesearch.apiservice;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import flickerimage.test.com.flickrimagesearch.utility.Constants;
import flickerimage.test.com.flickrimagesearch.utility.Utils;


/**
 * This BaseRequest class handles the common Network requests
 */
public abstract class BaseRequest implements Runnable
{

    private HttpURLConnection httpURLConnection;

    protected Object doGetServerRequest(RequestEntity requestEntity)
    {
        Object object;
        try
        {
            URL url = new URL(requestEntity.getUrl());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(requestEntity.getRequestType().name());
            httpURLConnection.setConnectTimeout(Constants.REQUEST_TIMEOUT);

            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            String resultantStr = readInputStream(in);
            if(Utils.isValidJson(resultantStr)){

                JSONObject jsonObject = new JSONObject(resultantStr);
                object = jsonObject;
            }
            else{
                object = resultantStr;
            }

            return object;

        } catch (IOException| JSONException  e) {
            return new JSONObject();
        }
        catch (Exception e) {
            return new JSONObject();
        }
        finally {
            httpURLConnection.disconnect();
        }

    }

    private String readInputStream(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

        } catch (SocketException | SocketTimeoutException e) {
            throw e;

        }catch (Exception e) {
            throw e;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString().trim();
    }
}
