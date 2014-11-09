package net.azulite.pictwitterdownloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AnalysisPicTwitter
{
	public static String[] analysis( String address )
	{
		try {
			URL url = new URL( address );
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setAllowUserInteraction( false );
			conn.setInstanceFollowRedirects( true );
			conn.setRequestMethod( "GET" );
			conn.connect();

			int httpStatusCode = conn.getResponseCode();
			switch( httpStatusCode )
			{
			case HttpURLConnection.HTTP_OK:
				break;
			case 301:
			case HttpURLConnection.HTTP_MOVED_TEMP:
			//case HttpURLConnection.HTTP_MOVED_TEMP:
			case HttpURLConnection.HTTP_SEE_OTHER:
				return analysis( conn.getHeaderField( "Location" ));
			default:
				return new String[0];
			}

			HashMap<String,Integer> add = new HashMap<String,Integer>();
			BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
			String line = "";
			Pattern p = Pattern.compile( ".*(data-url|video-src)=\"(https://pbs.twimg.com/.+?/.+?)(:large)*\".*" );
			Pattern t = Pattern.compile( "(mp4)$" );
			Matcher m;
			while ( (line = br.readLine() ) != null)
			{
				m = p.matcher( line );
				if (m.matches())
				{
					line = m.group( 2 );
					m = t.matcher( line );
					if ( ! m.matches() )
					{
						line += ":large";
					}
					System.out.println( line );
					add.put( line, 0 );
				}
			}
			br.close();

			if ( add.size() <= 0  ){ return new String[0];}
			ArrayList<String> list = new ArrayList<String>();
			for ( String key : add.keySet() )
			{
				list.add( key );
			}
			return (String[]) list.toArray(new String[0]);
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return new String[0];
	}
	
}
