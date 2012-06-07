package org.exitsoft.project.vcsadmin.dao.account;

import java.util.List;

import org.exitsoft.orm.core.hibernate.HibernateSuperDao;
import org.exitsoft.project.vcsadmin.entity.account.Resource;
import org.exitsoft.project.vcsadmin.entity.account.User;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问
 * @author vincent
 *
 */
@Repository
public class UserDao extends HibernateSuperDao<User, String>{
	
	/***
	 * 获取用户所有资源 HQL
	 */
	private final String Query_User_Resource = "select resource from User u left join u.rolesList role left join role.resourcesList resource where u.id=?";

	@SuppressWarnings("unchecked")
	public List<Resource> getUserResource(String id) {
		return createQuery(Query_User_Resource, id).list();
	}

	
}
