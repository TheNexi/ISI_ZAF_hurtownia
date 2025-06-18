package com.example.hurtownia_ISI_ZAF.service;

import com.example.hurtownia_ISI_ZAF.repository.UzytkownicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CustomOAuth2UserServiceTest {

    @Mock
    private UzytkownicyRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OAuth2UserRequest userRequest;

    @Mock
    private OAuth2User oAuth2User;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ClientRegistration.ProviderDetails.UserInfoEndpoint userInfoEndpoint = mock(ClientRegistration.ProviderDetails.UserInfoEndpoint.class);
        when(userInfoEndpoint.getUri()).thenReturn("https://dummyuserinfo.endpoint");

        ClientRegistration.ProviderDetails providerDetails = mock(ClientRegistration.ProviderDetails.class);
        when(providerDetails.getUserInfoEndpoint()).thenReturn(userInfoEndpoint);

        ClientRegistration clientRegistration = mock(ClientRegistration.class);
        when(clientRegistration.getProviderDetails()).thenReturn(providerDetails);

        when(userRequest.getClientRegistration()).thenReturn(clientRegistration);
    }

    @Test
    void loadUser_ThrowsOAuth2AuthenticationException() {
        CustomOAuth2UserService spyService = Mockito.spy(customOAuth2UserService);
        doThrow(new OAuth2AuthenticationException("Error")).when(spyService).loadUserFromDelegate(any());

        assertThrows(OAuth2AuthenticationException.class, () -> {
            spyService.loadUser(userRequest);
        });
    }
}
