package com.example.campusrent;

public class Request {
    private String itemId;
    private String ownerId;
    private String requesterId;
    private String status;

    public Request() {
        // Needed for Firestore
    }

    public Request(String itemId, String ownerId, String requesterId, String status) {
        this.itemId = itemId;
        this.ownerId = ownerId;
        this.requesterId = requesterId;
        this.status = status;
    }

    public String getItemId() {
        return itemId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getStatus() {
        return status;
    }
}