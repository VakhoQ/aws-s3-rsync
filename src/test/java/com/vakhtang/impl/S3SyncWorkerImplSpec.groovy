package com.vakhtang.impl

import com.vakhtang.ConfigSyncWorker
import com.vakhtang.SyncNode
import spock.lang.Specification

class S3SyncWorkerImplSpec extends Specification{


    def "s3 sync can execute without errors"() {

        when:
        ConfigSyncWorker worker = Mock(ConfigSyncWorker)
        worker.getObjectTree(_,_) >> new ArrayList<>();
        worker.getObjectTree("bucket-test-vk-01", "config");

        then:
        noExceptionThrown()
        0 * worker.getUpdatedObjectList(_,_)


    }
}
