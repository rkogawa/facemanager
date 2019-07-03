package facetec.core.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by rkogawa on 25/05/19.
 */
@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @Resource
    private ErrorAttributes errorAttributes;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @RequestMapping(value = PATH)
    public ResponseEntity error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletWebRequest requestAttributes = new ServletWebRequest(request);

        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(requestAttributes, false);
        if (Integer.valueOf("404").equals(errorAttributes.get("status"))) {
            String targetPath = (String) errorAttributes.get("path");
            // Ignora recursos não encontrados, por ex .css e .js
            if (!targetPath.contains(".")) {
                targetPath = targetPath.replace(contextPath, "");
                String urlPath = "?errorType=404&targetPath=" + targetPath;
                if(request.getParameterMap().containsKey("token")) {
                    urlPath += "&token=" + request.getParameter("token");
                }
                String encodeRedirectURL = response.encodeRedirectURL(urlPath);
                response.sendRedirect(encodeRedirectURL);

                // Null para dizer ao Spring que já processamos o HttpServletResponse
                return null;
            }
            return new ResponseEntity<>(errorAttributes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errorAttributes, HttpStatus.valueOf(response.getStatus()));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
