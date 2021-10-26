package com.contentlab.teams.java.channeltabsso;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import org.springframework.core.env.Environment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SSOAuthHelper
{
	/// <summary>
	/// Azure Client Id.
	/// </summary>
	public static final String ClientIdConfigurationSettingsKey = "azure.activedirectory.client-id";

	/// <summary>
	/// Azure Tenant Id.
	/// </summary>
	public static final String TenantIdConfigurationSettingsKey = "azure.activedirectory.tenant-id";

	/// <summary>
	/// Azure AppSecret .
	/// </summary>
	public static final String AppsecretConfigurationSettingsKey = "azure.activedirectory.client-secret";

	/// <summary>
	/// Azure Url .
	/// </summary>
	public static final String AzureInstanceConfigurationSettingsKey = "azure.instance";

	/// <summary>
	/// Azure Application Id URI.
	/// </summary>
	public static final String ApplicationIdURIConfigurationSettingsKey = "azure.api";

	/// <summary>
	/// Azure Scopes.
	/// </summary>
	public static final String AzureScopes = "azure.scopes";
	
	/// <summary>
	/// Azure Authorization Url .
	/// </summary>
	public static final String AzureAuthUrlConfigurationSettingsKey = "azure.auth.url";

    /// <summary>
    /// Azure Valid Issuers.
    /// </summary>
	public static final String ValidIssuersConfigurationSettingsKey = "azure.valid.issuers";
   
	public static String GetAccessTokenOnBehalfUser(Environment env, String assertion) throws Exception
	{
		String retval = null;

		try
		{
			String clientId = env.getProperty(ClientIdConfigurationSettingsKey); 
			String tenantId = env.getProperty(TenantIdConfigurationSettingsKey); 
			String clientSecret = env.getProperty(AppsecretConfigurationSettingsKey); 
			String issuerUrl = env.getProperty(AzureAuthUrlConfigurationSettingsKey);
			String instancerUrl = env.getProperty(AzureInstanceConfigurationSettingsKey);
			String azureScopes = env.getProperty(AzureScopes);
			
			String idToken = assertion.split(" ")[1];
			String body = "assertion=" + idToken 
					+ "&requested_token_use=on_behalf_of"
					+ "&grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer" 
					+ "&client_id=" + clientId + "@" + tenantId 
					+ "&client_secret=" + clientSecret + "&scope=" + azureScopes;

			byte[] out = body.getBytes(StandardCharsets.UTF_8);
			int length = out.length;

			URL url = new URL(instancerUrl + "/" + tenantId + issuerUrl);
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) con;
			http.setRequestMethod("POST"); // PUT is another valid option
			http.setRequestProperty("Accept", "application/json");
			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			http.setDoOutput(true);
			http.connect();

			OutputStream os = http.getOutputStream();
			os.write(out);
			os.close();

			if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				InputStream is = http.getInputStream();
				String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				is.close();
				
				ObjectMapper mapper = new ObjectMapper();
				JsonNode actualObj = mapper.readTree(responseBody);

				JsonNode accessToken = actualObj.get("access_token");
				retval = accessToken.asText();
			}
			else
			{
				try
				{
					InputStream is = http.getInputStream();
					retval = new String(is.readAllBytes(), StandardCharsets.UTF_8);
					is.close();
				}
				catch(Exception e)
				{
					try
					{
						InputStream errIs = http.getErrorStream();
						retval = new String(errIs.readAllBytes(), StandardCharsets.UTF_8);
						errIs.close();
					}
					catch(Exception ex)
					{
						retval += " Can't read either input or error stream: " + ex.getMessage();					
					}					
				}
				
				throw new Exception(retval);
			}
		}
		catch (Exception e)
		{
			retval = e.getMessage();
		}

		return retval;
	}
}
