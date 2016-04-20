package com.sherwin.baidu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * HTTP客户端简单实现
 * 
 * @author caihualin
 * 
 */
public class MultiThreadedHttpClient {
  public static final String HTTP_METHOD_GET = "GET";
  public static final String HTTP_METHOD_POST = "POST";
  public static final String HTTP_METHOD_PUT = "PUT";
  public static final String HTTP_METHOD_DELETE = "DELETE";

  public static final String MIME_TYPE_JSON = "application/json";
  public static final String DEFAULT_REQUEST_HEADER_CONTENT_TYPE = MIME_TYPE_JSON;
  public static final String DEFAULT_REQUEST_HEADER_ACCEPT = MIME_TYPE_JSON;

  private static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 100; // 默认每主机最大连接数
  private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 200; // 默认总共最大连接数

  private String hostname;

  private int port = 8082;

  private String webAppName = null;

  private String username;

  private String password;

  private MultiThreadedHttpConnectionManager manager;

  private int connectionTimeout;

  private int soTimeout;

  public MultiThreadedHttpClient( String hostname, int port, String username, String password ) {
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.password = password;
    this.manager = new MultiThreadedHttpConnectionManager();

    setConnManagerParams( manager.getParams() );
  }

  public MultiThreadedHttpClient( String hostname ) {
    this( hostname, 8082, null, null );
  }

  public String executeHttpGet( String service ) throws IOException {
    String url = constructUrl( service );

    HttpClient client = createHttpClient();

    HttpMethod getMethod = new GetMethod( url );
    getMethod.addRequestHeader( "accept", DEFAULT_REQUEST_HEADER_ACCEPT );

    InputStream inputStream = null;
    try {
      int statusCode = client.executeMethod( getMethod );

      inputStream = getMethod.getResponseBodyAsStream();
      String body = readStream( inputStream );

      checkStatusCode( statusCode, body );

      return body;
    } finally {
      getMethod.releaseConnection();
    }
  }

  private void checkStatusCode( int statusCode, String body ) throws IOException {
    if ( statusCode < 300 ) {
      return;
    }

    String message = null;

    if ( statusCode == 401 ) {
      message = "认证出错，出错信息：\n\r\n\r";
    } else {
      message = "请求异常，出错信息：\n\r\n\r";
    }

    throw new IOException( message + body );
  }

  public byte[] executeHttpGetBytes( String service ) throws IOException {
    String url = constructUrl( service );

    HttpClient client = createHttpClient();

    HttpMethod getMethod = new GetMethod( url );
    getMethod.addRequestHeader( "accept", DEFAULT_REQUEST_HEADER_ACCEPT );

    InputStream inputStream = null;
    try {
      int statusCode = client.executeMethod( getMethod );

      inputStream = getMethod.getResponseBodyAsStream();

      byte[] bytes = IOUtils.toByteArray( inputStream );

      if ( statusCode == 200 ) {
        return bytes;
      } else {
        throw new RuntimeException( "Execute HTTP Get Bytes Fail, statusCode: " + statusCode );
      }
    } finally {
      getMethod.releaseConnection();
    }
  }

  public String executeHttpPost( String service, String json ) throws IOException {
    return executeHttpPost( service, json, DEFAULT_REQUEST_HEADER_CONTENT_TYPE );
  }

  private String executeHttpPost( String service, String bodyStr, String contentType ) throws IOException {
    String url = constructUrl( service );

    HttpClient client = createHttpClient();

    PostMethod postMethod = new PostMethod( url );
    postMethod.setRequestEntity( new ByteArrayRequestEntity( bodyStr.getBytes( CharEncoding.UTF_8 ) ) );

    postMethod.setDoAuthentication( true );
    postMethod.addRequestHeader( "Content-Type", contentType + ";charset=UTF-8" );
    postMethod.addRequestHeader( "accept", DEFAULT_REQUEST_HEADER_ACCEPT );

    InputStream inputStream = null;
    try {
      int statusCode = client.executeMethod( postMethod );

      inputStream = postMethod.getResponseBodyAsStream();
      String body = readStream( inputStream );

      checkStatusCode( statusCode, body );

      return body;
    } finally {
      postMethod.releaseConnection();
    }
  }

  public String executeHttpPut( String service, Map<String, String> params ) throws IOException {
    return executeHttp( HTTP_METHOD_PUT, service, params );
  }

  public String executeHttpDelete( String service, Map<String, String> params ) throws IOException {
    return executeHttp( HTTP_METHOD_DELETE, service, params );
  }

  private void setConnManagerParams( HttpConnectionManagerParams params ) {
    params.setDefaultMaxConnectionsPerHost( DEFAULT_MAX_CONNECTIONS_PER_HOST );
    params.setMaxTotalConnections( DEFAULT_MAX_TOTAL_CONNECTIONS );
  }

  private String constructUrl( String serviceAndArguments ) {
    boolean hasWebAppName = StringUtils.isNotEmpty( webAppName );
    if ( hasWebAppName ) {
      serviceAndArguments = "/" + webAppName + serviceAndArguments;
    }

    String retval = "http://" + hostname + ":" + port + serviceAndArguments;

    return retval;
  }

  private HttpClient createHttpClient() {
    resetConnManagerParams();

    HttpClient client = new HttpClient( manager );

    // addCredentials( client );

    return client;
  }

  private void resetConnManagerParams() {
    if ( connectionTimeout != 0 ) {
      manager.getParams().setConnectionTimeout( connectionTimeout );
    }

    if ( soTimeout != 0 ) {
      manager.getParams().setSoTimeout( soTimeout );
    }
  }

	private void addCredentials(HttpClient client) {
    boolean hasWebAppName = StringUtils.isNotEmpty( webAppName );

    AuthScope scope = hasWebAppName ? AuthScope.ANY : new AuthScope( hostname, port, "AETL" );

    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials( username, password );

    client.getState().setCredentials( scope, credentials );

    if ( hasWebAppName )
      client.getParams().setAuthenticationPreemptive( true );
  }

  private String readStream( InputStream inputStream ) throws IOException {
    if ( inputStream == null ) {
      return null;
    }

    StringBuilder builder = new StringBuilder();

    InputStreamReader sr = new InputStreamReader( inputStream, CharEncoding.UTF_8 );
    BufferedReader reader = new BufferedReader( sr );

    String line = null;
    while ( ( line = reader.readLine() ) != null ) {
      builder.append( line + "\n" );
    }
    return builder.toString();
  }

  private String executeHttp( String httpMethod, String service, Map<String, String> params ) throws IOException {
    String url = constructUrl( service );

    HttpClient client = createHttpClient();

    HttpMethodBase method = getMethod( httpMethod, url );

    NameValuePair[] valuePairs = new NameValuePair[params.size()];
    int i = 0;
    for ( Entry<String, String> entry : params.entrySet() ) {
      valuePairs[( i++ )] = new NameValuePair( entry.getKey(), entry.getValue() );
    }
    method.setQueryString( valuePairs );
    method.setDoAuthentication( true );

    InputStream inputStream = null;
    try {
      int statusCode = client.executeMethod( method );

      inputStream = method.getResponseBodyAsStream();
      String body = readStream( inputStream );

      checkStatusCode( statusCode, body );

      return body;
    } finally {
      method.releaseConnection();
    }
  }

  private HttpMethodBase getMethod( String httpMethod, String url ) {
    if ( httpMethod.equals( HTTP_METHOD_POST ) )
      return new PostMethod( url );
    if ( httpMethod.equals( HTTP_METHOD_PUT ) )
      return new PutMethod( url );
    if ( httpMethod.equals( HTTP_METHOD_DELETE ) ) {
      return new DeleteMethod( url );
    }
    throw new RuntimeException( "http method[" + httpMethod + "] is error" );
  }

  public String getHostname() {
    return this.hostname;
  }

  public void setHostname( String hostname ) {
    this.hostname = hostname;
  }

  public int getPort() {
    return this.port;
  }

  public void setPort( int port ) {
    this.port = port;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername( String username ) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword( String password ) {
    this.password = password;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout( int connectionTimeout ) {
    this.connectionTimeout = connectionTimeout;
  }

  public int getSoTimeout() {
    return soTimeout;
  }

  public void setSoTimeout( int soTimeout ) {
    this.soTimeout = soTimeout;
  }
}
