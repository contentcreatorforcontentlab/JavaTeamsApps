package com.contentlab.teams.java.personaltabsso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController
{
	@Autowired
	private Environment env;

	@GetMapping("/GetUserAccessToken")
	public String GetUserAccessToken(@RequestHeader("Authorization") String assertion) throws Exception
	{
		String retval = null;
		
		try
		{
			retval = SSOAuthHelper.GetAccessTokenOnBehalfUser(env, assertion);
		}
		catch (Exception e)
		{
			throw e;
		}

		return retval;
	}

}