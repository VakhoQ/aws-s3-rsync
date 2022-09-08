package com.vakhtang.impl;

import com.vakhtang.SyncNode;
import com.vakhtang.ConfigSyncWorker;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemSyncWorkerImpl implements ConfigSyncWorker {

    @Override
    public List<SyncNode> getObjectTree(String rootLocation, String prefix)   {

        String path = rootLocation+"/"+prefix;

        URL url = this.getClass().getClassLoader().getResource(path);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<SyncNode> list = new ArrayList<>();
        walkRecursion(file, prefix, list);

        return list;
    }

    @Override
    public boolean updateConfig(String rootLocation, String prefix, String key) {
       throw new RuntimeException("not implemented yet");
    }

    private void walkRecursion(File file, String parent, List<SyncNode> list) {
        File [] objects = file.listFiles();

        for (File f : objects) {
            if(!f.isDirectory()){
                String key = parent + "/"+f.getName();
                String hashCode = getHashCode(f);
                SyncNode node = new SyncNode(key, hashCode);
                list.add(node);
            }else{
                walkRecursion(f,parent + "/" +f.getName(), list);
            }

        }
    }

    public static String getHashCode(File file) {

        byte[] b = new byte[0];
        try {
            b = Files.readAllBytes(Paths.get(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("can't read file byte array", e);
        }
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(b);
            return byteArrayToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }




}
