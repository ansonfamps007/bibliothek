/*
 * package com.dlib.bibliothek.util;
 * 
 * import java.io.BufferedReader; import java.io.InputStreamReader; import
 * java.net.HttpURLConnection; import java.net.MalformedURLException; import
 * java.net.URL; import java.time.LocalDateTime; import java.time.ZoneId; import
 * java.util.Date; import java.util.HashMap; import java.util.Map; import
 * java.util.Optional; import java.util.concurrent.ExecutionException; import
 * java.util.concurrent.ExecutorService; import java.util.concurrent.Executors;
 * import java.util.concurrent.Future; import java.util.function.Function;
 * 
 * import javax.naming.ServiceUnavailableException; import
 * javax.servlet.http.HttpServletRequest;
 * 
 * import org.springframework.beans.factory.annotation.Value; import
 * org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.stereotype.Service;
 * 
 * import com.fasterxml.jackson.core.type.TypeReference; import
 * com.fasterxml.jackson.databind.ObjectMapper; import
 * com.microsoft.aad.adal4j.AuthenticationContext; import
 * com.microsoft.aad.adal4j.AuthenticationResult;
 * 
 * import io.jsonwebtoken.Claims; import io.jsonwebtoken.ExpiredJwtException;
 * import io.jsonwebtoken.JwtException; import io.jsonwebtoken.Jwts; import
 * io.jsonwebtoken.SignatureAlgorithm; import lombok.extern.slf4j.Slf4j;
 * 
 * @Service
 * 
 * @Slf4j public class JwtUtil {
 * 
 * @Value("${jwt.secret}") private String secretKey;
 * 
 * @Value("${jwt.expiry}") private String expireDays;
 * 
 * private static final String AUTHORITY =
 * "https://login.microsoftonline.com/common/";
 * 
 * @Value("${jwt.client-id}") private String clientId;
 * 
 * public AuthenticationResult getTokenFromAzure(String userName, String
 * password) { // Request access token from AAD try { return
 * getAccessTokenFromUserCredentials(userName, password); } catch (Exception ex)
 * { log.debug("Error fetching access token - " + ex.getMessage() + " - " +
 * (null != ex.getCause() ? ex.getCause().getCause() : ex)); return null; }
 * 
 * // Get user info from Microsoft Graph // String userInfo =
 * getUserInfoFromGraph(result.getAccessToken()); }
 * 
 * public String getTokenFromRefreshToken(String token) throws
 * MalformedURLException, InterruptedException, ExecutionException,
 * ServiceUnavailableException {
 * 
 * AuthenticationContext context; AuthenticationResult result; ExecutorService
 * service = null; try { service = Executors.newFixedThreadPool(1); context =
 * new AuthenticationContext(AUTHORITY, false, service);
 * Future<AuthenticationResult> future =
 * context.acquireTokenByRefreshToken(token, clientId, null); result =
 * future.get(); } finally { if (null != service) { service.shutdown(); } }
 * 
 * if (null == result) { throw new
 * ServiceUnavailableException("Refresh token expired, please login"); } else {
 * return result.getAccessToken(); }
 * 
 * 
 * 
 * }
 * 
 * private AuthenticationResult getAccessTokenFromUserCredentials(String
 * username, String password) throws MalformedURLException,
 * InterruptedException, ExecutionException, ServiceUnavailableException {
 * AuthenticationContext context; AuthenticationResult result; ExecutorService
 * service = null; try { service = Executors.newFixedThreadPool(1); context =
 * new AuthenticationContext(AUTHORITY, false, service);
 * Future<AuthenticationResult> future =
 * context.acquireToken("https://graph.microsoft.com", clientId, username,
 * password, null); result = future.get(); } finally { if (null != service) {
 * service.shutdown(); } }
 * 
 * if (null == result) { throw new
 * ServiceUnavailableException("authentication result was null"); } return
 * result; }
 * 
 * public Map<String, Object> getDataFromToken(String accessToken) {
 * 
 * try { URL url = new URL("https://graph.microsoft.com/v1.0/me");
 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 * 
 * conn.setRequestMethod("GET"); conn.setRequestProperty("Authorization",
 * "Bearer " + accessToken); conn.setRequestProperty("Accept",
 * "application/json");
 * 
 * int httpResponseCode = conn.getResponseCode(); if (httpResponseCode == 200) {
 * StringBuilder response; BufferedReader in = new BufferedReader(new
 * InputStreamReader(conn.getInputStream()));
 * 
 * String inputLine; response = new StringBuilder(); while ((inputLine =
 * in.readLine()) != null) { response.append(inputLine); }
 * 
 * Optional<Object> optional = Optional.ofNullable( new
 * ObjectMapper().readValue(response.toString(), new TypeReference<Map<String,
 * Object>>() { })); if (optional.isPresent()) { return (Map<String, Object>)
 * optional.get(); }
 * 
 * } else { throw new
 * JwtException("JWT token/API key has been expired, please request again !"); }
 * } catch (Exception ex) {
 * log.debug("JwtUtil : getUserInfoFromGraph - Exception {} ", ex.getCause() +
 * " - " + (null != ex.getCause() ? ex.getCause().getCause() : ex)); } return
 * null; }
 * 
 * public String retriveUsernameFromJsonString(Map<String, Object>
 * mapAzureObject) { String userPrincipalName = null; if (null != mapAzureObject
 * && mapAzureObject.containsKey("userPrincipalName")) { userPrincipalName =
 * (String) mapAzureObject.get("userPrincipalName"); } return userPrincipalName;
 * 
 * }
 * 
 * public String getJwt(HttpServletRequest request) { final String
 * authorizationHeader = request.getHeader("Authorization"); if
 * (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
 * return authorizationHeader.substring(7); } return null; }
 * 
 * public boolean validateToken(String token, Map<String, Object>
 * mapAzureObject,UserDetails userDetails) { final String username =
 * retriveUsernameFromJsonString(mapAzureObject); return
 * (username.equalsIgnoreCase(userDetails.getUsername())); }
 * 
 * 
 * public Date extractExpiration(String token) { try { return
 * extractClaim(token, Claims::getExpiration); } catch (ExpiredJwtException ex)
 * { throw new
 * JwtException("JWT token/API key has been expired, please request again !"); }
 * }
 * 
 * public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
 * try { final Claims claims = extractAllClaims(token); return
 * claimsResolver.apply(claims); } catch (JwtException ex) { throw new
 * JwtException(ex.getMessage()); } }
 * 
 * private Claims extractAllClaims(String token) { try { return
 * Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody(); }
 * catch (Exception ex) { log.debug("extractAllClaims - {}", ex.getCause() +
 * " :- " + ex.getMessage()); throw new JwtException(ex.getMessage()); } }
 * 
 * 
 * public String generateToken(UserDetails userDetails) { Map<String, Object>
 * claims = new HashMap<>(); return createToken(claims,
 * userDetails.getUsername()); }
 * 
 * private String createToken(Map<String, Object> claims, String subject) { try
 * { LocalDateTime localDateTime = LocalDateTime.now(); Date today =
 * Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()); Date
 * expireDate = Date
 * .from(localDateTime.plusDays(Long.valueOf(expireDays)).atZone(ZoneId.
 * systemDefault()).toInstant()); return
 * Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(today).
 * setExpiration(expireDate) .signWith(SignatureAlgorithm.HS512,
 * secretKey).compact(); } catch (JwtException ex) { throw new
 * JwtException("JWT token creation failed !"); }
 * 
 * } }
 */