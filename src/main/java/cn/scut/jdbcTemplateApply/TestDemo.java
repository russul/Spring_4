package cn.scut.jdbcTemplateApply;



import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDemo {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"bean.xml");
		UserService userService = (UserService) context.getBean("userService");
		userService.add();

	}

}
