import org.apache.jasper.runtime.HttpJspBase;
import org.apache.jasper.runtime.InstanceManagerFactory;
import org.apache.jasper.runtime.JspSourceDependent;
import org.apache.tomcat.InstanceManager;

import javax.el.ExpressionFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.SkipPageException;
import java.io.IOException;
import java.util.Map;

public class welcome_jsp extends HttpJspBase implements JspSourceDependent{

    private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();
    private static Map<String, Long> _jspx_dependants;
    private ExpressionFactory _el_expressionfactory;
    private InstanceManager jsp_instancemanager;

    public Map<String, Long> getDependants() {
        return _jspx_dependants;
    }

    @Override
    public void _jspInit() {
        _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        jsp_instancemanager = InstanceManagerFactory.getInstanceManager(getServletConfig());
    }

    @Override
    protected void _jspDestroy() {

    }

    @Override
    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final PageContext pageContext;
        HttpSession session = null;
        final ServletContext application;
        final javax.servlet.ServletConfig config;
        JspWriter out = null;
        final Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("<html>\n");
            out.write("<head><title>Welcome</title></head>\n");
            out.write("<body>\n");
            out.write("Welcome\n");
            out.write("</body>\n");
            out.write("</html>");
        } catch (Throwable t){
            if (!(t instanceof SkipPageException)){
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0)
                    try {
                        out.clearBuffer();
                    } catch (IOException e) {
                    }
                if (_jspx_page_context != null){
                    _jspx_page_context.handlePageException(t);
                }
            }
        } finally {
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
}
