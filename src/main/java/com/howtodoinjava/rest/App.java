package com.howtodoinjava.rest;

import com.google.common.collect.ImmutableList;
import com.howtodoinjava.auth.BasicAuthenticator;
import com.howtodoinjava.auth.BasicAuthorizer;
import com.howtodoinjava.auth.OAuthAuthenticator;
import com.howtodoinjava.auth.User;
import com.howtodoinjava.dao.EmployeeDb;
import com.howtodoinjava.rest.controllers.EmployeeController;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application<Configuration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Override
    public void initialize(final Bootstrap<Configuration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(final Configuration configuration, final Environment e) throws Exception {
        e.jersey().register(new EmployeeController(new EmployeeDb()));
        //****** Dropwizard security - custom classes ***********/
        AuthFilter<BasicCredentials, User> basicAuthenticator = new BasicCredentialAuthFilter.Builder<User>()
            .setAuthenticator(new BasicAuthenticator())
            .setAuthorizer(new BasicAuthorizer())
            .setRealm("BASIC-AUTH-REALM")
            .buildAuthFilter();

        AuthFilter<String, User> oauthCredentialAuthFilter = new OAuthCredentialAuthFilter.Builder<User>()
            .setAuthenticator(new OAuthAuthenticator())
//            .setAuthorizer(new ExampleAuthorizer())
            .setPrefix("Bearer")
            .buildAuthFilter();

        e.jersey().register(new AuthDynamicFeature(new ChainedAuthFilter<>(
            ImmutableList.of(basicAuthenticator, oauthCredentialAuthFilter)
        )));
        e.jersey().register(RolesAllowedDynamicFeature.class);
        e.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
