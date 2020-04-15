package com.app.socialtruth.ui.steps;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.socialtruth.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import ernestoyaquello.com.verticalstepperform.Step;
import mehdi.sakout.fancybuttons.FancyButton;

public class ImageSelectionStep extends Step<String> {
    private RecyclerView videosListView;
    private String selectedVideoPath=null;

    public ImageSelectionStep(String stepTitle) {
        super(stepTitle);
    }
    private class VideoItem{
        String path;
        String id;
        String duration;
        String resolution;
        String size;
        public VideoItem( String path, String id,String duration,String resolution,String size){
            this.path = path;
            this.id = id;
            this.duration = duration;
            this.resolution = resolution;
            this.size = size;

        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImg;
        public View item;
        public FancyButton selection_btn;
        public ViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.imgView);
            selection_btn = itemView.findViewById(R.id.selection_btn);
            item = itemView;
        }
    }

    private class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<VideoItem> mImageItems;
        private static final int HEADER = 0;
        private static final int CONTENT = 1;
        private int previousSelected = -1;
        public ContentAdapter(ArrayList<VideoItem> mImageItems){
            this.mImageItems = mImageItems;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.video_selection, container, false));
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if(selectedVideoPath!=null){

                if(mImageItems.get(position).path.equals(selectedVideoPath))
                    holder.selection_btn.setVisibility(View.VISIBLE);
                else  holder.selection_btn.setVisibility(View.GONE);
            }
            else
                holder.selection_btn.setVisibility(View.GONE);


            //   holder.mImg.setImageURI(Uri.parse( mImageItems.get(position).path));
            // Utils.Get

            //   Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mImageItems.get(position).path, MediaStore.Video.Thumbnails.MICRO_KIND);
            //    holder.mImg.setImageBitmap(bitmap);
            holder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedVideoPath = mImageItems.get(position).path;
                    markAsCompletedOrUncompleted(true);
                    notifyItemChanged(position);
                    if(previousSelected!=-1)
                        notifyItemChanged(previousSelected);
                    previousSelected =position;
                }
            });

            Glide.with(holder.itemView.getContext())
                    .load(mImageItems.get(position).path) // or URI/path
                    .into(holder.mImg);
           // holder.videoLenTxtView.setText(milliSecondsToTimer(Long.parseLong(mImageItems.get(position).duration)));
           // holder.videoLenTxtView.setText(milliSecondsToTimer(Long.parseLong(mImageItems.get(position).duration)));
            final int i =position;


        }
        @Override
        public int getItemCount() {
            return mImageItems.size();
        }
    }
    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private void initImageRecyclerView(){
        Uri externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ArrayList<VideoItem> mImageItems = new ArrayList<>();
        String[] projection = {  MediaStore.Images.Media._ID

        };


        Cursor mCursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null,MediaStore.Images.Media.DATE_MODIFIED+" DESC");

        while (mCursor.moveToNext()) {

            long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri uri = ContentUris.withAppendedId(externalUri, id);
            mImageItems.add(new VideoItem(uri.toString(),""
                    ,""
                    ,""
                    ,""));
        }
        mCursor.close();
        GridLayoutManager listManager = new GridLayoutManager(getContext(),2, GridLayoutManager.HORIZONTAL, false);

        videosListView.setLayoutManager(listManager);

        videosListView.setAdapter(new ContentAdapter(mImageItems));
    }

    @Override
    protected View createStepContentLayout() {
        videosListView = new RecyclerView(getContext());
        initImageRecyclerView();
        return videosListView;
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(selectedVideoPath!=null, "No Image Selected");
    }

    @Override
    public String getStepData() {
        return selectedVideoPath;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        if(selectedVideoPath!=null){
        //    App.Log("Selected video name "+new File(selectedVideoPath).getName());
            return new File(selectedVideoPath).getName();
        }
        else
            return    "No Image Selected";
    }

    @Override
    protected void onStepOpened(boolean animated) {
        // This will be called automatically whenever the step gets opened.
    }

    @Override
    protected void onStepClosed(boolean animated) {
        // This will be called automatically whenever the step gets closed.
    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as completed.
    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as uncompleted.
    }

    @Override
    public void restoreStepData(String stepData) {
        this. selectedVideoPath = stepData;
    }
}
