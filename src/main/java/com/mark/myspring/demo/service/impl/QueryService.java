package com.mark.myspring.demo.service.impl;

import com.mark.myspring.demo.service.IQueryService;
import com.mark.myspring.framework.annotation.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: 帅气的Mark
 * @Description: Mark行行好，给点注释吧！
 * @Date: Create in 2018/4/23 14:44
 * @QQ: 85104982
 */
@Service
public class QueryService implements IQueryService {
    /**
     * 查询
     */
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        return json;
    }

}