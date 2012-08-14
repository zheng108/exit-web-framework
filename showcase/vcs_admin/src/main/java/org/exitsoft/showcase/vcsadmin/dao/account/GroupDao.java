package org.exitsoft.showcase.vcsadmin.dao.account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.exitsoft.orm.core.hibernate.HibernateSuperDao;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.vcsadmin.entity.account.Group;
import org.springframework.stereotype.Repository;

/**
 * 部门数据访问
 * 
 * @author vincent
 *
 */
@Repository
public class GroupDao extends HibernateSuperDao<Group, String>{

}
