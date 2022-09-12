package com.vakhtang;

import java.util.ArrayList;
import java.util.List;

public interface ConfigSyncWorker {

    /**
     * Returns list of object tree. <br>
     * Child node is separated by Slash <br>
     * Example: <br>
     * &nbsp  root/A/1.json <br>
     * &nbsp  root/A/B/2.json <br>
     *
     * @return List of nodes that represent key (location) and hash code (based on content)
     */
    public List<ConfigSyncNode> getObjectTree(String rootLocation, String prefix);

    /**
     * Returns list of updated object.
     * Time complexity if this method is O(N^N) that
     *
     * @param syncNodesBefore node structure in memory
     * @param syncNodesAfter  node structure after rescan
     * @return list of object keys (location)
     */
    public default List<String> getUpdatedObjectList(List<ConfigSyncNode> syncNodesBefore, List<ConfigSyncNode> syncNodesAfter) {
        List<String> objectsNeedUpdate = new ArrayList<>();
        out:
        for (ConfigSyncNode i : syncNodesBefore) {
            for (ConfigSyncNode j : syncNodesAfter) {
                if (i.getObjectKey().equals(j.getObjectKey())) {
                    if (!i.getObjectHash().equals(j.getObjectHash())) {
                        objectsNeedUpdate.add(i.getObjectKey());
                        continue out;
                    }
                }
            }
        }
        return objectsNeedUpdate;
    }

    /**
     *
     * @param remoteRootPath - root location where we have configs in remote server.
     * @param localRootPath - root location where we have configs in the local server.
     * @param remoteFullPath - full path of remote file
     * @return local full path where config needs to be downloaded
     * @throws S3SyncConfigException if we try to update incorrect path
     */
    public default String getLocalFullPath(String remoteRootPath, String localRootPath, String remoteFullPath) throws S3SyncConfigException {
        Integer index = remoteFullPath.indexOf(remoteRootPath);
        if(index!=0){
            throw new S3SyncConfigException("Incorrect Path Detected");
        }
        String relativeRemote = remoteFullPath.substring(remoteRootPath.length());
        return localRootPath + relativeRemote;
    }


    /**
     *
     * @param remoteLocation - Remote location like S3 bucket name
     * @param remoteRelativePath - remote object path like S3 key
     * @param localDesFullPath - exact location where the file needs to be downloaded
     * @throws S3SyncConfigException
     */
    public void update(String remoteLocation, String remoteRelativePath, String localDesFullPath) throws S3SyncConfigException;
}
