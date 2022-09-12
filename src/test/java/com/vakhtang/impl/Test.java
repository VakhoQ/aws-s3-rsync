package com.vakhtang.impl;

import com.vakhtang.ConfigSyncNode;
import com.vakhtang.ConfigSyncWorker;
import com.vakhtang.S3SyncConfigException;
import org.apache.commons.codec.binary.StringUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Test {

    public static void main(String[] args) throws S3SyncConfigException {




       String localPath = new S3SyncWorkerImpl().getLocalFullPath("app/vk/config", "/Users/vakhtang_koroghlishv/aws-s3-rsync/config", "app/vk/config/A/B/4.json" );
        System.out.println(localPath);

//
//        ConfigSyncWorker worker = new S3SyncWorkerImpl();
//        List<ConfigSyncNode> obj = worker.getObjectTree("bucket-test-vk-01", "app/vk/config");
//
//        for (ConfigSyncNode i : obj) {
//
//            System.out.println(i.getObjectKey());
//             String localPath = getLocalFullPath("app/vk/config", "/Users/vakhtang_koroghlishv/aws-s3-rsync/config/", "app/vk/config/A/B/4.json" );
//            //download("bucket-test-vk-01", i.getObjectKey(), "/Users/vakhtang_koroghlishv/aws-s3-rsync/test.json");
//            //break;
//        }

    }



    public static void download(String s3bucket, String key, String downloadLocation) throws S3SyncConfigException {

        S3Client client = S3Client.builder().region(Region.US_EAST_1).build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3bucket)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> in= client.getObject(getObjectRequest);

        try {
            Files.copy(in, Paths.get(downloadLocation), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new S3SyncConfigException("Error while downloading S3 object",e);
        }

    }
}
