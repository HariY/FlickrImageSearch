package flickerimage.test.com.flickrimagesearch.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static boolean isValidJson(String inputStr){
        JSONObject jsonObject = null;
        JSONArray jsonArry = null;
        try
        {
            jsonObject = new JSONObject(inputStr);
        }
        catch (JSONException ex1)
        {
            try {
                jsonArry = new JSONArray(inputStr);
            } catch (JSONException ex2) {
                return false;
            }
        }
        return true;
    }

    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            while(true) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
