package flickerimage.test.com.flickrimagesearch.utility;

public interface Constants {

    String HOST_URL = "https://api.flickr.com/";

    String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";

    String SEARCH_API_URL = "services/rest/?method=flickr.photos.search&api_key=" +
            API_KEY + "&format=json&nojsoncallback=1&safe_search=1";

    String IMG_URL_SCHEME= "http://farm";

    String IMG_URL_HOST= "static.flickr.com";

    String IMG_URL_JPG_STR= "jpg";

    String SLASH_STR= "/";

    String UNDERSCORE_STR= "_";

    String DOT_STR= ".";

    String EQUAL_SYMBOL = "=";

    String AMPERSAND_SYMBOL = "&";

    String KEY_TEXT = "text";

    String KEY_PAGE = "page";

    //Parser constants
    String KEY_PHOTOS = "photos";

    String KEY_PHOTO= "photo";

    String KEY_ID = "id";

    String KEY_OWNER = "owner";

    String KEY_SECRET = "secret";

    String KEY_SERVER = "server";

    String KEY_FARM = "farm";

    String KEY_TITLE = "title";

    String API_ERROR_MSG = "Something went wrong Please try again.";

    int REQUEST_TIMEOUT = 30000;
}
