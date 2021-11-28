package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.DepartmentMapper;
import com.xxxx.server.pojo.Department;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rhy
 * @since 2021-04-23
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private  DepartmentMapper departmentMapper;
    /**
     * 获取所有部门
     * @return
     */
    @Override
    public List<Department> getAllDepartment() {
        return  departmentMapper.getAllDepartment(-1);
    }

    @Override
    public RespBean addDep(Department department) {
        department.setEnabled(true);
        departmentMapper.addDep(department);
        if(1== department.getResult()){
            return  RespBean.success("添加成功!",department);
        }
        return  RespBean.error("添加失败!");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public RespBean deleteDep(Integer id) {
        Department department=new Department();
        department.setId(id);
        departmentMapper.deleteDep(department);
        if(-2==department.getResult()){
            return  RespBean.error("该部门下还有子部门,删除失败!");
        }
        if(-1==department.getResult()){
            return  RespBean.error("该部门下还有员工,删除失败!");
        }
        if(1==department.getResult()){
            return  RespBean.error("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
