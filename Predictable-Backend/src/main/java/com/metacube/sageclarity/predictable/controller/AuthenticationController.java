package com.metacube.sageclarity.predictable.controller;

import com.metacube.sageclarity.predictable.config.JWTConfig.TokenProvider;
import com.metacube.sageclarity.predictable.entity.User;
import com.metacube.sageclarity.predictable.enums.ExceptionType;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;
import com.metacube.sageclarity.predictable.service.UserService;
import com.metacube.sageclarity.predictable.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/token")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/generate-token", method = RequestMethod.POST)
    public ResponseEntity<ResponseObject> register(@RequestBody LoginUser loginUser,HttpServletRequest request) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);

        Principal principal = request.getUserPrincipal();
        User user = null;
        try {
            user = userService.getByUserName(principal.getName());
            UserLoginVO userLoginVO = new UserLoginVO(principal);
            if(user!=null){
                userLoginVO.setUserVO(new UserVO(user));
            }
            userLoginVO.setToken(token);
            return new ResponseEntity(ResponseObject.getResponse(userLoginVO),HttpStatus.OK);
        } catch (ApplicationLevelException e) {
            //logger.error(e.getMessage(), e);
            return new ResponseEntity(ResponseObject.getResponse(ExceptionType.GENERAL_ERROR.getMessage()
                    , ExceptionType.GENERAL_ERROR.getCode()),HttpStatus.UNAUTHORIZED);
        }

       // return ResponseEntity.ok(new AuthToken(token));
    }

}
