package com.rm.business_lib.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @ClassName: TestUserDao
 * @Description:
 * @Author: 鲸鱼
 * @CreateDate: 8/14/20 6:45 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/14/20 6:45 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
@Entity
public class TestUserDao {
    @Id
    private  int id;

    @Generated(hash = 1891090122)
    public TestUserDao(int id) {
        this.id = id;
    }

    @Generated(hash = 809397229)
    public TestUserDao() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
