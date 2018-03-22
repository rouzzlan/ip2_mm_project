package be.kdg.musicmaker.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "content"})
public class ChatMessage {

    @JsonProperty("content")
    private String content;

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

}
