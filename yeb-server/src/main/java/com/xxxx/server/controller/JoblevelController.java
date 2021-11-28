package com.xxxx.server.controller;


import com.xxxx.server.pojo.Joblevel;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rhy
 * @since 2021-04-23
 */
@RestController
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value = "查询所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJoblevels(){
        return joblevelService.list();
    }

    @ApiOperation(value = "添加职称信息")
    @PostMapping("/")
    public RespBean  addJoblevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if(joblevelService.save(joblevel)){
            return  RespBean.success("职称信息添加成功");
        }
        return  RespBean.error("职称信息添加失败");
    }

    @ApiOperation(value = "更新职称信息")
    @PutMapping("/")
    public RespBean updateJoblevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if(joblevelService.updateById(joblevel)){
            return  RespBean.success("职称信息更新成功!");
        }
        return  RespBean.error("职称信息更新失败!");
    }

    @ApiOperation(value = "删除职称信息")
    @DeleteMapping("/{id}")
    public  RespBean removeJoblevelByid(@PathVariable Integer id ){
        if(joblevelService.removeById(id)){
            return  RespBean.success("职称信息删除成功");
        }
        return  RespBean.error("职称信息删除失败");
    }

    @ApiOperation(value = "批量删除职称信息")
    @DeleteMapping("/")
    public  RespBean removeJoblevelsByIds(Integer []ids){
        if(joblevelService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("职称信息删除成功!");
        }
        return  RespBean.error("职称信息删除失败!");
    }

}
