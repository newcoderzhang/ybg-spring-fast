package com.ybg.rbac.resources.service;
import java.util.List;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.util.Page;
import com.ybg.rbac.resources.domain.SysButtonVO;
import com.ybg.rbac.resources.domain.SysColorVO;
import com.ybg.rbac.resources.domain.SysMenuIconVO;
import com.ybg.rbac.resources.domain.SysResourcesVO;
import com.ybg.rbac.resources.qvo.ResourcesQvo;
import com.ybg.rbac.resources.qvo.SysButtonQvo;
import com.ybg.rbac.resources.qvo.SysColorQvo;
import com.ybg.rbac.resources.qvo.SysMenuIconQvo;

public interface ResourcesService {
	
	/** 返回主键的创建 **/
	// sys_resources
	SysResourcesVO create(SysResourcesVO bean);
	
	/** 更新数据，条件 和 需要更新的字段都不能为空 不限个数个条件
	 * 
	 * @author Deament
	 * @param updatemap
	 *            需要更新的字段和值
	 * @param wheremap
	 *            更新中的条件字段和值
	 * @param table_name
	 *            表的名称 **/
	// sys_resources
	void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> wheremap);
	
	/** 分页查询 **/
	// sys_resources
	Page query(Page page, ResourcesQvo qvo);
	
	/** 不分页查询 **/
	// sys_resources
	List<SysResourcesVO> query(ResourcesQvo qvo);
	
	/** 角色 权限集合 **/
	// sys_res_role
	// sys_resources
	List<SysResourcesVO> getRolesByUserId(String roleid);
	
	/** 授权的按钮操作 **/
	// sys_res_role
	// sys_resources
	List<SysResourcesVO> getOperatorButton(String roleid, String parentid);
	
	/** 授权按钮组 **/
	// sys_button
	List<SysButtonVO> querybutton(SysButtonQvo qvo);
	
	/** 获取菜单样式列表 **/
	List<SysMenuIconVO> queryicon(SysMenuIconQvo qvo);
	
	/** 获取颜色列表 **/
	List<SysColorVO> querycolor(SysColorQvo qvo);
	
	void removebyid(String id);
	
	SysResourcesVO get(String id);
}
