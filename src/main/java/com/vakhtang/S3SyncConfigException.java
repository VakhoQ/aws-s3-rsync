package com.vakhtang;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class S3SyncConfigException extends Exception{

    public S3SyncConfigException() {
        super();
    }


    public S3SyncConfigException(String msg) {
        super(msg);
    }

    public S3SyncConfigException(String message, IOException error) {
        super(message,error);
    }
}
