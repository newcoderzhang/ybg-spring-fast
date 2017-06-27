package com.ybg.rbac.resources.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.ybg.base.jdbc.BaseDao;
import com.ybg.base.jdbc.BaseMap;
import com.ybg.base.jdbc.util.QvoConditionUtil;
import com.ybg.base.util.Page;
import com.ybg.rbac.resources.domain.SysButtonVO;
import com.ybg.rbac.resources.domain.SysColorVO;
import com.ybg.rbac.resources.domain.SysMenuIconVO;
import com.ybg.rbac.resources.domain.SysResourcesVO;
import com.ybg.rbac.resources.mapper.ResourcesMapper;
import com.ybg.rbac.resources.qvo.ResourcesQvo;
import com.ybg.rbac.resources.qvo.SysButtonQvo;
import com.ybg.rbac.resources.qvo.SysColorQvo;
import com.ybg.rbac.resources.qvo.SysMenuIconQvo;

@Repository
public class ResourcesDaoImpl extends BaseDao implements ResourcesDao {
	
	private static String	QUERY_TABLE_NAME	= "sys_resources res";
	private static String	QUERY_TABLE_COLUMN	= " res.id,res.name,res.parentid,res.reskey,res.type,res.resurl,res.level,res.icon,res.ishide,res.description,res.colorid ";
	
	public SysResourcesVO create(final SysResourcesVO bean) {
		BaseMap<String, Object> createmap = new BaseMap<String, Object>();
		String id = null;
		createmap.put("name", bean.getName());
		createmap.put("parentid", bean.getParentid());
		createmap.put("reskey", bean.getReskey());
		createmap.put("type", bean.getType());
		createmap.put("resurl", bean.getResurl());
		createmap.put("level", bean.getLevel());
		createmap.put("icon", bean.getIcon());
		createmap.put("ishide", bean.getIshide());
		createmap.put("description", bean.getDescription());
		createmap.put("colorid", bean.getColorid());
		try {
			id = baseCreate(createmap, "sys_resources", "id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		bean.setId(id);
		return bean;
	}
	
	public void update(BaseMap<String, Object> updatemap, BaseMap<String, Object> WHEREmap) {
		this.baseupdate(updatemap, WHEREmap, "sys_resources");
	}
	
	public Page query(Page page, ResourcesQvo qvo) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(",color.colorclass").append(FROM).append(QUERY_TABLE_NAME).append(",sys_color color");
		sql.append(getcondition(qvo));
		page.setResult(getJdbcTemplate().query(page.getPagesql(sql), new ResourcesMapper()));
		page.setTotals(queryForInt(sql));
		return page;
	}
	
	private String getcondition(ResourcesQvo qvo) {
		StringBuilder sql = new StringBuilder();
		sql.append(WHERE).append("1=1");
		if (QvoConditionUtil.checkInteger(qvo.getIsdelete())) {
			sql.append(AND).append("res.isdelete=").append(qvo.getIsdelete());
		}
		else {
			sql.append(AND).append("res.isdelete=0");// 默认
		}
		sql.append(AND).append("res.colorid=color.id");
		sqlappen(sql, "res.colorid", qvo.getColorid());
		sqlappen(sql, "res.id", qvo.getId());
		sqlappen(sql, "res.level", qvo.getLevel());
		sqlappen(sql, "res.description", qvo.getDescription(), qvo);
		sqlappen(sql, "res.icon", qvo.getIcon(), qvo);
		sqlappen(sql, "res.ishide", qvo.getIshide(), qvo);
		sqlappen(sql, "res.name", qvo.getName(), qvo);
		sqlappen(sql, "res.reskey", qvo.getReskey(), qvo);
		sqlappen(sql, "res.resurl", qvo.getResurl(), qvo);
		sqlappen(sql, "res.type", qvo.getType());
		return sql.toString();
	}
	
	public List<SysResourcesVO> query(ResourcesQvo qvo) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(",color.colorclass").append(FROM).append(QUERY_TABLE_NAME).append(",sys_color color");
		sql.append(getcondition(qvo));
		return getJdbcTemplate().query(sql.toString(), new ResourcesMapper());
	}
	
	public List<SysResourcesVO> getRolesByUserId(String roleid) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(",sc.colorclass").append(FROM).append("sys_resources res,sys_res_role rr,sys_color sc").append(WHERE).append("res.id=rr.resId");
		sqlappen(sql, "rr.roleid", roleid);
		sql.append(AND).append("rr.state=0");
		sql.append(AND).append("res.isdelete=0");
		sql.append(AND).append("sc.id=res.colorid");
		return getJdbcTemplate().query(sql.toString(), new ResourcesMapper());
	}
	
	public List<SysResourcesVO> getOperatorButton(String roleid, String parentid) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(QUERY_TABLE_COLUMN).append(",color.colorclass").append(FROM).append(QUERY_TABLE_NAME).append(", sys_res_role rr  ").append(",sys_color color");
		sql.append(WHERE).append("res.id=rr.resId").append(AND).append("res.colorid=color.id").append(AND).append("rr.roleid=").append(roleid);
		sqlappen(sql, "rr.roleid", roleid);
		sqlappen(sql, "parentid", parentid);
		return getJdbcTemplate().query(sql.toString(), new ResourcesMapper());
	}
	
	public List<SysButtonVO> querybutton(SysButtonQvo qvo) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(" button.id,button.name,button.description,button.button ").append(FROM).append(" sys_button button");
		return getJdbcTemplate().query(sql.toString(), new RowMapper<SysButtonVO>() {
			
			public SysButtonVO mapRow(ResultSet rs, int index) throws SQLException {
				SysButtonVO bean = new SysButtonVO();
				bean.setButton(rs.getString("button"));
				bean.setDescription(rs.getString("description"));
				bean.setId(rs.getInt("id"));
				bean.setName("name");
				return bean;
			}
		});
	}
	
	private String getIconCondition(SysMenuIconQvo qvo) {
		StringBuilder sql = new StringBuilder();
		sql.append(WHERE).append("1=1");
		sqlappen(sql, "icon.id", qvo.getId());
		sqlappen(sql, "icon.name", qvo.getName(), qvo);
		sqlappen(sql, "icon.iconclass", qvo.getIconclass(), qvo);
		sqlappen(sql, "icon.type", qvo.getType(), qvo);
		return sql.toString();
	}
	
	public List<SysMenuIconVO> queryicon(SysMenuIconQvo qvo) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append("icon.id,icon.name,icon.iconclass,icon.type").append(FROM).append("sys_icon icon");
		sql.append(getIconCondition(qvo));
		return getJdbcTemplate().query(sql.toString(), new RowMapper<SysMenuIconVO>() {
			
			public SysMenuIconVO mapRow(ResultSet rs, int index) throws SQLException {
				SysMenuIconVO bean = new SysMenuIconVO();
				bean.setId(rs.getInt("id"));
				bean.setIconclass(rs.getString("iconclass"));
				bean.setName(rs.getString("name"));
				bean.setType(rs.getString("type"));
				return bean;
			}
		});
	}
	
	public List<SysColorVO> querycolor(SysColorQvo qvo) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append("id,colorclass,description").append(FROM).append("sys_color");
		return getJdbcTemplate().query(sql.toString(), new RowMapper<SysColorVO>() {
			
			public SysColorVO mapRow(ResultSet rs, int index) throws SQLException {
				SysColorVO bean = new SysColorVO();
				bean.setId(rs.getInt("id"));
				bean.setDescription(rs.getString("description"));
				bean.setColorclass(rs.getString("colorclass"));
				return bean;
			}
		});
	}
}
