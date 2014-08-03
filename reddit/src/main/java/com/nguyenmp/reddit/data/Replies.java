package com.nguyenmp.reddit.data;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonDeserialize(using = RepliesDeserializer.class)
@JsonSerialize
public class Replies extends Listing<Reply> {
}
