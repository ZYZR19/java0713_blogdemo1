package api;

import com.sun.org.apache.bcel.internal.generic.NEW;
import model.User;
import model.UserDao;
import sun.nio.cs.US_ASCII;
import view.HtmlGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;
import java.io.IOException;

//实现登录功能
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取到用户的信息,并进行简单的校验
        //2.数据库中查找,看用户是否存在
        //3.对比密码是否匹配
        //4.匹配成功登录成功 创建一个Session  servlet本来就实现了session管理 可管理用户的登录状态
        // 自动分配sessionid 和session对象以键值对的形式组织起来,然后保存,同时吧session以set_cookie的形式返回给浏览器
        //5.返回一个登录成功的提示页面
        resp.setContentType("text/html; charset =utf-8");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        if (name==null||"".equals(name)||password==null||" ".equals(password)) {
            String html = HtmlGenerator.getMessagePage("您提交的用户名或密码有误! 请重新输入","login.html");
            resp.getWriter().write(html);
            return;
        }

        UserDao userDao = new UserDao();
        User user = userDao.selectByName(name);
        if (user ==null||password.equals(user.getPassword())) {//如果密码不匹配或者用户名不存在
            String html = HtmlGenerator.getMessagePage("用户名或者密码错误","login.html");
            resp.getWriter().write(html);
            return;
        }


        HttpSession httpSession = req.getSession(true);
        httpSession.setAttribute("user",user);//后续再访问服务器时 服务器就知道客户端到底是谁了

        String html = HtmlGenerator.getMessagePage("登录成功","article");
        resp.getWriter().write(html);

    }
}
