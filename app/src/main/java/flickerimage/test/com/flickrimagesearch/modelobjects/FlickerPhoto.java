package flickerimage.test.com.flickrimagesearch.modelobjects;

import flickerimage.test.com.flickrimagesearch.utility.Constants;

/**
 * This modal holds API data
 */
public class FlickerPhoto {
    private String id;
    private String title;
    private String secret;
    private String server;
    private int farm;
    private String url;

    public FlickerPhoto(String id, String title, String secret, String server, int farm) {
        this.id = id;
        this.title = title;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        generateImageURL();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }



    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }



    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getImgUrl(){
        return this.url;
    }
    //call this method in the constructor so that only one time image url will be created.
    private void generateImageURL(){

        //Format is  http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
        StringBuilder stringBuilder = new StringBuilder(Constants.IMG_URL_SCHEME);
        stringBuilder = stringBuilder.append(String.valueOf(farm));
        stringBuilder = stringBuilder.append(Constants.DOT_STR);
        stringBuilder = stringBuilder.append(Constants.IMG_URL_HOST);
        stringBuilder = stringBuilder.append(Constants.SLASH_STR);
        stringBuilder = stringBuilder.append(server);
        stringBuilder = stringBuilder.append(Constants.SLASH_STR);
        stringBuilder = stringBuilder.append(id);
        stringBuilder = stringBuilder.append(Constants.UNDERSCORE_STR);
        stringBuilder = stringBuilder.append(secret);
        stringBuilder = stringBuilder.append(Constants.DOT_STR);
        stringBuilder = stringBuilder.append(Constants.IMG_URL_JPG_STR);

        this.url = stringBuilder.toString();
    }

}
