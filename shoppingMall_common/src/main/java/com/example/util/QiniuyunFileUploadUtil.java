package com.example.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * @类名 QiniuyunFileUploadUtil
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/4 13:53
 * @版本 1.0
 */
public class QiniuyunFileUploadUtil {

    public static String uploadFile(byte[] localFilePath, String fileName) {
        //指定文件要上传的个人仓库地区
        Configuration cfg = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(cfg);
        //鉴权
        Auth auth = Auth.create(ConstantUtil.ACCESS_KEY, ConstantUtil.SECRETE_KEY);
        //返回鉴权后的通行证
        String upToken = auth.uploadToken(ConstantUtil.BUCKET);
        try {
            //上传文件
            Response response = uploadManager.put(localFilePath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            //上传失败打印异常信息
            ex.printStackTrace();
        }
        return null;
    }

    public static void deleteFile(String fileName) {
        //指定要删除的文件所在的个人仓库地区
        Configuration cfg = new Configuration(Region.huanan());
        //鉴权
        Auth auth = Auth.create(ConstantUtil.ACCESS_KEY, ConstantUtil.SECRETE_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //删除文件
            bucketManager.delete(ConstantUtil.BUCKET, fileName);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            ex.printStackTrace();
        }
    }
}
