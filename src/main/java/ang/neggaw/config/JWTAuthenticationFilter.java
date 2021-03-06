package ang.neggaw.config;

import ang.neggaw.entities.ClientOnline;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ClientOnline clientOnline = null;
        try {
            clientOnline = new ObjectMapper().readValue(request.getInputStream(), ClientOnline.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    clientOnline.getEmailClient(),
                    clientOnline.getPassword()
            ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User springUser = (User) authResult.getPrincipal();

        String tokenJWT = Jwts.builder()
                .setSubject(springUser.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .claim("roles", springUser.getAuthorities())
                .claim("emailClient", springUser.getUsername())
                .compact();

        response.addHeader(SecurityConstants.HEADER_TOKEN, SecurityConstants.PREFIX_TOKEN + tokenJWT);
    }
}
