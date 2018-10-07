package cn.scut.jdbcTemplateApply;

import org.springframework.jdbc.core.JdbcTemplate;


public class UserDao {
	
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void add() {
		String sql="insert into user values(?,?)";
		jdbcTemplate.update(sql, "zhangsan","123321");
		
	}
}
