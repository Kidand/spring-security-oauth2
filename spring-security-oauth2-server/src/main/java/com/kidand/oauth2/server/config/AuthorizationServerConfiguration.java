package com.kidand.oauth2.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer//开启授权服务
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Bean
    @Primary//主要配置，覆盖默认配置
    @ConfigurationProperties(prefix = "spring.datasource")//指定配置
    public DataSource dataSource(){
        //JDBC数据源
        return DataSourceBuilder.create().build();
    }

    @Bean
    public TokenStore tokenStore(){
        //将token放在数据库中
        return new JdbcTokenStore(dataSource());
    }

    @Bean
    public ClientDetailsService jdbcClientDeatails(){
        //从数据库中读取客户端配置
        return new JdbcClientDetailsService(dataSource());
    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
//        userDetailsService.createUser(User.withUsername("user_1").password(passwordEncoder.encode("123456"))
//                .authorities("ROLE_USER").build());
//        userDetailsService.createUser(User.withUsername("user_2").password(passwordEncoder.encode("123456"))
//                .authorities("ROLE_USER").build());
//        return userDetailsService;
//    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients
//                .inMemory()
//                .withClient("client")
//                .secret(passwordEncoder.encode("secret"))
//                .authorizedGrantTypes("authorization_code")//授权模式
//                .scopes("app")//授权范围
//                .redirectUris("http://www.kadind.cn");

        clients.withClientDetails(jdbcClientDeatails());//客户端配置
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore());//端点配置
        endpoints.authenticationManager(authenticationManager);//Spring security5中新增加了加密方式，并把原有的spring security的密码存储格式改了
    }

    @Autowired
    private AuthenticationManager authenticationManager;
}
