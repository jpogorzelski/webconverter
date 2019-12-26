package pl.pogorzelski.webconverter.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import javax.inject.Inject;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/public/**", "/register/**", "/error/**").permitAll()//
                .antMatchers("/users/**").hasAuthority("ADMIN")//
                .anyRequest().fullyAuthenticated()//
                .and()//
                .formLogin()//
                .loginPage("/login")//
                .failureUrl("/login?error")//
                .usernameParameter("email")//
                .permitAll()//
                .and()//
                .logout()//
                .logoutUrl("/logout")//
                .deleteCookies("remember-me")//
                .logoutSuccessUrl("/")//
                .permitAll()//
                .and()//
                .rememberMe();//
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint()).accessDeniedPage("/403");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

}