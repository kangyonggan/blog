package com.kangyonggan.blog.service.impl.system;

import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.mapper.MenuMapper;
import com.kangyonggan.blog.model.Menu;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.system.MenuService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kangyonggan
 * @since 12/6/18
 */
@Service
public class MenuServiceImpl extends BaseService<Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public boolean hasMenus(Long userId, String[] menuCodes) {
        return menuMapper.selectExistsUserMenuCodes(userId, menuCodes);
    }

    @Override
    public List<Menu> findMenusByUserId(Long userId) {
        List<Menu> menus = menuMapper.selectMenusByUserId(userId);

        return recursionList(menus, new ArrayList<>(), StringUtils.EMPTY);
    }

    @Override
    public List<Menu> findAllMenus() {
        Example example = new Example(Menu.class);
        example.setOrderByClause("sort ASC");
        List<Menu> menus = menuMapper.selectByExample(example);

        return recursionList(menus, new ArrayList<>(), StringUtils.EMPTY);
    }

    @Override
    public List<Menu> findMenusByRoleId(Long roleId) {
        return menuMapper.selectMenusByRoleId(roleId);
    }

    @Override
    public void deleteAllMenusByRoleId(Long roleId) {
        menuMapper.deleteAllMenusByRoleId(roleId);
    }

    @Override
    @MethodLog
    public void deleteMenu(Long menuId) {
        myMapper.deleteByPrimaryKey(menuId);
    }

    @Override
    @MethodLog
    public void saveMenu(Menu menu) {
        myMapper.insertSelective(menu);
    }

    @Override
    @MethodLog
    public void updateMenu(Menu menu) {
        myMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public boolean existsMenuCode(String menuCode) {
        Menu menu = new Menu();
        menu.setMenuCode(menuCode);

        return super.exists(menu);
    }

    @Override
    @MethodLog
    public List<Menu> findRoleMenus(Long roleId) {
        return menuMapper.selectMenusByRoleId(roleId);
    }

    /**
     * 递归找出 parentCode 下边的所有子节点
     *
     * @param from
     * @param toList
     * @param parentCode
     * @return
     */
    private List<Menu> recursionList(List<Menu> from, List<Menu> toList, String parentCode) {
        if (CollectionUtils.isEmpty(from)) {
            return toList;
        }

        for (int i = 0; i < from.size(); i++) {
            Menu menu = from.get(i);
            if (parentCode.equals(menu.getParentCode())) {
                List<Menu> leaf = new ArrayList<>();
                menu.setChildren(leaf);
                toList.add(menu);
                recursionList(from, leaf, menu.getMenuCode());
            }
        }

        return toList;
    }

}
