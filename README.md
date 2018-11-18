**Over View**

The FlickerImageSearch enables users to search intended results of images. While the example application will work in a sandboxed environment when built, integrating the APIs for valid results. However this example uses sandbox APIs of Uber and may required to follow the access rules.

**Usage**

1. Accept the user input and search for appropriate images
2. Showcase the images in the user intuitive format to check for all in scrollable widget.
3. Image downloading works through lazy loaders so that to manage the respective cache mechanism and to keep up the user experience.
4. Application has effectively used the RAM/File cache.
5. Dynamically handles the network disconnects.
6. Perfect for pagination and handle the multiple search requests effectively by clearing the existed requests in-flow w.r.t user fresh inputs.

**Functional flow**
 Sequence diagrams explains the functional flow of the application. More details can be checked through below sequence charts.
 
![Screenshot](https://github.com/HariY/FlickrImageSearch/blob/master/sq1.png)
![Screenshot](https://github.com/HariY/FlickrImageSearch/blob/master/sq2.png)

**Gradle requirements**

1. MinSDKVersion : 15
2. TargetSDKVersion : 27
3. implementation 'com.android.support.constraint:constraint-layout:1.1.3'
 ￼ ￼

**Technical Components**

1. LRU Cache & File Cache mechanism.
2. Broadcast Receivers
3. Activities, Thread Pool Executers, HttpURLConnection, JSON Parsers & Adapters
4. Singleton and Delegate patters to maintain cache effectively.

**Improvements**

1. Preservation of data can be handled by placing the Android serialization(Through parcel able).
2. Callback mechanism can be improved for API calls for error handling.
3. Handle FILE cache as per the guidelines of DISK-LRU cache so that to optimise the storage usage space.
4. Can provide smooth animations while rendering the images.
5. Can use Staggered Layout Manager to render difference in image dimensions.
