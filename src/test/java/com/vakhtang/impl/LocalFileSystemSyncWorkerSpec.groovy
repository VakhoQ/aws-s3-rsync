package com.vakhtang.impl

import com.vakhtang.ConfigSyncNode
import com.vakhtang.ConfigSyncWorker
import spock.lang.Specification

class LocalFileSystemSyncWorkerSpec extends Specification {

    def "should detect content modification"() {

        given: 'store object key and hash code in the memory'
        ConfigSyncWorker worker = new LocalFileSystemSyncWorkerImpl();
        List<ConfigSyncNode> nodesInMemory = worker.getObjectTree("s3-sync-local-before-update", "root-config");

        and: 'after content of the file is updated'
        List<ConfigSyncNode> nodesAfterUpdate = worker.getObjectTree("s3-sync-local-after-update", "root-config");


        when: 'compare lists'
        List<String> list = worker.getUpdatedObjectList(nodesInMemory, nodesAfterUpdate);

        then:
        list.size() ==2
        list.get(0) == "root-config/A/3.json"
        list.get(1) == "root-config/2.json"

    }

}
