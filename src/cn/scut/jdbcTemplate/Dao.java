package cn.scut.jdbcTemplate;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.activation.FileDataSource;
import javax.ws.rs.GET;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Dao {
	@Test
	public void add() {
		// 设置连接数据库参数
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/company");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");

		// 创建jdbcTemplate对象
		JdbcTemplate template = new JdbcTemplate(dataSource);
		// 给出sql模板
		String sql = "insert into user values(?,?)";
		// 调用方法
		template.update(sql, "zhuzhu", "123");

	}

	@Test
	public void update() {
		// 设置连接数据库参数
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/company");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");
		// 创建jdbcTemplate对象
		JdbcTemplate template = new JdbcTemplate(dataSource);
		// 给出sql模板
		String sql = "update user set password=? where username=?";
		template.update(sql, "haha", "yangzhen");
	}

	@Test
	public void delete() {
		// 设置连接数据库参数
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/company");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");
		// 创建jdbcTemplate对象
		JdbcTemplate template = new JdbcTemplate(dataSource);
		// 给出sql模板
		String sql = "delete from user where username=?";
		template.update(sql, "zhuzhu");
	}

	@Test
	public void query() {
		// 设置连接数据库参数
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/company");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");
		// 创建jdbcTemplate对象
		JdbcTemplate template = new JdbcTemplate(dataSource);
		// 给出sql模板
		String sql = "select count(*) from user";
		// 查询单行单列
		int count = template.queryForObject(sql, Integer.class);
		System.out.println(count);
		// 查询一行
		String sql2 = "select *from user where username=?";
		User user = template
				.queryForObject(sql2, new MyRowMapper(), "yangzhen");
		System.out.println(user);
		// 查询多行
		String sql3 = "select *from user";
		List<User> list = template.query(sql3, new MyRowMapper2<User>(User.class));
		System.out.println(list);

	}
}

class MyRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet arg0, int arg1) throws SQLException {
		// 对数据封装即可
		String username = arg0.getString("username");
		String password = arg0.getString("password");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);

		return user;
	}

}
//模仿dbutils结果集转换写法
class MyRowMapper2<T> implements RowMapper<T> {
	private final Class<? extends T> type;


	public MyRowMapper2(Class<? extends T> type) {
		this.type = type;
	}

	@Override
	public T mapRow(ResultSet arg0, int arg1) throws SQLException {

		// 对数据封装即可
		String username = arg0.getString("username");
		String password = arg0.getString("password");
		T t = null;
		try {
			t = type.newInstance();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				//PropertyDescriptor可以返回属性的set和get方法
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
						type);
				Method setMethod = pd.getWriteMethod();
				if(field.getName().equalsIgnoreCase("username")){
					setMethod.invoke(t, username);	
				}
				
				if(field.getName().equalsIgnoreCase("password")){
					setMethod.invoke(t, password);
				}
				

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return t;
	}

}
