package com.truedei.swagger.plugin.controller;

import com.truedei.swagger.plugin.annotation.APiFileInfo;
import com.truedei.swagger.plugin.annotation.Apicp;
import com.truedei.swagger.plugin.bean.ScanBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
@Api(value = "1.test接口",tags = "1.test接口设置",position = 1)
public class TestController {

    @PostMapping("/one")
    @ResponseBody
    @ApiOperation(value = "one", notes = "one",position = 1)
    @APiFileInfo("/test/one")
    public String testOne(@Apicp(
            modelName = "tetsOne",
            noValues = {"name","address"},
            noValueTypes = {"string","string"},
            noVlaueExplains = {"名字","地址"},
            noVlaueExample = {"郑晖"},
            noVlaueRequired = {true}
    )
                          @RequestBody String str){
        return "one";
    }

    @GetMapping("/tow")
    @ResponseBody
    @ApiOperation(value = "tow", notes = "tow",position = 2)
    public String testTow(){
        return "tow";
    }

    @PostMapping("/three")
    @ResponseBody
    @ApiOperation(value = "three", notes = "three",position = 3)
    public String testThree(@RequestBody ScanBean scanBean, @RequestBody ScanBean scanBean2){
        return "tow";
    }


}
