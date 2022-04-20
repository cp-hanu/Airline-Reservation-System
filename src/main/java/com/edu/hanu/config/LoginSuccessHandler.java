//package com.edu.hanu.config;
//
//import com.edu.hanu.model.Role;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.HashSet;
//
//
//@Configuration
//public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    @Override
//    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String targetUrl = determineTargetUrl(authentication);
//        if(response.isCommitted()){
//            return ;
//
//        }
//        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//        redirectStrategy.sendRedirect(request,response,targetUrl);
//
//    }
//
//    protected String determineTargetUrl(Authentication authentication){
//        String url = "/login?error=true";
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        HashSet<String> roles = new HashSet<>();
//        for(GrantedAuthority a : authorities){
//            roles.add(a.getAuthority());
//        }
//
//        if(roles.contains("ROLE_ADMIN")){
//            url = "/admin";
//        }else if(roles.contains("ROLE_CUSTOMER")){
//            url = "/home";
//        }
//        return url;
//    }
//
////    @Override
////    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
////        String targetUrl = "";
////        if(response.isCommitted()){
////            return ;
////
////        }
////        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
////        redirectStrategy.sendRedirect(request,response,targetUrl);
////    }
//}
//
