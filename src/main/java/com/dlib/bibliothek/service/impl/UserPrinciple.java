/*
 * package com.dlib.bibliothek.service.impl;
 * 
 * import java.util.ArrayList; import java.util.Collection; import
 * java.util.List; import java.util.Objects;
 * 
 * import org.springframework.security.core.GrantedAuthority; import
 * org.springframework.security.core.authority.SimpleGrantedAuthority; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.stereotype.Service; import
 * org.springframework.util.StringUtils;
 * 
 * import com.dlib.bibliothek.model.User; import
 * com.fasterxml.jackson.annotation.JsonIgnore;
 * 
 * @Service public class UserPrinciple implements UserDetails {
 * 
 * private static final long serialVersionUID = 6384109567428029390L;
 * 
 * private Integer id;
 * 
 * private String name;
 * 
 * private String username;
 * 
 * @JsonIgnore private String password;
 * 
 * private Collection<? extends GrantedAuthority> authorities;
 * 
 * public UserPrinciple() { super(); }
 * 
 * public UserPrinciple(Integer id, String name, String username, String
 * password, Collection<? extends GrantedAuthority> authorities) { this.id = id;
 * this.name = name; this.username = username; this.password = password;
 * this.authorities = authorities; }
 * 
 * public static UserPrinciple build(User user) { List<GrantedAuthority> roles =
 * new ArrayList<>(); if (!StringUtils.isEmpty(user.getRole())) { roles.add(new
 * SimpleGrantedAuthority(user.getRole())); } return new
 * UserPrinciple(user.getId(), user.getName(), user.getUsername(),
 * user.getPassword(), roles); }
 * 
 * public Integer getId() { return id; }
 * 
 * public String getName() { return name; }
 * 
 * @Override public String getUsername() { return username; }
 * 
 * @Override public String getPassword() { return password; }
 * 
 * @Override public Collection<? extends GrantedAuthority> getAuthorities() {
 * return authorities; }
 * 
 * @Override public boolean isAccountNonExpired() { return true; }
 * 
 * @Override public boolean isAccountNonLocked() { return true; }
 * 
 * @Override public boolean isCredentialsNonExpired() { return true; }
 * 
 * @Override public boolean isEnabled() { return true; }
 * 
 * @Override public boolean equals(Object o) { if (this == o) return true; if (o
 * == null || getClass() != o.getClass()) return false;
 * 
 * UserPrinciple user = (UserPrinciple) o; return Objects.equals(id, user.id); }
 * }
 */