package com.cenoa.authentication.auth;

import com.cenoa.authentication.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity register(
      @RequestBody RegisterRequest request
  ) {
    try {
      return ResponseEntity.ok(service.register(request));
    } catch (Exception exception) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This email has been already registered.");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }
  @GetMapping("/me")
  public ResponseEntity me(Principal principal) {
    return ResponseEntity.ok(principal.getName());
  }

  @GetMapping("/demo")
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello from secured endpoint");
  }

  @GetMapping("/validate_token")
  public ResponseEntity<ConnValidationResponse> validateGet(Principal principal) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    System.out.println("--------------------------------------------------------------");
    User jwtUser = (User) auth.getPrincipal();
    //Get the username of the logged-in user: getPrincipal()
    System.out.println("auth.getPrincipal()=>"+jwtUser.getUsername() );
    //Get the password of the authenticated user: getCredentials()
    System.out.println("auth.getCredentials()=>"+auth.getCredentials());
    //Get the assigned roles of the authenticated user: getAuthorities()
    System.out.println("auth.getAuthorities()=>"+auth.getAuthorities());
    //Get further details of the authenticated user: getDetails()
    System.out.println("auth.getDetails()=>"+auth.getDetails());
    System.out.println("--------------------------------------------------------------");

    String username = (String) principal.getName();
    String token = (String) "sdfsdfds";
    List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) auth.getAuthorities();
    return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.GET.name())
            .username(username).token(token).authorities(grantedAuthorities)
            .isAuthenticated(true).build());
  }

}
