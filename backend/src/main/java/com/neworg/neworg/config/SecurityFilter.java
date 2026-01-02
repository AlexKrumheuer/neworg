package com.neworg.neworg.config;

import com.neworg.neworg.user.UserRepository;
import com.neworg.neworg.service.TokenService;
import com.neworg.neworg.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        
        if(token != null){
            var login = tokenService.validateToken(token);

            if(login != null && !login.isEmpty()){
                User user = userRepository.findByEmail(login);

                // Cria o objeto de autenticação do Spring (Usuario, Credenciais, Permissões)
                // Como não temos roles/perfis ainda, passamos lista vazia
                var authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                
                // Salva no contexto: "O usuário está logado!"
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Chama o próximo filtro (segue o fluxo)
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}