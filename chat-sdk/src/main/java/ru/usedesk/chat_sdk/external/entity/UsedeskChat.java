package ru.usedesk.chat_sdk.external.entity;

public class UsedeskChat {

    private String id;
    private String token;
    private String socketId;
    private String ticket;
    private UsedeskClient client;
    private long connectedAt;
    private long disconnectedAt;

    public UsedeskChat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public long getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(long connectedAt) {
        this.connectedAt = connectedAt;
    }

    public long getDisconnectedAt() {
        return disconnectedAt;
    }

    public void setDisconnectedAt(long disconnectedAt) {
        this.disconnectedAt = disconnectedAt;
    }

    public UsedeskClient getClient() {
        return client;
    }

    public void setClient(UsedeskClient client) {
        this.client = client;
    }
}