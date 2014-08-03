package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

//@JsonDeserialize(using = ReplyDeserializer.class)
@JsonSerialize
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "kind"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Comment.class, name = "t1"),
        @JsonSubTypes.Type(value = MoreChildren.class, name = "more")
})
/** A reply can either be another comment or a "morecomment" item */
public abstract class Reply extends Thing {
}