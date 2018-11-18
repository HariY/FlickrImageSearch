package flickerimage.test.com.flickrimagesearch.customviews;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import flickerimage.test.com.flickrimagesearch.R;
import flickerimage.test.com.flickrimagesearch.imagelaod.ImageLoader;

/**
 * Handles the display of Image either through cache or network
 */
public class FlickerImageView extends AppCompatImageView implements  Runnable{

    private String imgUrl;
    private int index;
    private Bitmap bitmap;
    public FlickerImageView(Context context) {
        super(context);
    }

    public FlickerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlickerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showImage(String imgUrl,int index){

        if(imgUrl!=null) {
            this.imgUrl = imgUrl;
            this.index = index;
            setTag(imgUrl);
            this.setImageDrawable(null);
            ImageLoader loader = ImageLoader.getInstance(getContext());
            loader.displayImage(this.imgUrl, 1, this);
        }
    }

    public void setDownLoadedBitmap(Bitmap btm){
        this.bitmap = btm;
    }

    @Override
    public void run(){

        if(bitmap != null && !bitmap.isRecycled()) {
            this.setImageBitmap(bitmap);
        }else{
            this.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public String getImgUrl(){
        return this.imgUrl;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Object drawable = getDrawable();
        if(drawable !=null && drawable instanceof BitmapDrawable){
            Bitmap temp = ((BitmapDrawable)drawable).getBitmap();
            if (temp!=null && temp.isRecycled()) {
                return;
            }
        }

        super.onDraw(canvas);
    }
}
