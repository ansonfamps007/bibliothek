/*
 * package com.dlib.bibliothek.controller;
 * 
 * import java.io.BufferedReader; import java.io.IOException; import
 * java.io.InputStreamReader; import java.net.HttpURLConnection; import
 * java.net.MalformedURLException; import java.net.URL; import java.util.List;
 * import java.util.concurrent.ExecutionException; import
 * java.util.concurrent.ExecutorService; import java.util.concurrent.Executors;
 * import java.util.concurrent.Future;
 * 
 * import javax.naming.ServiceUnavailableException;
 * 
 * import org.json.JSONObject; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.scheduling.annotation.Scheduled; import
 * org.springframework.security.access.prepost.PreAuthorize; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.dlib.bibliothek.model.Book; import
 * com.dlib.bibliothek.model.BookReturnResponse; import
 * com.dlib.bibliothek.model.Language; import com.dlib.bibliothek.model.User;
 * import com.dlib.bibliothek.repository.BookRepository; import
 * com.dlib.bibliothek.repository.LanguageRepository; import
 * com.dlib.bibliothek.repository.UserRepository; import
 * com.dlib.bibliothek.util.BookUtil; import
 * com.microsoft.aad.adal4j.AuthenticationContext; import
 * com.microsoft.aad.adal4j.AuthenticationResult;
 * 
 * import lombok.extern.slf4j.Slf4j;
 * 
 * @RestController
 * 
 * @RequestMapping("/api/user")
 * 
 * @Slf4j public class TestRestAPIs {
 * 
 * @Autowired BookRepository bookRepository;
 * 
 * @Autowired UserRepository userRepository;
 * 
 * @Autowired LanguageRepository languageRepository;
 * 
 * @Autowired private BookUtil bookUtil;
 * 
 * @GetMapping("/api/test/user")
 * 
 * @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") public String
 * userAccess() { return ">>> User Contents!"; }
 * 
 * @GetMapping("/api/test/pm")
 * 
 * @PreAuthorize("hasRole('PM') or hasRole('ADMIN')") public String
 * projectManagementAccess() { return ">>> Board Management Project"; }
 * 
 * @GetMapping("/api/test/admin")
 * 
 * @PreAuthorize("hasRole('ADMIN')") public String adminAccess() { return
 * ">>> Admin Contents"; }
 * 
 * @GetMapping("/testApiBook") public List<BookReturnResponse>
 * testApiBook(@RequestParam int id) { return
 * bookRepository.findAllReturnAlert(10); }
 * 
 * private final static String AUTHORITY =
 * "https://login.microsoftonline.com/common/"; private final static String
 * CLIENT_ID = "829d8324-2ae3-40ba-a9ce-45b2473e57ef";
 * 
 * @GetMapping("/testPush") public void testApiWatch() { try (BufferedReader br
 * = new BufferedReader(new InputStreamReader(System.in))) { String username =
 * "anson.f@toshokan.onmicrosoft.com"; String password =
 * "xq0m8NGSQDTibX7uSvRgbK9b1Vd0a1q5";
 * 
 * // Request access token from AAD AuthenticationResult result =
 * getAccessTokenFromUserCredentials(username, password); // Get user info from
 * Microsoft Graph String userInfo =
 * getUserInfoFromGraph(result.getAccessToken()); System.out.println(userInfo);
 * } catch (IOException | InterruptedException | ExecutionException |
 * ServiceUnavailableException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } }
 * 
 * //@Scheduled(cron = "0 0 9,17   ")
 * 
 * @GetMapping("/testBlaBla") public List<User> testPushNot() throws
 * InterruptedException, ExecutionException {
 * 
 * return userRepository.findAllUserBooks();
 * 
 * }
 * 
 * private static AuthenticationResult getAccessTokenFromUserCredentials(String
 * username, String password) throws MalformedURLException,
 * InterruptedException, ExecutionException, ServiceUnavailableException {
 * AuthenticationContext context; AuthenticationResult result; ExecutorService
 * service = null; try { service = Executors.newFixedThreadPool(1); context =
 * new AuthenticationContext(AUTHORITY, false, service);
 * Future<AuthenticationResult> future =
 * context.acquireToken("https://graph.microsoft.com", CLIENT_ID, username,
 * password, null); result = future.get();
 * 
 * Future<AuthenticationResult> futureWithRefresh = context
 * .acquireTokenByRefreshToken(future.get().getRefreshToken(), CLIENT_ID, null);
 * AuthenticationResult result1 = futureWithRefresh.get();
 * System.out.println(result1); } finally { service.shutdown(); }
 * 
 * if (result == null) { throw new
 * ServiceUnavailableException("authentication result was null"); } return
 * result; }
 * 
 * private static String getUserInfoFromGraph(String accessToken) throws
 * IOException {
 * 
 * URL url = new URL("https://graph.microsoft.com/v1.0/me"); HttpURLConnection
 * conn = (HttpURLConnection) url.openConnection();
 * 
 * conn.setRequestMethod("GET"); conn.setRequestProperty("Authorization",
 * "Bearer " + accessToken); conn.setRequestProperty("Accept",
 * "application/json");
 * 
 * int httpResponseCode = conn.getResponseCode(); if (httpResponseCode == 200) {
 * BufferedReader in = null; StringBuilder response; try { in = new
 * BufferedReader(new InputStreamReader(conn.getInputStream())); String
 * inputLine; response = new StringBuilder(); while ((inputLine = in.readLine())
 * != null) { response.append(inputLine); } } finally { in.close(); } return
 * response.toString(); } else { return
 * String.format("Connection returned HTTP code: %s with message: %s",
 * httpResponseCode, conn.getResponseMessage()); } } }
 */