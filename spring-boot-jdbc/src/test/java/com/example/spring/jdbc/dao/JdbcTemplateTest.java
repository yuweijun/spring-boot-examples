package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.User;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.object.SqlFunction;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http://www.importnew.com/18016.html
 * http://www.importnew.com/17939.html
 * http://www.importnew.com/18013.html
 *
 * @author yuweijun 2017-02-23
 */
public class JdbcTemplateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplateTest.class);

    private static JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        String createTableSql = "create table test (id int(4) AUTO_INCREMENT PRIMARY KEY, name varchar(100))";
        jdbcTemplate.update(createTableSql);
    }

    @BeforeClass
    public static void setUpClassOnlyRunOnce() {
        LOGGER.info("setup jdbc connection for test class, and only run this method once .................");
        String url = "jdbc:mysql://localhost/jdbc";
        String username = "dbuser";
        String password = "dbpass";
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @After
    public void tearDown() {
        String dropTableSql = "drop table test";
        jdbcTemplate.execute(dropTableSql);
    }

    @Test
    public void test() {
        // 1.声明SQL
        String sql = "select * from user";
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                // 2.处理结果集
                String value = rs.getString("name");
                System.out.println("Column TABLENAME:" + value);
            }
        });
    }

    @Test
    public void testPpreparedStatement1() {
        int count = jdbcTemplate.execute(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn)
                    throws SQLException {
                return conn.prepareStatement("select count(*) from test");
            }
        }, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement pstmt)
                    throws SQLException, DataAccessException {
                pstmt.execute();
                ResultSet rs = pstmt.getResultSet();
                rs.next();
                return rs.getInt(1);
            }
        });
        Assert.assertEquals(0, count);
    }

    @Test
    public void testPreparedStatement2() {
        String insertSql = "insert into test(name) values (?)";
        int count = jdbcTemplate.update(insertSql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setObject(1, "name4");
            }
        });

        Assert.assertEquals(1, count);
        String deleteSql = "delete from test where name=?";
        count = jdbcTemplate.update(deleteSql, new Object[]{"name4"});
        Assert.assertEquals(1, count);
    }

    @Test
    public void testResultSet1() {
        jdbcTemplate.update("insert into test(name) values('name5')");
        String listSql = "select * from test";
        List result = jdbcTemplate.query(listSql, new RowMapper<Map>() {
            @Override
            public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map row = new HashMap();
                row.put(rs.getInt("id"), rs.getString("name"));
                return row;
            }
        });

        Assert.assertEquals(1, result.size());
        jdbcTemplate.update("delete from test where name='name5'");
    }

    @Test
    public void testResultSet2() {
        jdbcTemplate.update("insert into test(name) values('name5')");
        String listSql = "select * from test";
        final List result = new ArrayList();
        jdbcTemplate.query(listSql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Map row = new HashMap();
                row.put(rs.getInt("id"), rs.getString("name"));
                result.add(row);
            }
        });

        //1.查询一行数据并返回int型结果
        jdbcTemplate.queryForObject("select count(*) from test", int.class);
        //2. 查询一行数据并将该行数据转换为Map返回
        jdbcTemplate.queryForMap("select * from test where name='name5'");
        //3.查询一行任何类型的数据，最后一个参数指定返回结果类型
        jdbcTemplate.queryForObject("select count(*) from test", Integer.class);
        //4.查询一批数据，默认将每行数据转换为Map
        jdbcTemplate.queryForList("select * from test");
        //5.只查询一列数据列表，列类型是String类型，列名字是name
        jdbcTemplate.queryForList("select name from test where name=?", new Object[]{"name5"}, String.class);
        //6.查询一批数据，返回为SqlRowSet，类似于ResultSet，但不再绑定到连接上

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from test");

        Assert.assertEquals(1, result.size());
        jdbcTemplate.update("delete from test where name='name5'");
    }

    @Test
    @Ignore
    public void testResultSet3() {
        jdbcTemplate.update("insert into test(name) values('name5')");
        String listSql = "select * from test";
        List result = jdbcTemplate.query(listSql, new ResultSetExtractor<List>() {
            @Override
            public List extractData(ResultSet rs)
                    throws SQLException, DataAccessException {
                List result = new ArrayList();
                while (rs.next()) {
                    Map row = new HashMap();
                    row.put(rs.getInt("id"), rs.getString("name"));
                    result.add(row);
                }
                return result;
            }
        });

        Assert.assertEquals(0, result.size());
        jdbcTemplate.update("delete from test where name='name5'");
    }

    @Test
    public void testNamedParameterJdbcTemplate1() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
        //namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String insertSql = "insert into test(name) values(:name)";
        String selectSql = "select * from test where name=:name";
        String deleteSql = "delete from test where name=:name";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", "name5");
        namedParameterJdbcTemplate.update(insertSql, paramMap);
        final List<Integer> result = new ArrayList<Integer>();
        namedParameterJdbcTemplate.query(selectSql, paramMap,
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        result.add(rs.getInt("id"));
                    }
                });
        Assert.assertEquals(1, result.size());
        SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        namedParameterJdbcTemplate.update(deleteSql, paramSource);
    }

    @Test
    public void testNamedParameterJdbcTemplate2() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        User model = new User();
        model.setName("name5");
        String insertSql = "insert into test(name) values(:name)";
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(model);
        namedParameterJdbcTemplate.update(insertSql, paramSource);
    }

    static class UserSqlQuery extends SqlQuery<User> {
        public UserSqlQuery(JdbcTemplate jdbcTemplate) {
            //super.setDataSource(jdbcTemplate.getDataSource());
            super.setJdbcTemplate(jdbcTemplate);
            super.setSql("select * from test where name = ?");
            super.declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        @Override
        protected RowMapper<User> newRowMapper(Object[] parameters, Map context) {
            return new RowMapper() {
                @Override
                public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map row = new HashMap();
                    row.put(rs.getInt("id"), rs.getString("name"));
                    return row;
                }
            };
        }
    }

    static class InsertUser extends SqlUpdate {
        public InsertUser(JdbcTemplate jdbcTemplate) {
            super.setJdbcTemplate(jdbcTemplate);
            super.setSql("insert into test(name) values(?)");
            super.declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }
    }

    @Test
    public void testSqlUpdate() {
        SqlUpdate insert = new InsertUser(jdbcTemplate);
        insert.update("name5");

        String updateSql = "update test set name=? where name = ?";
        SqlUpdate update = new SqlUpdate(jdbcTemplate.getDataSource(), updateSql, new int[]{Types.VARCHAR, Types.VARCHAR});
        update.update("name6", "name5");
        String deleteSql = "delete from test where name = :name";

        SqlUpdate delete = new SqlUpdate(jdbcTemplate.getDataSource(), deleteSql, new int[]{Types.VARCHAR});
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", "name5");
        delete.updateByNamedParam(paramMap);
    }

    @Test
    public void testSqlQuery() {
        SqlQuery query = new UserSqlQuery(jdbcTemplate);
        List<User> result = query.execute("name5");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testSqlFunction() {
        jdbcTemplate.update("insert into test(name) values('name5')");
        String countSql = "select count(*) from test";
        SqlFunction<Integer> sqlFunction1 = new SqlFunction<Integer>(jdbcTemplate.getDataSource(), countSql);
        Assert.assertEquals(1, sqlFunction1.run());
        String selectSql = "select name from test where name = ?";
        SqlFunction<String> sqlFunction2 = new SqlFunction<String>(jdbcTemplate.getDataSource(), selectSql);
        sqlFunction2.declareParameter(new SqlParameter(Types.VARCHAR));
        String name = (String) sqlFunction2.runGeneric(new Object[]{"name5"});
        Assert.assertEquals("name5", name);
    }

    @Test
    public void crud() {
        insert();
        delete();
        update();
        select();
    }

    private void insert() {
        jdbcTemplate.update("insert into test(name) values('name1')");
        jdbcTemplate.update("insert into test(name) values('name2')");
        int t = jdbcTemplate.queryForObject("select count(*) from test", int.class);
        Assert.assertEquals(2, t);
    }

    private void delete() {
        jdbcTemplate.update("delete from test where name=?", new Object[]{"name2"});
        int t = jdbcTemplate.queryForObject("select count(*) from test", int.class);
        Assert.assertEquals(1, t);
    }

    private void update() {
        jdbcTemplate.update("update test set name='name3' where name=?", new Object[]{"name1"});
        int t = jdbcTemplate.queryForObject("select count(*) from test where name='name3'", int.class);
        Assert.assertEquals(1, t);
    }

    private void select() {
        jdbcTemplate.query("select * from test", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.print("====id:" + rs.getInt("id"));
                System.out.println(",name:" + rs.getString("name"));
            }
        });
    }

    /**
     * new SimpleJdbcInsert(jdbcTemplate) ： 首次通过DataSource对象或JdbcTemplate对象初始化SimpleJdbcInsert；
     * insert.withTableName(“test”) ：用于设置数据库表名；
     * args ： 用于指定插入时列名及值，如本例中只有name列名，即编译后的sql类似于“insert into test(name) values(?)”；
     * insert.compile() ：可选的编译步骤，在调用执行方法时自动编译，编译后不能再对insert对象修改；
     * 执行：execute方法用于执行普通插入；executeAndReturnKey用于执行并获取自动生成主键（注意是Number类型），必须首先通过setGeneratedKeyName设置主键然后才能获取，如果想获取复合主键请使用setGeneratedKeyNames描述主键然后通过executeReturningKeyHolder获取复合主键KeyHolder对象；executeBatch用于批处理；
     */
    @Test
    public void testSimpleJdbcInsert() {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.withTableName("test");
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("name", "name5");
        insert.compile();

        // 1.普通插入
        insert.execute(args);
        int t = jdbcTemplate.queryForObject("select count(*) from test", int.class);
        Assert.assertEquals(1, t);

        // 2.插入时获取主键值
        insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.withTableName("test");
        insert.setGeneratedKeyName("id");
        Number id = insert.executeAndReturnKey(args);
        Assert.assertEquals(2, id.intValue());

        // 3.批处理
        insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.withTableName("test");
        insert.setGeneratedKeyName("id");
        int[] updateCount = insert.executeBatch(new Map[]{args, args, args});
        Assert.assertEquals(1, updateCount[0]);

        int s = jdbcTemplate.queryForObject("select count(*) from test", int.class);
        Assert.assertEquals(5, s);
    }

    @Test
    public void testFetchKey1() throws SQLException {
        final String insertSql = "insert into test(name) values('name5')";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn)
                    throws SQLException {
                return conn.prepareStatement(insertSql, new String[]{"id"});
            }
        }, generatedKeyHolder);

        Assert.assertEquals(1, generatedKeyHolder.getKey().intValue());
    }

    @Test
    public void testFetchKey2() {
        final String insertSql = "insert into test(name) values('name5')";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlUpdate update = new SqlUpdate();
        update.setJdbcTemplate(jdbcTemplate);
        update.setReturnGeneratedKeys(true);
        //update.setGeneratedKeysColumnNames(new String[]{"id"});
        update.setSql(insertSql);
        update.update(null, generatedKeyHolder);
        Assert.assertEquals(1, generatedKeyHolder.getKey().intValue());
    }

    @Test
    public void testBatchUpdate1() {
        String insertSql = "insert into test(name) values('name5')";
        String[] batchSql = new String[]{insertSql, insertSql};
        jdbcTemplate.batchUpdate(batchSql);
        int t = jdbcTemplate.queryForObject("select count(*) from test", int.class);
        Assert.assertEquals(2, t);
    }

    @Test
    public void testBatchUpdate3() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String insertSql = "insert into test(name) values(:name)";
        User model = new User();
        model.setName("name5");
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(new Object[]{model, model});
        namedParameterJdbcTemplate.batchUpdate(insertSql, params);
        int t = jdbcTemplate.queryForObject("select count(*) from test", int.class);
        Assert.assertEquals(2, t);
    }

    @Test
    public void testBatchUpdate5() {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.withTableName("test");
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("name", "name5");
        insert.executeBatch(new Map[]{valueMap, valueMap});
        int t = jdbcTemplate.queryForObject("select count(*) from test", int.class);
        Assert.assertEquals(2, t);
    }

}
