package com.example.tenniscourtreservation.model;
import lombok.Data;

@Data
public class User {
//public class User implements UserDetails{

    private Long id;
    private String username;
    private String password;
    private String role;

    public static boolean isLogged = false;

    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

//    public void setPassword(String password) {
//        this.password = password;
//        this.password = passwordEncoder().encode(password);
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(new SimpleGrantedAuthority(role));
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    @InjectService
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    public static String getUserName() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        String userName = null;
//        if (authentication != null) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            userName = userDetails.getUsername();
//        }
//        return userName;
//    }
}
