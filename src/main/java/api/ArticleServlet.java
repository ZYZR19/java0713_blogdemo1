package api;

import model.Article;
import model.ArticleDao;
import model.User;
import model.UserDao;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import view.HtmlGenerator;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//获取文章列表
public class ArticleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.验证用户是否登录 尚未登录就提示用户先登录
        //2.判断请求中是否存在article id参数
        //a. 没有这个参数就去执行获取文章列表参数
        //b.有这个参数就去执行获取文章详情操作
        resp.setContentType("text/html;charset=utf-8");
        HttpSession httpSession = req.getSession(false) ;
        if (httpSession ==null) {
            //未登录状态
            String html = HtmlGenerator.getMessagePage("请先登录","login.html");
            resp.getWriter().write(html);
            return;
        }

        User user = (User) httpSession.getAttribute("user");
        //判断如果当前登陆成功 就获取到user对象 这个对象来自于登录逻辑
        //通过session来获得内部存储的user对象也就知道了用户信息
        String  articleIdstr= req.getParameter("articleId");
        //与api相关
        if (articleIdstr==null) {
             getAllArticle(user,resp);//用户信息 和 html响应结果到resp
        }else {
            getOneArticle(Integer.parseInt(articleIdstr),user,resp);
        }
     }

    private void getOneArticle(int articleId, User user, HttpServletResponse resp) throws IOException {
        ArticleDao articleDao = new ArticleDao();
        Article article = articleDao.selectById(articleId);
        if (article == null) {
            String  html = HtmlGenerator.getMessagePage("文章不存在","article");
            resp.getWriter().write(html);
            return;
        }

        UserDao userDao = new UserDao();
        Article author = articleDao.selectById(article.getUserId());
        String html = HtmlGenerator.getArticleDetailPage(article,user,author);
        resp.getWriter().write(html);
    }

    private void getAllArticle(User user, HttpServletResponse resp) throws IOException {
        //获取文章列表
        //1.查找数据库
        //2.构造页面
        ArticleDao articleDao = new ArticleDao();
        List<Article> articles = articleDao.selectAll();//查到所有文章
        String html = HtmlGenerator.getArticleListPage(articles,user);//获取一个页面生成一个文章列表
        resp.getWriter().write(html);

    }
}
