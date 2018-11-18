package flickerimage.test.com.flickrimagesearch.apiservice;

public interface RequestCallBack {

    void onRequestSuccessFull(ServerReqestTypes serverReqestType, Object result,int pageNumner);

    void onRequestFailure(ServerReqestTypes serverReqestType ,String errorMsg,int pageNumner);


}
