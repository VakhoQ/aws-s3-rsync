package com.vakhtang.impl

import com.vakhtang.ConfigSyncNode
import com.vakhtang.ConfigSyncWorker
import spock.lang.Specification

class S3SyncWorkerImplSpec extends Specification {


    def "s3 sync can execute without errors"() {

        when:
        ConfigSyncWorker worker = Mock(ConfigSyncWorker)
        worker.getObjectTree(_, _) >> new ArrayList<>();
        worker.getObjectTree("bucket-test-vk-01", "config");

        then:
        noExceptionThrown()
        0 * worker.getUpdatedObjectList(_, _)

    }


    def "find local path correctly"() {

        when:
        ConfigSyncWorker worker = new S3SyncWorkerImpl();
        String result = worker.getLocalFullPath(s3RootPrefix, localConfigRootPath, s3Key)

        then:
        result == output

        where:
        s3RootPrefix    | localConfigRootPath             | s3Key                          | output
        "app/vk/config" | "/Users/vk/aws-s3-rsync/config" | "app/vk/config/A/B/4.json"     | "/Users/vk/aws-s3-rsync/config/A/B/4.json"
        "app/vk/config" | "/Users/vk/aws-s3-rsync/config" | "app/vk/config/A/1.json"       | "/Users/vk/aws-s3-rsync/config/A/1.json"
        "app/vk/config" | "/linux/tmp"                    | "app/vk/config/c.json"         | "/linux/tmp/c.json"
        "app/vk/config" | "/linux/tmp"                    | "app/vk/config/A/B/C/D/1.json" | "/linux/tmp/A/B/C/D/1.json"
        "app/vk/config" | "/linux/tmp/vk/config"          | "app/vk/config/A/B/C/D/1.json" | "/linux/tmp/vk/config/A/B/C/D/1.json"
    }


}
