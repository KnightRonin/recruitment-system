package com.yisen.hbaseboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.yisen.hbaseboot.entity.RowData;
import com.yisen.hbaseboot.service.PostService;
import com.yisen.hbaseboot.util.ResultData;
import com.yisen.hbaseboot.util.exception.DaoException;
import com.yisen.hbaseboot.util.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * @AuthorList: LiuYiSen
 * @Date: 2020/7/5 14:47
 */
@RestController
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping("/PosDetail")
    public ResultData postSingleRecord(@RequestParam Map<String, String> map) throws DaoException, ServiceException, IOException {
        ResultData resultData = null;
        postService.postSingleRecord(map);
        resultData = new ResultData(true, "存储成功", 200, null);
        return resultData;
    }

    @GetMapping("/importData")
    public ResultData importData() throws IOException, DaoException {
        ResultData resultData = null;
        postService.importData();
        resultData = new ResultData(true, "存储成功", 200, null);
        return resultData;
    }
}
