package com.storefront.config;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.util.List;
import com.storefront.entities.Providers;
import com.storefront.entities.User;
import com.storefront.helpers.AppConstants;
import com.storefront.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenicationSuccessHandler");

        // identify the provider

        var oauth2AuthenicationToken = (OAuth2AuthenticationToken) authentication;
        

        String authorizedClientRegistrationId = oauth2AuthenicationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info(key + " : " + value);
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROlE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("dummy");

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            // google
            // google attributes

            // user.setName(oauthUser.getAttribute("name").toString());

            // user.setEmail(oauthUser.getAttribute("email").toString());

            // user.setFirstName(oauthUser.getAttribute("given_name").toString());
            // user.setLastName(oauthUser.getAttribute("family_name").toString());


            user.setEmail(oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString() : "unknown@example.com");

            user.setFirstName(oauthUser.getAttribute("given_name") != null ? oauthUser.getAttribute("given_name").toString() : "Unknown");

            user.setLastName(oauthUser.getAttribute("family_name") != null ? oauthUser.getAttribute("family_name").toString() : "Unknown");

            user.setName(user.getFirstName()+ " "+ user.getLastName());

            // user.setDOB(oauthUser.getAttribute("birthday"));

            user.setDOB((oauthUser.getAttribute("birthday") != null ? LocalDate.parse(oauthUser.getAttribute("birthday")) : LocalDate.parse("2000-01-01")));    
            



            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            // github
            // github attributes
            // String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
            //         : oauthUser.getAttribute("login").toString() + "@gmail.com";
            // String picture = oauthUser.getAttribute("avatar_url").toString();
            // String name = oauthUser.getAttribute("login").toString();
            // String providerUserId = oauthUser.getName();

            // user.setEmail(email);
            // user.setProfilePicLink(picture);
            // user.setName(name);
            // user.setProviderUserid(providerUserId);
            // user.setProvider(Providers.GITHUB);

            // user.setAbout("This account is created using github");
        }

        else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {

        }

        else {
            logger.info("OAuthAuthenicationSuccessHandler: Unknown provider");
        }


        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null) {
            userRepo.save(user);
            System.out.println("user saved:" + user.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/store/home");

    }

}


    



