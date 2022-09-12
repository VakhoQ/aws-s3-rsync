package com.vakhtang.impl;

import com.vakhtang.ConfigSyncNode;
import com.vakhtang.ConfigSyncWorker;
import com.vakhtang.S3SyncConfigException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.util.List;

public class S3SyncWorkerImpl implements ConfigSyncWorker {
    @Override
    public List<ConfigSyncNode> getObjectTree(String s3Bucket, String prefix) {

        List<ConfigSyncNode> nodes = new ArrayList<>();
        S3Client client = S3Client.builder().region(Region.US_EAST_1).build();
        ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(s3Bucket).prefix(prefix).build();
        ListObjectsV2Response response = client.listObjectsV2(request);
            List<S3Object> objects = response.contents();
            for (S3Object object : objects) {
                if(object.size()==0){
                    continue;
                }
                ConfigSyncNode node = new ConfigSyncNode(object.key(), object.eTag());
                nodes.add(node);
            }
        return nodes;
    }

    @Override
    public  void update(String s3bucket, String S3ObjectKey, String downloadLocation) throws S3SyncConfigException {

        S3Client client = S3Client.builder().region(Region.US_EAST_1).build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3bucket)
                .key(S3ObjectKey)
                .build();
        ResponseInputStream<GetObjectResponse> in= client.getObject(getObjectRequest);

        try {
            Files.copy(in, Paths.get(downloadLocation), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new S3SyncConfigException("Error while downloading S3 object",e);
        }

    }
}
