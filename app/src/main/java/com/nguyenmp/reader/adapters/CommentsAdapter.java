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
        return position == 0 ? 0 : 1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Inflate a new view if we cannot recycle an old one
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.list_item_link, parent, false);
        }

        TextView title = (TextView) view.findViewById(R.id.list_item_link_title);

        if (position == 0) {
            Link link = comments.getParent().getData().getChildren()[0];
            title.setText(link.getData().getTitle());
        } else {
            FlatReply flatReply = flatComments.get(position - 1);
            view.setPadding(16 * flatReply.indent, 0, 0, 0);
            Reply reply = flatReply.reply;
            if (reply instanceof MoreChildren) {
                title.setText("Load More Children");
            } else {
                Comment comment = (Comment) reply;
                String body = comment.getData().getBody();
                title.setText(body);
            }
        }

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