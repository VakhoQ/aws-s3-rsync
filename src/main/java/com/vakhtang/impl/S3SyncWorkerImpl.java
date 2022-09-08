package com.vakhtang.impl;

import com.vakhtang.SyncNode;
import com.vakhtang.ConfigSyncWorker;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.*;

import java.util.List;

public class S3SyncWorkerImpl implements ConfigSyncWorker {
    @Override
    public List<SyncNode> getObjectTree(String s3Bucket, String prefix) {


        List<SyncNode> nodes = new ArrayList<>();

        S3Client client = S3Client.builder().region(Region.US_EAST_1).build();
        ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(s3Bucket).prefix(prefix).build();
        ListObjectsV2Response response = client.listObjectsV2(request);


            List<S3Object> objects = response.contents();

            for (S3Object object : objects) {
                if(object.size()==0){
                    continue;
                }
                SyncNode node = new SyncNode(object.key(), object.eTag());
                nodes.add(node);
            }

        return nodes;
    }

    @Override
    public boolean updateConfig(String rootLocation, String prefix, String key) {
       throw new RuntimeException("Not Implemented Yet");
    }
}
