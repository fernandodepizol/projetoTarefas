package br.com.tarefas.security;


import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import br.com.tarefas.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
public class WebSecurityConfig {
	
	private static final String[] PATHS = new String[] {"/tarefa/**", "/categoria/**", "/usuario/**"};
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private AuthEntryPointJWT unauthorizedHandler;
	

		@Bean 
		public SecurityFilterChain securityFilterChain(HttpSecurity http,
									MvcRequestMatcher.Builder mvc) throws Exception { 
			http
			//.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable) // Desabilitar CSRF
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(authorize -> authorize
					.requestMatchers(mvc.pattern("/api/auth/**")).permitAll()
					.requestMatchers(mvc.pattern("/h2-console/**")).permitAll() //permite acesso ao h2Console
								.requestMatchers(mvc.pattern(HttpMethod.POST, "/tarefa/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.PUT, "/tarefa/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.DELETE, "/tarefa/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.GET, "/tarefa/**")).hasAnyRole("ADMIN", "USER")
										
								.requestMatchers(mvc.pattern(HttpMethod.POST, "/categoria/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.PUT, "/categoria/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.DELETE, "/categoria/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.GET, "/categoria/**")).hasAnyRole("ADMIN", "USER")
										
								
								.requestMatchers(mvc.pattern(HttpMethod.POST, "/usuario/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.PUT, "/usuario/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.DELETE, "/usuario/**")).hasRole("ADMIN")
								.requestMatchers(mvc.pattern(HttpMethod.GET, "/usuario/**")).hasAnyRole("ADMIN", "USER")
										
								
								.anyRequest().authenticated()
							)
				//.httpBasic(Customizer.withDefaults());
				  .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
				  .exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
		 return http.build(); 
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsServiceImpl)
		.passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	@Scope("prototype")
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080")); //de onde ele aceita as requisições
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
