package edu.hit.lvyoubackend.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import edu.hit.lvyoubackend.entity.User;
import edu.hit.lvyoubackend.service.UserService;
import edu.hit.lvyoubackend.utils.AliOSSUtils;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private UserService userService;

    /**
    * multiplefile需指明名称，前端名称默认为file，所以需要Requestparam 不加的话只能找名为image的文件
     */

    @PostMapping("/api/uploadAvatar")
    public Response uploadAvatar(@RequestParam(value ="file") MultipartFile image, @RequestParam Integer uid) throws IOException {

        //TODO

        String url = aliOSSUtils.upload(image);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid",uid).set("avatarSrc",url);
        userService.update(null,updateWrapper);

        return new Response(StatusCode.OK.set("000","上传成功"),url);
    }

    @PostMapping("/api/uploadCover")
    public Response uploadCover(@RequestParam(value ="file") MultipartFile image, @RequestParam Integer uid) throws IOException {

        //TODO

        String url = aliOSSUtils.upload(image);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid",uid).set("coverSrc",url);
        userService.update(null,updateWrapper);

        return new Response(StatusCode.OK.set("000","上传成功"),url);
    }

    @PostMapping("/api/uploadScheduleImage")
    public Response uploadScheduleImage(@RequestParam(value ="file") MultipartFile[] images) throws IOException {
        String url = "";
        int i = 1 ;
        HashMap<String, String> map = new HashMap<>();
        log.info("{}张图片",images.length);
        for (MultipartFile image : images){

            url = aliOSSUtils.upload(image);
            map.put("image" + i,url);
            i++;
        }

        return new Response(StatusCode.OK.set("000","上传成功"),map);
    }

}
