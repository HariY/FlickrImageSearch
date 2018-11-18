package flickerimage.test.com.flickrimagesearch.apiservice;

import java.util.Map;

/**
 * This is request modal which builds network paramets
 */
public class RequestEntity {

    private String url;
    private RequestTypes requestType ;
    private Map<String, String> params = null;
    private Map<String, String> headers = null;

    public RequestEntity(String url,RequestTypes requestType){
        this.url = url;
        this.requestType = requestType;
    }


    public String getUrl() {
        return url;
    }

    public RequestTypes getRequestType() {
        return requestType;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }



    public enum RequestTypes {
        GET,
        POST,
        DELETE,
        PUT;
    }
}
