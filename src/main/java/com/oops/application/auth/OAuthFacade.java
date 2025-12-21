package com.oops.application.auth;

import com.oops.application.auth.model.OAuthUserInfoModel;
import com.oops.application.auth.model.command.OAuthSignUpCommand;
import com.oops.application.auth.model.result.AuthResult;
import com.oops.application.auth.model.result.OAuthCheckResult;
import com.oops.application.user.OAuthUserInquiryService;
import com.oops.application.user.OAuthUserRegisterService;
import com.oops.application.user.UserInquiryService;
import com.oops.application.user.UserRegisterService;
import com.oops.application.user.model.command.OAuthUserRegisterCommand;
import com.oops.application.user.model.command.UserRegisterCommand;
import com.oops.application.user.model.result.UserResult;
import com.oops.common.exception.ErrorCode;
import com.oops.common.exception.NotFoundException;
import com.oops.domain.user.model.vo.OAuthProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthFacade {

	private final GoogleOAuthService googleOAuthService;

	private final UserInquiryService userInquiryService;

	private final UserRegisterService userRegisterService;

	private final OAuthUserInquiryService oAuthUserInquiryService;

	private final OAuthUserRegisterService oAuthUserRegisterService;

	private final JwtTokenService jwtTokenService;

	@Transactional
	public AuthResult signUp(OAuthSignUpCommand command) {
		var oauthInfo = googleOAuthService.getOAuthUserInfo(command.accessToken());

		boolean isExists = oAuthUserInquiryService.existsByProviderAndOauthId(OAuthProvider.GOOGLE,
				oauthInfo.oauthId());

		var user = isExists
				? oAuthUserInquiryService.findByProviderAndOauthId(OAuthProvider.GOOGLE, oauthInfo.oauthId())
					.map(oauthUserResult -> userInquiryService.findById(oauthUserResult.userId()))
					.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_FOR_OAUTH_ERROR))
				: registerNewOAuthUser(oauthInfo);

		var tokens = jwtTokenService.generateTokens(user.id());

		return new AuthResult(user.id(), tokens);
	}

	private UserResult registerNewOAuthUser(OAuthUserInfoModel oauthInfo) {
		var userResult = userRegisterService.register(new UserRegisterCommand(oauthInfo.email(), oauthInfo.name()));

		oAuthUserRegisterService.register(
				new OAuthUserRegisterCommand(OAuthProvider.GOOGLE, oauthInfo.oauthId(), userResult.id()));

		return userResult;
	}

	public OAuthCheckResult checkSignUp(String provider, String accessToken) {
		var oauthProvider = OAuthProvider.from(provider);
		var oauthInfo = googleOAuthService.getOAuthUserInfo(accessToken);

		boolean exists = oAuthUserInquiryService.existsByProviderAndOauthId(oauthProvider, oauthInfo.oauthId());

		return new OAuthCheckResult(exists);
	}

}
