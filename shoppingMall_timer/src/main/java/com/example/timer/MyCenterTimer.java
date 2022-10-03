package com.example.timer;

import com.example.service.RedisService;
import com.example.util.ConstantUtil;
import com.example.util.FastDFSUtil;
import com.example.util.FileUtil;
import com.example.util.QiniuyunFileUploadUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @类名 DeleteInfectiveTimer
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/4 22:41
 * @版本 1.0
 */
@Component
public class MyCenterTimer {

    @DubboReference(interfaceClass = RedisService.class, version = "1.0.0")
    private RedisService redisService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteIneffectiveProfileTimer() {
        Set<Object> set = redisService.sdiff(ConstantUtil.UPLOAD_IMAGE_NAME_SET, ConstantUtil.SAVE_IMAGE_NAME_SET);
        for (Object object : set) {

//            QiniuyunFileUploadUtil.deleteFile((String) object);

            String fileName = (String) object;
            FastDFSUtil.delete(FileUtil.getGroup(fileName), FileUtil.getResourceName(fileName));
            redisService.remove(ConstantUtil.UPLOAD_IMAGE_NAME_SET, object);
        }
    }
}
