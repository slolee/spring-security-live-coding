package com.example.dammpractice.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dammpractice.domain.MemberType;
import com.example.dammpractice.endpoint.model.MemberResponse;
import com.example.dammpractice.endpoint.model.RegisterRequest;
import com.example.dammpractice.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final MemberService memberService;
	private final OAuth2AttributeRepository oAuth2AttributeRepository;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
		// 1. DefaultOAuth2UserService 로 OAuth2 Provider(ex. kakao, naver..) 으로부터 사용자 정보를 읽어온다.
		OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = userService.loadUser(req);

		// 2. 읽어온 사용자정보를 나의 엔티티로 만든다.
		OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(req, oAuth2User);

		// 3. 아직 가입되어있지 않은 사용자(Email) 이라면 회원가입한다.
		MemberResponse member;
		if (memberService.existsByEmail(oAuth2Attribute.getEmail())) {
			member = memberService.retrieveMemberByEmail(oAuth2Attribute.getEmail());
		} else {
			RegisterRequest registerReq = new RegisterRequest(oAuth2Attribute.getEmail(), "", oAuth2Attribute.getNickname());
			MemberType memberType = MemberType.valueOf(oAuth2Attribute.getProvider().toUpperCase());
			member = memberService.register(registerReq, memberType);
			oAuth2AttributeRepository.save(oAuth2Attribute);
		}

		// 4. OAuth2User 구현체로 만들어 반환한다.
		return CustomOAuth2User.of(member.getMemberId(), oAuth2Attribute);
	}

}
