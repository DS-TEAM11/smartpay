package org.shds.smartpay.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.repository.MemberRepository;
import org.shds.smartpay.security.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import org.shds.smartpay.security.filter.JwtAuthenticationProcessingFilter;
import org.shds.smartpay.security.handler.LoginFailureHandler;
import org.shds.smartpay.security.handler.LoginSuccessHandler;
import org.shds.smartpay.security.oauth.handler.OAuth2LoginFailureHandler;
import org.shds.smartpay.security.oauth.handler.OAuth2LoginSuccessHandler;
import org.shds.smartpay.security.oauth.service.CustomOAuth2UserService;
import org.shds.smartpay.security.service.JwtService;
import org.shds.smartpay.security.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Log4j2
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private String front_url;
    private final LoginService loginService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

//        http.headers(headers -> headers.httpStrictTransportSecurity(s->{}).disable());

        // CORS 설정 추가
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/", "/index.html", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**").permitAll()
                    .requestMatchers("/member/signup", "/oauth2/sign-up", "/login", "/logout", "/api/payment/done", "/seller", "/seller/**").permitAll() // 특정 페이지 접근 허용
                    //.anyRequest().authenticated(); // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
                    .requestMatchers("/ws/**").permitAll()  // WebSocket 엔드포인트는 인증없이 접근 가능하도록 설정
                    .requestMatchers("/member/jwt-test", "/member/tetest", "/member/logout").hasRole("USER")
                    .anyRequest().permitAll();
            //.anyRequest().authenticated();

        });

        // 세션을 사용하지 않도록 설정
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 Handler 설정
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // customUserService 설정
        );

        // 인증되지 않은 사용자의 요청 처리 (카카오 로그인으로 리다이렉트되지 않도록 설정)
        //근데 이게 있으니까 oauth 기본 로그인 페이지가 사라지긴했는데 일단 ok
        //어차피 rest api로 사용할거니까
        //만약 oauth 기본 로그인 페이지 필요하면 여기 아래 3줄 주석처리하면 됨!
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/login"); // 인증되지 않은 사용자는 이 URL로 리다이렉트
                })
        );


        // 필터 설정
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        // FormLogin 및 HttpBasic 비활성화
        http.formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }


    //Cors 설정 Security로 통일
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
//        System.out.println("########### 여기서 걸린거임 ########");
//        log.info("########### 여기서 걸린거임 ########");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(front_url); // React 앱의 주소
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("Origin");
        configuration.addAllowedHeader("X-Requested-With");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedHeader("Key");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Authorization-Refresh");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");  // 헤더 노출 설정
        configuration.addExposedHeader("Authorization-Refresh");  // 헤더 노출 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, memberRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     */
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
        return jwtAuthenticationFilter;
    }

}
