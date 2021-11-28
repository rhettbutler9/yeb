package com.xxxx.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.service.IMenuService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色信息")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role){
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if(roleService.save(role)){
            return  RespBean.success("角色信息添加成功");
        }
        return  RespBean.error("角色信息添加失败");
    }

    @ApiOperation(value = "删除角色信息")
    @DeleteMapping("/role/{rid}")
    public  RespBean removeRole(@PathVariable Integer rid){
        if(roleService.removeById(rid)){
            return  RespBean.success("角色信息删除成功");
        }
        return  RespBean.error("角色信息删除失败");
    }

    @ApiOperation(value = "批量删除角色信息")
    @DeleteMapping("/")
    public RespBean removeRole(Integer []ids){
        if(roleService.removeByIds(Arrays.asList(ids))){
            return  RespBean.success("批量删除角色信息成功");
        }
        return  RespBean.error("批量删除角色信息失败");
    }


    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public  List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色id查询所有菜单")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid)).stream().map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid,Integer [] mids){
        return  menuRoleService.updateMenuRole(rid,mids);

    }
}
