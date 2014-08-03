package com.nguyenmp.reddit.data;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;

public class CommentsDeserializer extends JsonDeserializer<Comments> {
    @Override
    public Comments deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Comments comments = new Comments();
        JsonNode rootNode = jp.readValueAsTree();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        // Deserialize listing of links
        JavaType parentType = mapper.getTypeFactory().constructParametricType(Listing.class, Link.class);
        comments.setParent(mapper.<Listing<Link>>readValue(rootNode.get(0), parentType));

        // Deserialize listing of comments
        JsonNode threadsNode = rootNode.get(1);
        JavaType threadsType = mapper.getTypeFactory().constructParametricType(Listing.class, Reply.class);
        comments.setThreads(mapper.<Listing<Reply>>readValue(threadsNode, threadsType));
        return comments;
    }
}
