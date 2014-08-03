package com.nguyenmp.reddit.data;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

public class ReplyDeserializer extends JsonDeserializer<Reply> {
    @Override
    public Reply deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();
        String kind = node.get("kind").asText();
        if (kind.equals("more")) {
            return jp.readValueAs(MoreChildren.class);
        } else if (kind.equals("t1")) {
            return jp.readValueAs(Comment.class);
        } else {
            throw new IOException("Reply kind of unknown type: " + kind);
        }
    }
}
