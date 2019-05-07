package facetec.core.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rkogawa on 06/05/19.
 */
@Component
public class TokenAuthenticationService {

    private static final String SECRET = "MySecreteApp";

    private static final String HEADER_AUTH_STRING = "Authorization";

    private static final String HEADER_FAILURE_STRING = "Failure";

    @Transactional
    public void addAuthentication(HttpServletResponse response,
            Authentication username) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(username.getName())
                .setExpiration(new Date(System.currentTimeMillis() + 860_000_000))
                .signWith(SignatureAlgorithm.HS512, SECRET);
        response.addHeader(HEADER_AUTH_STRING, jwtBuilder.compact());
    }

    public void addFailureAuthentication(HttpServletResponse response,
            String message) {
        response.addHeader(HEADER_FAILURE_STRING, message);
    }

    @Transactional
    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTH_STRING);

        if (StringUtils.isNotEmpty(token)) {
            Claims jwtBody = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            String user = jwtBody.getSubject();
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }
        return null;
    }
}
