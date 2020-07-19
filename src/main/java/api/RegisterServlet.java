package api;

import model.User;
import model.UserDao;
import view.HtmlGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: asus
 * Date: 2020-07-19
 * Time: 9:18
 */
public class RegisterServlet extends HttpServlet {
    //浏览器通过post方法提交注册信息给服务器

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取到前段提交的数据(用户名 密码) 校验是否合法
        resp.setContentType("text/html; charset=utf-8");
        String name=req.getParameter("name");
        String password=req.getParameter("password");
        if (name==null||"".equals(name)||password==null||"".equals(password)){
            //用户提交的数据有误
            //返回一个错误页面
            String html = HtmlGenerator.getMessagaPage("用户名或者密码为空","register.html");
            resp.getWriter().write(html);
            return;
        }
        //2 拿着用户名在数据库中查 看存在 存在就注册失败
        UserDao userDao = new UserDao();
        User existUser = userDao.selectByName(name);
        if (existUser!=null) {

        }
        // 3.根据前端提交的数据构造user 对象 并插入到数据库中
        // 4.返回一个结果页面 提示当前注册成功
        }
}
