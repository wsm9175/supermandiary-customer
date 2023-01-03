package com.lodong.spring.supermandiarycustomer.service;


import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.dto.jwt.TokenRequestDTO;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.jwt.TokenInfo;
import com.lodong.spring.supermandiarycustomer.repository.UserCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserCustomerRepository userCustomerRepository;


    @Transactional
    public void registerCustomer(UserCustomer user) throws IllegalStateException, Exception {
        if (isDuplicatedConstructor(user.getEmail())) throw new IllegalStateException("이메일 중복값 존재");
        try {
            userCustomerRepository.save(user);
        }catch (NullPointerException nullPointerException){
            System.out.println(nullPointerException.getMessage());
            throw new NullPointerException("빈 값 존재");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("데이터 베이스 저장 실패");
        }
        //채팅관련 유저 등록 코드 포함 예정 using savedUser
    }

    @Transactional
    public TokenInfo authCustomer(UserCustomer user) {
        log.info("UsernamePasswordAuthenticationToken");
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword());
        log.info("Authentication");
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("TokenInfo");
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        log.info("return");
        return tokenInfo;
    }

    @Transactional
    public TokenInfo reissueForCustomer(TokenRequestDTO tokenRequestDTO) throws NullPointerException {
        // 만료된 refresh token 에러
        if(!jwtTokenProvider.validateToken(tokenRequestDTO.getRefreshToken())){
            throw new NullPointerException("유효기간지남");
        }

        // AccessToken 에서 UserName(pk) 가져오기
        String accessToken = tokenRequestDTO.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        //user pk로 유저 검색 / repo에 저장된 refresh token이 없으면 에러
        UserCustomer userCustomer = userCustomerRepository.findById(authentication.getName())
                .orElseThrow(NullPointerException::new);

        String refreshToken = userCustomer.getRefreshToken();

        if(refreshToken == null){
            throw new NullPointerException("회원정보에 refreshToken 존재 X");
        }

        //리프레시 토큰 불일치 에러
        if(!refreshToken.equals(tokenRequestDTO.getRefreshToken())){
            throw new RuntimeException("회원정보의 refreshToken과 불일치");
        }

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        insertRefreshTokenForCustomer(tokenInfo.getRefreshToken(), userCustomer.getPhoneNumber());

        return tokenInfo;
    }

    public boolean isDuplicatedConstructor(String email) {
        return userCustomerRepository.existsByEmail(email);
    }

    public void insertRefreshTokenForCustomer(String refreshToken, String phoneNumber) {
        userCustomerRepository.insertRefreshToken(refreshToken, phoneNumber);
    }
}

