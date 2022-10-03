package com.example.util;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @类名 fastDFSUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/7/14 21:46
 * @版本 1.0
 */
public class FastDFSUtil {

    @PostConstruct
    public static StorageClient init() throws MyException, IOException {
        ClientGlobal.init("fastDFS.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        return new StorageClient(trackerServer, storageServer);
    }

    public static String[] upload(byte[] file_buff, String file_ext_name) {
        String[] resArr = null;
        try {
            resArr = init().upload_file(file_buff, file_ext_name, null);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return resArr;
    }

    public static byte[] download(String groupName, String remoteFilePath) {
        try {
            return init().download_file(groupName, remoteFilePath);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int delete(String groupName, String remoteFileName) {
        try {
            return init().delete_file(groupName, remoteFileName);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return -1;
    }
}