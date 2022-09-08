package com.vakhtang;

public class SyncNode {

    private final String objectKey;
    private final String objectHash;


    public SyncNode(String objectKey, String objectHash) {
        this.objectKey = objectKey;
        this.objectHash = objectHash;
    }


    public String getObjectKey() {
        return objectKey;
    }

    public String getObjectHash() {
        return objectHash;
    }
}
