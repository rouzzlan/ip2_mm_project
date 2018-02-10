package be.kdg.musicmaker.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.*;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        Collection<GrantedAuthority> authorities = authentication.getAuthorities();

        Object[] ga = authorities.toArray();
        List<String> roles = new ArrayList<String>();

        for (Object authority: ga)
        {
            SimpleGrantedAuthority sga = (SimpleGrantedAuthority) authority;
            String role = sga.getAuthority();
            roles.add(role);
        }
        additionalInfo.put("user_roles", roles);

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
