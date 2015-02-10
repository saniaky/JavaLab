package chat;

public class ChatMessage {
    private String message;

    private ChatUser author;
    private ChatUser recipient;

    // Временная метка сообщения (в микросекундах)
    private long timestamp;

    public ChatMessage(String message, ChatUser author, long timestamp) {
        super();
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatUser getAuthor() {
        return author;
    }

    public void setAuthor(ChatUser author) {
        this.author = author;
    }

    public ChatUser getRecipient() {
        return recipient;
    }

    public void setRecipient(ChatUser recipient) {
        this.recipient = recipient;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}