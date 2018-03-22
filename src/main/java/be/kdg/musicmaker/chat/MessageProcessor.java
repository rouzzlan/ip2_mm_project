package be.kdg.musicmaker.chat;

public class MessageProcessor {

    public static ServerMessage build(ChatMessage message) {
        if (message.getContent() != null && !message.getContent().isEmpty()) {
            return new ServerMessage("[" + message.getName() + "]: " + message.getContent());
        }
        return new ServerMessage("[server]: Welcome " +message.getName());
    }
}
