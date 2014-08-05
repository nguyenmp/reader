package com.nguyenmp.reader.loaders;

import android.content.Context;

import com.nguyenmp.reddit.data.Comments;

public class CommentsLoader extends MyAsyncTaskLoader<Comments> {
    private final String mLinkID;

    public CommentsLoader(Context context, String linkID) {
        super(context);
        this.mLinkID = linkID;
    }

    @Override
    public Comments loadInBackground() {
        try {
            return Comments.get(mLinkID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void postProcessing(Comments oldData, Comments newData) {
        // Do nothing
    }
}
