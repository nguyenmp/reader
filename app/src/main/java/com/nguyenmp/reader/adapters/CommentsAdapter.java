package com.nguyenmp.reader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nguyenmp.reader.R;
import com.nguyenmp.reddit.data.Comment;
import com.nguyenmp.reddit.data.Comments;
import com.nguyenmp.reddit.data.Link;
import com.nguyenmp.reddit.data.Listing;
import com.nguyenmp.reddit.data.MoreChildren;
import com.nguyenmp.reddit.data.Reply;
import com.nguyenmp.reddit.data.Thing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommentsAdapter extends BaseAdapter {
    private static final int ITEM_TYPE_LINK = 0, ITEM_TYPE_COMMENT = 1, ITEM_TYPE_MORE = 2;

    private final Context context;
    private Comments comments;
    private List<FlatReply> flatComments;

    public static interface Callback {
        public void loadMore();
    }

    public CommentsAdapter(Context context, Comments comments) {
        this.context = context;
        this.comments = comments;
        this.flatComments = flatten(Arrays.asList(comments.getThreads().getData().getChildren()));
    }

    @Override
    public int getCount() {
        return flatComments.size() + 1;
    }

    @Override
    public Thing getItem(int position) {
        if (position == 0) return comments.getParent().getData().getChildren()[0];
        else return flatComments.get(position - 1).reply;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return ITEM_TYPE_LINK;
        else if (getItem(position) instanceof Comment) return ITEM_TYPE_COMMENT;
        else return ITEM_TYPE_MORE;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Delegate the work of building the view to the specific functions
        // getLinkView, getCommentView, getMoreView

        switch(getItemViewType(position)) {
            case ITEM_TYPE_COMMENT:
                return getCommentView(position, view, parent);
            case ITEM_TYPE_LINK:
                return getLinkView(position, view, parent);
            case ITEM_TYPE_MORE:
                return getMoreView(position, view, parent);
        }

        return null;
    }

    private View getLinkView(int position, View view, ViewGroup parent) {
        Link link = comments.getParent().getData().getChildren()[0];
        return LinksAdapter.renderLink(context, view, parent, link);
    }

    private View getMoreView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_item_more_comments, parent, false);
        }

        FlatReply reply = flatComments.get(position - 1);
        view.setPadding(16 * reply.indent, 0, 0, 0);

        return view;
    }

    private View getCommentView(int position, View view, ViewGroup parent) {
        Comment comment = (Comment) getItem(position);

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_item_comment, parent, false);
        }

        FlatReply reply = flatComments.get(position - 1);
        view.setPadding(16 * reply.indent, 0, 0, 0);

        TextView bodyView = (TextView) view.findViewById(R.id.comment_body);
        String body = comment.getData().getBody();
        if (body == null) body = "";
        bodyView.setText(body);

        return view;
    }

    private static List<FlatReply> flatten(List<Reply> replies) {
        return flatten(replies, 0);
    }

    private static List<FlatReply> flatten(List<Reply> replies, int indent) {
        List<FlatReply> result = new ArrayList<FlatReply>();
        for (Reply reply : replies) result.addAll(flatten(reply, indent));
        return result;
    }

    private static List<FlatReply> flatten(Reply reply, int indent) {
        List<FlatReply> result = new ArrayList<FlatReply>();
        result.add(new FlatReply(reply, indent));

        if (reply instanceof Comment) {
            Comment comment = (Comment) reply;
            Listing<Reply> replyListing = comment.getData().getReplies();
            if (replyListing != null) {
                List<Reply> replies = Arrays.asList(replyListing.getData().getChildren());
                result.addAll(flatten(replies, indent + 1));
            }
        }

        return result;
    }

    private static class FlatReply {
        public final Reply reply;
        public final int indent;

        private FlatReply(Reply reply, int indent) {
            this.reply = reply;
            this.indent = indent;
        }
    }
}