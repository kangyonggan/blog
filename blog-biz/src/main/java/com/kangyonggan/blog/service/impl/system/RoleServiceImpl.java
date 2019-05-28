package com.kangyonggan.blog.service.impl.system;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.dto.RoleRequest;
import com.kangyonggan.blog.mapper.RoleMapper;
import com.kangyonggan.blog.model.Role;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.system.MenuService;
import com.kangyonggan.blog.service.system.RoleService;
import com.kangyonggan.blog.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 12/6/18
 */
@Service
public class RoleServiceImpl extends BaseService<Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuService menuService;

    @Override
    public boolean hasRoles(Long userId, String... roleCodes) {
        return roleMapper.selectExistsUserRoleCode(userId, roleCodes);
    }

    @Override
    public void deleteAllRolesByUserId(Long userId) {
        roleMapper.deleteAllRolesByUserId(userId);
    }

    @Override
    public List<Role> findAllRoles() {
        Role role = new Role();
        role.setIsDeleted(YesNo.NO.getCode());

        return myMapper.select(role);
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    @MethodLog
    public void saveRole(Role role) {
        myMapper.insertSelective(role);
    }

    @Override
    @MethodLog
    public void updateRole(Role role) {
        myMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenu(Role role, String[] menuIds) {
        myMapper.updateByPrimaryKeySelective(role);

        updateRoleMenus(role.getRoleId(), menuIds);
    }


    private void updateRoleMenus(Long roleId, String[] menuIds) {
        menuService.deleteAllMenusByRoleId(roleId);
        if (menuIds != null && menuIds.length > 0) {
            roleMapper.insertRoleMenus(roleId, menuIds);
        }
    }

    @Override
    public boolean existsRoleCode(String roleCode) {
        Role role = new Role();
        role.setRoleCode(roleCode);
        return super.exists(role);
    }

    @Override
    @MethodLog
    public List<Role> searchRoles(RoleRequest roleRequest) {
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();

        String roleCode = roleRequest.getRoleCode();
        if (StringUtils.isNotEmpty(roleCode)) {
            criteria.andLike("roleCode", StringUtil.toLike(roleCode));
        }

        String roleName = roleRequest.getRoleName();
        if (StringUtils.isNotEmpty(roleName)) {
            criteria.andLike("roleName", StringUtil.toLike(roleName));
        }

        String[] createdTime = roleRequest.getCreatedTime();
        if (createdTime != null && StringUtils.isNotEmpty(createdTime[0])) {
            criteria.andGreaterThanOrEqualTo("createdTime", createdTime[0]);
            criteria.andLessThanOrEqualTo("createdTime", createdTime[1]);
        }

        if (StringUtils.isNotEmpty(roleRequest.getSort())) {
            if (roleRequest.getOrder() == 0) {
                example.orderBy(roleRequest.getSort()).asc();
            } else {
                example.orderBy(roleRequest.getSort()).desc();
            }
        } else {
            example.orderBy("roleId").desc();
        }

        PageHelper.startPage(roleRequest.getPageNum(), roleRequest.getPageSize());
        return myMapper.selectByExample(example);
    }

}
