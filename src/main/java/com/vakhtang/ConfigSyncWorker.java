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
    public List<SyncNode> getObjectTree(String rootLocation, String prefix);

    /**
     * Returns list of updated object. Time complexity if this method is O(N^N)
     *
     * @param syncNodesBefore node structure in memory
     * @param syncNodesAfter  node structure after rescan
     * @return list of object keys (location)
     */
    public default List<String> getUpdatedObjectList(List<SyncNode> syncNodesBefore, List<SyncNode> syncNodesAfter) {
        List<String> objectsNeedUpdate = new ArrayList<>();
        out:
        for (SyncNode i : syncNodesBefore) {
            for (SyncNode j : syncNodesAfter) {
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

    public boolean updateConfig(String rootLocation, String prefix, String key);
}
