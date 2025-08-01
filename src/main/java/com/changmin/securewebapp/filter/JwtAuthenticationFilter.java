package com.changmin.securewebapp.filter;

import com.changmin.securewebapp.entity.User;
import com.changmin.securewebapp.repository.UserRepository;
import com.changmin.securewebapp.service.LogoutService;
import com.changmin.securewebapp.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final LogoutService logoutService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")){
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);

		if (!jwtUtil.validateToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		if(logoutService.isLoggedOut(token)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized: 로그아웃된 토큰입니다.");
			return;
		}

		String username = jwtUtil.extractUsername(token);
		User user = userRepository.findByUsername(username).orElse(null);

		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized: 사용자 정보를 찾을 수 없음.");
			return;
		}

		String role = user.getRole();

		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority(role)));

		authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}