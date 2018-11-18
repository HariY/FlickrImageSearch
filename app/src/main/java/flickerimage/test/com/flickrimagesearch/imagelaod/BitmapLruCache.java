package flickerimage.test.com.flickrimagesearch.imagelaod;

import android.graphics.Bitmap;
import android.util.LruCache;


/**
 * This saves downloaded images to RAM cache based on LRU principles
 */
public class BitmapLruCache extends LruCache<String,Bitmap> {

    public BitmapLruCache(int maxSizeInKB ) {
        super( maxSizeInKB );
    }

    @Override
    protected int sizeOf( String key, Bitmap value ) {
        //in kb
        return value.getByteCount()/1024;
    }

    @Override
    protected void entryRemoved( boolean evicted, String key, Bitmap oldValue, Bitmap newValue ) {
        if (oldValue != null && !oldValue.isRecycled()) {
            oldValue.recycle();
            oldValue = null;
        }

    }
}
