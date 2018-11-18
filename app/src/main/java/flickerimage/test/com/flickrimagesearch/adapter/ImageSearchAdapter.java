package flickerimage.test.com.flickrimagesearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import flickerimage.test.com.flickrimagesearch.R;
import flickerimage.test.com.flickrimagesearch.customviews.FlickerImageView;
import flickerimage.test.com.flickrimagesearch.modelobjects.FlickerPhoto;

/**
 * This is an adapter which dispalys the images in Gridview
 */
public class ImageSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<FlickerPhoto> flickerPhotoList;

    private Context context;
    public static final int FOOTER_VIEW = 1;
    private boolean isLoaderAdded;


    public ImageSearchAdapter(Context mContext, List<FlickerPhoto> flickerPhotoList) {
        context=mContext;
        this.flickerPhotoList=flickerPhotoList;
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int)} . Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == FOOTER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(v);
            return vh;
        }
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_image, parent, false);
        SearchImageViewHolder searchImageViewHolder=new SearchImageViewHolder(v);
        return searchImageViewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof FooterViewHolder)
        {
        }
         else{
            FlickerPhoto flickerPhoto = flickerPhotoList.get(position);
            if (flickerPhoto.getImgUrl() != null) {
                ((SearchImageViewHolder) holder).searchImageView.showImage(flickerPhoto.getImgUrl(),position);
                ((SearchImageViewHolder) holder).photoTextView.setText(flickerPhoto.getTitle());

            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return (flickerPhotoList.size() + (isLoaderAdded? 1 : 0));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == flickerPhotoList.size() && isLoaderAdded) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    class SearchImageViewHolder extends RecyclerView.ViewHolder
    {
        FlickerImageView searchImageView;
        TextView photoTextView;

        public SearchImageViewHolder(View itemView) {
            super(itemView);

            searchImageView = itemView.findViewById(R.id.searchImageView);
            photoTextView = itemView.findViewById(R.id.photoText);

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder
    {

        public FooterViewHolder(View itemView) {
            super(itemView);

        }
    }

    public void clearList()
    {
        isLoaderAdded = false;
        if(flickerPhotoList !=null && flickerPhotoList.size()>0) {
            flickerPhotoList.clear();
            notifyDataSetChanged();
        }
    }

    public void addListToExistingList(List<FlickerPhoto> flickerPhotoList, boolean needToClearList)
    {
        if(needToClearList)
            this.flickerPhotoList.clear();
        this.flickerPhotoList.addAll(flickerPhotoList);
        isLoaderAdded = false;
        notifyDataSetChanged();

    }


    public List<FlickerPhoto> getFlickerPhotoList()
    {
        return flickerPhotoList;
    }

    public void addLoadingFooter() {
        isLoaderAdded = true;
        notifyDataSetChanged();
    }

    public void removeLoadingFooter() {
        isLoaderAdded = false;
        notifyDataSetChanged();
    }

}
