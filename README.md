### J-hyberbin

* 超简洁的数据库操作工具。

* 支持mysql,oracle,sqlite,sqlserver等流行数据库，可扩展。

* 支持灵活开启事务。

* 支持sql参数预处理

* 支持原生的sql语法，无需培训轻松上手。

* 支持JPA annotation注解，无须繁琐的配置文件。

* 不依赖第三方插件
---
### POJO定义
```java
package org.jplus.model;

public class Servers {
    private Integer id;
    private String name;
    private String adds;
    private String note;
    //类型：1测试环境，2项目环境
    private Integer type;
    
    //setter and getter ...(在此省略，实际开发请补全)
}
```
---
### 测试类
```java
package org.jplus.hyb.database.crud;

import org.jplus.hyb.database.bean.FieldColumn;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.transaction.TxManager;
import org.jplus.hyb.database.util.Pager;
import org.jplus.hyb.log.LocalLogger;
import org.jplus.hyb.log.LoggerManager;
import org.jplus.model.Servers;
import org.jplus.util.ObjectHelper;
import org.junit.*;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 *
 * @author Hyberbin
 */
public class HyberbinTest {

    private static Connection CONNECTION;

    static {
        LocalLogger.setLevel(LocalLogger.DEBUG);
        LoggerManager.setLogFactory(LoggerFactory.class);
        SimpleConfigurator.addConfigurator(new DbConfig(DbConfig.DRIVER_SQLITE, "jdbc:sqlite:data.db", "", "", DbConfig.DEFAULT_CONFIG_NAME));
        ConfigCenter.INSTANCE.setManager(new TxManager(DbConfig.DEFAULT_CONFIG_NAME) {
            @Override
            public Connection getConnection() {
                if (CONNECTION == null) {
                    CONNECTION = super.getConnection();
                }
                connection = CONNECTION;
                return CONNECTION;
            }
        });
    }

    public HyberbinTest() {
    }

    public static Servers getDefault() {
        Servers servers = new Servers();
        servers.setId(0);
        servers.setName("Default");
        servers.setNote("server");
        servers.setType(-1);
        servers.setAdds("adds");
        return servers;
    }

    @BeforeClass
    public static void setUpClass() {
        DatabaseAccess lite = new DatabaseAccess(ConfigCenter.INSTANCE.getManager());
        try {
            Object count = lite.queryUnique("SELECT COUNT(*) FROM sqlite_master where type='table' and name='servers'");
            if (!Integer.valueOf(1).equals(count)) {
                String sql = "CREATE TABLE `servers` (\n"
                        + "  `Id` integer PRIMARY KEY autoincrement,\n"
                        + "  `name` varchar(255) DEFAULT NULL ,\n"
                        + "  `adds` varchar(255) DEFAULT NULL,\n"
                        + "  `note` varchar(255) DEFAULT NULL,\n"
                        + "  `type` int(11) DEFAULT NULL\n"
                        + ") ";
                new DatabaseAccess(ConfigCenter.INSTANCE.getManager()).update(sql);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        DatabaseAccess access = new DatabaseAccess(ConfigCenter.INSTANCE.getManager());
        try {
            int update = access.update("delete from servers");
            System.out.println("删除了" + update + "条数据");
        } catch (SQLException e) {
            System.out.println("初始化错误");
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            ConfigCenter.INSTANCE.getManager().finalCloseConnection();
        } catch (SQLException e) {
            System.out.println("初始化错误");
        }
    }

    @Before
    public void setUp() {
        for (int i = 0; i < 5; i++) {
            try {
                Servers servers = new Servers();
                servers.setId(i);
                servers.setName("server" + i);
                servers.setNote("server" + i);
                servers.setType(i);
                servers.setAdds("adds" + i);
                Hyberbin hyberbin = new Hyberbin(servers);
                hyberbin.insert("");
            } catch (SQLException ex) {
                Logger.getLogger(HyberbinTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @After
    public void tearDown() {
        DatabaseAccess access = new DatabaseAccess(ConfigCenter.INSTANCE.getManager());
        try {
            int update = access.update("delete  from servers");
            System.out.println("删除了" + update + "条数据");
        } catch (SQLException e) {
            System.out.println("初始化错误");
            e.printStackTrace();
        }
    }

    /**
     * Test of setTableName method, of class Hyberbin.
     */
    @Test
    public void testSetTableName() {
        System.out.println("setTableName");
        String tableName = "error";
        Servers servers = new Servers();
        Hyberbin instance = new Hyberbin(servers);
        instance.setTableName(tableName);
        servers.setId(0);
        try {
            instance.showOnebyKey("id");
        } catch (SQLException ex) {
            assertTrue(ex instanceof SQLException);
            return;
        }
        fail("testSetTableName faild.");
    }

    public static Servers getByID(int id) {
        Servers servers = new Servers();
        servers.setId(id);
        Hyberbin<Servers> hyb = new Hyberbin(servers);
        try {
            return hyb.showOnebyKey("id");
        } catch (SQLException ex) {
            return null;
        }
    }

    /**
     * Test of removeField method, of class Hyberbin.
     */
    @Test
    public void testRemoveField() {
        System.out.println("removeField");
        String fieldName = "adds";
        Servers servers = getByID(1);
        String adds = servers.getAdds();
        Hyberbin instance = new Hyberbin(servers);
        instance.removeField(fieldName);
        servers.setAdds("abcdefg");
        try {
            instance.updateByKey("id");
        } catch (SQLException ex) {
            fail("testRemoveField faild.");
            ex.printStackTrace();
        }
        String after = getByID(1).getAdds();
        assertEquals(after, adds);
        assertNotSame(after, "abcdefg");
    }

    /**
     * Test of setField method, of class Hyberbin.
     */
    @Test
    public void testSetField() {
        System.out.println("setField");
        String fieldName = "id";
        Servers servers = new Servers();
        servers.setId(1);
        Hyberbin instance = new Hyberbin(servers);
        Hyberbin<Servers> result = instance.setField(fieldName);
        try {
            servers = result.showOnebyKey("id");
        } catch (SQLException ex) {
            fail("testSetField faild.");
            ex.printStackTrace();
        }
        assertNotNull(servers.getId());
        assertNull(servers.getAdds());
        assertNull(servers.getName());
        assertNull(servers.getNote());
        assertNull(servers.getType());
    }

    /**
     * Test of setUpdateNull method, of class Hyberbin.
     */
    @Test
    public void testSetUpdateNull() {
        System.out.println("setUpdateNull");
        Servers servers = new Servers();
        servers.setId(1);
        Hyberbin instance = new Hyberbin(servers);
        instance.setUpdateNull(true);
        try {
            instance.updateByKey("id");
        } catch (SQLException ex) {
            fail("testSetField faild.");
            ex.printStackTrace();
        }
        servers = getByID(1);
        assertNotNull(servers.getId());
        assertNull(servers.getAdds());
        assertNull(servers.getName());
        assertNull(servers.getNote());
        assertNull(servers.getType());
    }

    /**
     * Test of addParmeter method, of class Hyberbin.
     */
    @Test
    public void testAddParmeter() {
        System.out.println("addParmeter");
        Object parmeter = 1;
        String sql = "select * from servers where id=?";
        Hyberbin instance = new Hyberbin();
        instance.addParmeter(parmeter);
        List<Map> mapList = null;
        try {
            mapList = instance.getMapList(sql);
        } catch (SQLException ex) {
            fail("testAddParmeter faild.");
        }
        System.out.println(mapList);
        assertNotNull(mapList);
        assertTrue(ObjectHelper.isNotEmpty(mapList));
    }

    /**
     * Test of insert method, of class Hyberbin.
     */
    @Test
    public void testInsert() {
        try {
            Servers servers = new Servers();
            servers.setId(100);
            servers.setName("server");
            servers.setNote("server");
            servers.setType(100);
            servers.setAdds("adds");
            Hyberbin hyberbin = new Hyberbin(servers);
            hyberbin.insert("");
        } catch (SQLException ex) {
            fail("testInsert faild.");
        }
    }

    /**
     * Test of updateByKey method, of class Hyberbin.
     */
    @Test
    public void testUpdateByKey() {
        System.out.println("updateByKey");
        Servers servers = getByID(1);
        String name = "testUpdateByKey";
        servers.setName(name);
        Hyberbin instance = new Hyberbin(servers);
        try {
            instance.updateByKey("id");
        } catch (SQLException ex) {
            fail("testUpdateByKey faild.");
            ex.printStackTrace();
        }
        servers = getByID(1);
        assertEquals(servers.getName(), name);
    }

    /**
     * Test of autoUp method, of class Hyberbin.
     */
    @Test
    public void testAutoUp() {
        System.out.println("autoUp");
        Servers servers = getByID(1);
        Integer type = servers.getType();
        Hyberbin instance = new Hyberbin(servers);
        int result = 0;
        try {
            result = instance.autoUp("type", "where id=1");
        } catch (SQLException ex) {
            fail("testUpdateByKey faild.");
            ex.printStackTrace();
        }
        assertEquals(result, 1);
        Servers byID = getByID(1);
        assertEquals(type.longValue(), byID.getType() - 1);
    }

    /**
     * Test of delete method, of class Hyberbin.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        Servers servers = getByID(1);
        Hyberbin instance = new Hyberbin(servers);
        int result = 0;
        try {
            result = instance.delete("where id=1");
        } catch (SQLException ex) {
            fail("testUpdateByKey faild.");
            ex.printStackTrace();
        }
        assertEquals(result, 1);
        Servers byID = getByID(1);
        assertNull(byID.getId());
        assertNull(byID.getAdds());
        assertNull(byID.getName());
        assertNull(byID.getType());
        assertNull(byID.getNote());
    }

    /**
     * Test of deleteByKey method, of class Hyberbin.
     */
    @Test
    public void testDeleteByKey() {
        System.out.println("deleteByKey");
        Servers servers = getByID(1);
        Hyberbin instance = new Hyberbin(servers);
        int result = 0;
        try {
            result = instance.deleteByKey("id");
        } catch (SQLException ex) {
            fail("testUpdateByKey faild.");
            ex.printStackTrace();
        }
        assertEquals(result, 1);
        Servers byID = getByID(1);
        assertNull(byID.getId());
        assertNull(byID.getAdds());
        assertNull(byID.getName());
        assertNull(byID.getType());
        assertNull(byID.getNote());
    }

    /**
     * Test of showOne method, of class Hyberbin.
     */
    @Test
    public void testShowOne() {
        System.out.println("showOne");
        Servers servers = new Servers();
        Hyberbin<Servers> instance = new Hyberbin(servers);
        try {
            servers = instance.showOne("select * from servers");
        } catch (SQLException ex) {
            fail("testShowOne faild.");
            ex.printStackTrace();
        }
        assertNotNull(servers);
        assertNotNull(servers.getAdds());
        assertNotNull(servers.getName());
        assertNotNull(servers.getId());
        assertNotNull(servers.getType());
        assertNotNull(servers.getNote());
    }

    /**
     * Test of showOnebyKey method, of class Hyberbin.
     */
    @Test
    public void testShowOnebyKey() {
        System.out.println("showOnebyKey");
        Servers servers = getByID(1);
        assertNotNull(servers);
        assertNotNull(servers.getAdds());
        assertNotNull(servers.getName());
        assertNotNull(servers.getId());
        assertNotNull(servers.getType());
        assertNotNull(servers.getNote());
    }

    /**
     * Test of showAll method, of class Hyberbin.
     */
    @Test
    public void testShowAll() {
        System.out.println("showAll");
        Hyberbin instance = new Hyberbin(new Servers());
        try {
            List result = instance.showAll();
            assertTrue(ObjectHelper.isNotEmpty(result));
        } catch (SQLException ex) {
            fail("testShowAll faild.");
            ex.printStackTrace();
        }

    }

    /**
     * Test of getCount method, of class Hyberbin.
     */
    @Test
    public void testGetCount() {
        System.out.println("getCount");
        Hyberbin instance = new Hyberbin();
        int result = 0;
        try {
            result = instance.getCount("select * from servers");
        } catch (SQLException ex) {
            fail("testGetCount faild.");
            ex.printStackTrace();
        }
        assertTrue(result > 0);
    }

    /**
     * Test of showByPage method, of class Hyberbin.
     */
    @Test
    public void testShowByPage() {
        System.out.println("showByPage");
        String where = "";
        Pager pager = new Pager(2);
        Hyberbin instance = new Hyberbin(new Servers());
        try {
            instance.showByPage(where, pager);
            assertFalse(ObjectHelper.isEmpty(pager.getData()));
            assertTrue(pager.getItems() == 5);
            assertTrue(pager.getTotalPage() == 3);
        } catch (SQLException ex) {
            fail("testShowByPage faild.");
            ex.printStackTrace();
        }
    }

    /**
     * Test of showList method, of class Hyberbin.
     */
    @Test
    public void testShowList() {
        System.out.println("showList");
        String sql = "select * from Servers";
        Hyberbin instance = new Hyberbin(new Servers());
        List result = null;
        try {
            result = instance.showList(sql);
        } catch (SQLException ex) {
            fail("testShowList faild.");
            ex.printStackTrace();
        }
        assertFalse(ObjectHelper.isEmpty(result));
        assertTrue(result.size() == 5);
    }

    /**
     * Test of getMapList method, of class Hyberbin.
     */
    @Test
    public void testGetMapList_String() {
        System.out.println("getMapList");
        String sql = "select * from Servers";
        Hyberbin instance = new Hyberbin();
        List<Map> result = null;
        try {
            result = instance.getMapList(sql);
        } catch (SQLException ex) {
            fail("testGetMapList_String faild.");
            ex.printStackTrace();
        }
        assertFalse(ObjectHelper.isEmpty(result));
        assertTrue(result.size() == 5);
    }

    /**
     * Test of getMapList method, of class Hyberbin.
     */
    @Test
    public void testGetMapList_String_Pager() {
        System.out.println("getMapList");
        Pager pager = new Pager(2);
        Hyberbin instance = new Hyberbin(new Servers());
        String sql = "select * from Servers";
        try {
            instance.getMapList(sql, pager);
            assertFalse(ObjectHelper.isEmpty(pager.getData()));
            assertTrue(pager.getItems() == 5);
            assertTrue(pager.getTotalPage() == 3);
        } catch (SQLException ex) {
            ex.printStackTrace();
            fail("testGetMapList_String_Pager faild.");
        }
    }

    /**
     * Test of getNuList method, of class Hyberbin.
     */
    @Test
    public void testGetNuList() {
        System.out.println("getNuList");
        Hyberbin instance = new Hyberbin();
        List<FieldColumn> result = instance.getNuList();
        assertTrue(ObjectHelper.isEmpty(result));
    }

    /**
     * Test of addNullField method, of class Hyberbin.
     */
    @Test
    public void testAddNullField() {
        System.out.println("addNullField");
        String field = "id";
        Hyberbin instance = new Hyberbin(new Servers());
        instance.addNullField(field);
    }

}
```
