package com.nguyenmp.reddit.data;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;

public class RepliesDeserializer extends JsonDeserializer<Listing<Reply>> {
    @Override
    public Listing<Reply> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();
        if (node.isTextual()) {
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructParametricType(Listing.class, Reply.class);
            return mapper.readValue(node, type);
        }
    }
}
