package com.example.dammpractice.security.oauth;

import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "oauth_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OAuth2Attribute {

	@Id
	@Column(name = "oauth_id")
	private Long id;

	@Transient
	private String attributeKey;

	@Transient
	private Map<String, Object> attributes;

	private String provider; // kakao, naver, google
	private String email;
	private String nickname;

	@Builder
	public OAuth2Attribute(
		Long id,
		String provider,
		String email,
		String nickname,
		String attributeKey,
		Map<String, Object> attributes
	) {
		this.id = id;
		this.provider = provider;
		this.email = email;
		this.nickname = nickname;
		this.attributeKey = attributeKey;
		this.attributes = attributes;
	}

	public static OAuth2Attribute of(OAuth2UserRequest req, OAuth2User user) {
		String registrationId = req.getClientRegistration().getRegistrationId();
		String attributeName = req.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		OAuth2Attribute oAuth2Attribute = null;
		if (registrationId.equals("kakao")) {
			oAuth2Attribute = ofKakao(user, registrationId, attributeName);
		}
		return oAuth2Attribute;
	}

	public static OAuth2Attribute ofKakao(OAuth2User user, String provider, String attributeKey) {
		Map<String, Object> attributes = user.getAttributes();

		Map<String, Object> profile = (Map<String, Object>) attributes.get("properties");
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

		String nickname = Optional.ofNullable((String) profile.get("nickname")).orElse("");
		String email = Optional.ofNullable((String) account.get("email")).orElse("");

		return OAuth2Attribute.builder()
			.id(user.getAttribute(attributeKey))
			.attributeKey(attributeKey)
			.attributes(attributes)
			.provider(provider)
			.email(email)
			.nickname(nickname)
			.build();
	}
}
