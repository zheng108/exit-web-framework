package org.exitsoft.showcase.vcsadmin.dao.account;

import org.exitsoft.orm.core.hibernate.HibernateSuperDao;
import org.exitsoft.showcase.vcsadmin.entity.account.User;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问
 * @author vincent
 *
 */
@Repository
public class UserDao extends HibernateSuperDao<User, String>{

	/**
	 * 通过用户id更新用户密码
	 * 
	 * @param userId 用户id
	 * @param password 密码
	 */
	public void updatePassword(String userId, String password) {
		executeUpdateByQueryNamedUseJapStyle(User.UpdatePassword, password,userId);
	}

	
}
