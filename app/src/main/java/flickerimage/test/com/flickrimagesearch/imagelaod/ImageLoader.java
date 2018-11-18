package flickerimage.test.com.flickrimagesearch.imagelaod;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

import flickerimage.test.com.flickrimagesearch.FlickerImageSearch;
import flickerimage.test.com.flickrimagesearch.R;
import flickerimage.test.com.flickrimagesearch.customviews.FlickerImageView;
import flickerimage.test.com.flickrimagesearch.utility.Utils;


public class ImageLoader {


    private static ImageLoader imageLoader;
    private ExecutorService executorService;
    private BitmapLruCache memoryCache;
    private FileCache fileCache;
    private Map<FlickerImageView, String> imageViews;
    private int IMAGE_SIZE_WIDTH;
    private android.os.Handler handler;

    private ImageLoader(Context context) {
        executorService = Executors.newFixedThreadPool(4);
        imageViews = Collections.synchronizedMap(new WeakHashMap<FlickerImageView, String>());

        initLruCache(context);
        initFileCache(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        IMAGE_SIZE_WIDTH = displayMetrics.widthPixels/3;
        handler  = new android.os.Handler();
    }

    public static ImageLoader getInstance(Context context) {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(context);
        }
        return imageLoader;
    }


    //initialize LRU bitmap cache
    private void initLruCache(Context contex) {
        FlickerImageSearch application = (FlickerImageSearch) contex.getApplicationContext();
        int cacheSizeInKb = application.getCacheSize();
        memoryCache = new BitmapLruCache(cacheSizeInKb);
    }

    //initialize File bitmap cache
    private void initFileCache(Context contex) {
        fileCache = new FileCache(contex);
    }

    public void displayImage(String url, int loader, FlickerImageView imageView) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        else {
            imageView.setImageResource(R.mipmap.ic_launcher);
            BitmapLoader bitmapLoader = new BitmapLoader(imageView);
            executorService.submit(bitmapLoader);
        }
    }


    private class BitmapLoader implements Runnable {
        FlickerImageView imageToDownLoad;

        BitmapLoader(FlickerImageView imageToDownLoad) {
            this.imageToDownLoad = imageToDownLoad;
        }

        @Override
        public void run() {
            if (isViewReused(imageToDownLoad))
            {
                return;
            }

            Bitmap bmp = getBitmap(imageToDownLoad.getImgUrl());

            memoryCache.put(imageToDownLoad.getImgUrl(), bmp);

            if (isViewReused(imageToDownLoad))
            {
                return;
            }

            imageToDownLoad.setDownLoadedBitmap(bmp);
            handler.post(imageToDownLoad);
        }
    }

    private Bitmap getBitmap(String url) {

        Bitmap bitmap = null;
        try {
            //Load from disc
            File f = fileCache.getFile(url);
            if (f.length() > 0) {
                bitmap = decodeBitmapFile(f);
            }
            if (bitmap != null)
                return bitmap;

            //Download from server
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.copyStream(is, os);
            os.close();

            bitmap = decodeBitmapFile(f);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmap;
    }


    private Bitmap decodeBitmapFile(File f) {
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options Options = new BitmapFactory.Options();
            Options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, Options);

            // Calculate inSampleSize
            Options.inSampleSize = calculateScaleValue(Options,  IMAGE_SIZE_WIDTH, IMAGE_SIZE_WIDTH);

            // Decode bitmap with inSampleSize set
            Options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, Options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  int calculateScaleValue(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public boolean isViewReused(FlickerImageView photoToLoad) {
        String tag = imageViews.get(photoToLoad);
        return tag == null || !tag.equals((String) photoToLoad.getTag());
    }


    public void clearMemCache()
    {
        if(memoryCache!=null) {
            memoryCache.evictAll();
        }
    }

}
