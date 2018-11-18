package flickerimage.test.com.flickrimagesearch.apiservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * RequestManager handles API request through pool
 */
public class RequestManager
{

    private static RequestManager requestManager;
    private ExecutorService executorService;
    private ArrayList<Future> futureList;

    private RequestManager()
    {
        executorService = Executors.newFixedThreadPool(4);
        futureList = new ArrayList<Future>();
    }

    public static RequestManager getInstance() {
        if (requestManager == null) {
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    public void addRequest(BaseRequest request){

        //Assuming that for now we have only multiple search requests so clear the previous search request for better user expereince
        //To cancel any other type of requests we have to subclass the Future class and set some tag.
        if(request instanceof ImageSearchRequest)
        {
            clearAllRequests();
        }

        Future future =  executorService.submit(request);
        futureList.add(future);
    }

    public void clearAllRequests()
    {
        try
        {
            for (int i = 0; i < futureList.size(); i++) {
                Future future = futureList.get(i);
                if (!future.isCancelled()) {
                    future.cancel(true);
                }
            }
            futureList.clear();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void stop()
    {
        try
        {
            executorService.shutdownNow();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
