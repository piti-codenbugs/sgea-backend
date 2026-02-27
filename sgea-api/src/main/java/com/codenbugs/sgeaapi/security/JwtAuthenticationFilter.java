package com.codenbugs.sgeaapi.security;

import com.codenbugs.sgeaapi.entity.login_test.UserTest;
import com.codenbugs.sgeaapi.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Método que realiza todos los filtros relacionados con el Token.
     *
     * @param request     contiene el token.
     * @param response    modifica lo que se envía al cliente
     * @param filterChain cadena de filtros configurada.
     * @throws ServletException es una excepción general del contenedor Tomcat, se lanza cuando falla un filtro,
     *                          un servlet o hay problema en la cadena de filtros.
     * @throws IOException      es lanzada cuando el cliente cierra la conexión o hay error en la red.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = getTokenFromRequest(request);
            final String email;

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            email = jwtService.getEmailFromToken(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserTest userDetails = (UserTest) userDetailsService.loadUserByUsername(email);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new BadRequestException("Token inválido o expirado");
        }
    }

    /**
     * Devuelve el Token
     *
     * @param request contiene en el encabezado lo que se necesita para generar el Token.
     * @return la cadena de caracteres que representa el Token.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}