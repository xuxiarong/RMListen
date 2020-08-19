package com.rm.business_lib.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.rm.business_lib.db.TestUserDao;

import com.rm.business_lib.db.TestUserDaoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig testUserDaoDaoConfig;

    private final TestUserDaoDao testUserDaoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        testUserDaoDaoConfig = daoConfigMap.get(TestUserDaoDao.class).clone();
        testUserDaoDaoConfig.initIdentityScope(type);

        testUserDaoDao = new TestUserDaoDao(testUserDaoDaoConfig, this);

        registerDao(TestUserDao.class, testUserDaoDao);
    }
    
    public void clear() {
        testUserDaoDaoConfig.clearIdentityScope();
    }

    public TestUserDaoDao getTestUserDaoDao() {
        return testUserDaoDao;
    }

}
