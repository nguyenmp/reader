package com.nguyenmp.reader.frontpage;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nguyenmp.reader.R;
import com.nguyenmp.reader.util.LoaderOfMore;
import com.nguyenmp.reddit.data.Link;

public class ImmutableLinksAdapter extends BaseAdapter {
    public static final int LOAD_MORE_THRESHOLD = 5;  // Load more when we are 5 items from the bottom

    private static final int TYPE_LINK = 0, TYPE_SELF_POST = 1;

    private final Link[] data;
    private final Context context;
    private final LoaderOfMore callback;

    public static interface Callback {
        public void loadMore();
    }

    public ImmutableLinksAdapter(Link[] data, Context context, LoaderOfMore callback) {
        this.data = data;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Link getItem(int position) {
        return data[position];
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getData().isIs_self() ? TYPE_SELF_POST : TYPE_LINK;
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(getItem(position).getData().getId(), 36);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Load more if we're showing the last view
        int lastItemIndex = data.length - 1;
        if (position >= (lastItemIndex) - LOAD_MORE_THRESHOLD && callback != null) callback.loadMore();

        Link link = getItem(position);

        return renderLink(context, view, parent, link);
    }

    public static View renderLink(Context context, View view, ViewGroup parent, Link link) {

        // Inflate a new view if we cannot recycle an old one
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            int layout = link.getData().isIs_self() ? R.layout.list_item_self_text : R.layout.list_item_link;
            view = layoutInflater.inflate(layout, parent, false);
        }

        TextView title = (TextView) view.findViewById(R.id.list_item_link_title);
        title.setText(link.getData().getTitle());

        TextView subtitle = (TextView) view.findViewById(R.id.list_item_link_subtitle);
        subtitle.setText(link.getData().getSubreddit());

        if (link.getData().isIs_self()) {
//            TextView body = (TextView) view.findViewById(R.id.self_post_content);
//            String html = link.getData().getSelftext_html();
////            html = StringEscapeUtils.unescapeHtml4(html);
//            html = html == null ? "" : html;
//            html = html.replace("<!-- SC_OFF -->", "");
//            html = html.replace("<!-- SC_ON -->", "");
//            body.setText(Html.fromHtml(html));
        } else {
            // Link is an external link

        }

        return view;
    }

}