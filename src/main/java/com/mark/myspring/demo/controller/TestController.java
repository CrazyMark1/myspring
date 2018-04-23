package com.mark.myspring.demo.controller;

import com.mark.myspring.demo.service.IQueryService;
import com.mark.myspring.framework.annotation.Autowired;
import com.mark.myspring.framework.annotation.Controller;
import com.mark.myspring.framework.annotation.RequestMapping;
import com.mark.myspring.framework.annotation.RequestParam;
import com.mark.myspring.framework.webmvc.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 帅气的Mark
 * @Description: Mark行行好，给点注释吧！
 * @Date: Create in 2018/4/23 14:44
 * @QQ: 85104982
 */
@Controller
public class TestController {
    @Autowired
    IQueryService queryService;
    public void query(String teacher){
        System.out.println(queryService.query(teacher));
    }

}
