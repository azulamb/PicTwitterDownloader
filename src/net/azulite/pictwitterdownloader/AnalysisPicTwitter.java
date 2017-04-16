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
		try
		{
			URL url = new URL( address.replace( "mobile.twitter.com", "twitter.com" ) );
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
			String line = "", type = "", durl = "";
			Pattern p = Pattern.compile( ".*data-image-url=\"(https://pbs.twimg.com/.+?/.+?)(:large)*\".*" );
			Pattern v = Pattern.compile( ".*background-image:url\\('https://pbs.twimg.com/([^/]+)/(.*)\\.jpg.*" );
			Pattern t = Pattern.compile( ".+mp4$" );
			Matcher m;

			while ( (line = br.readLine() ) != null )
			{

				m = v.matcher( line );
				if ( m.matches() ){
					type = m.group( 1 );
					if ( type.equals( "tweet_video_thumb" ) ){
						//add.clear();
						durl = "https://pbs.twimg.com/tweet_video/" + m.group( 2 ) + ".mp4";
						System.out.println( durl );
						add.put( durl, 0 );
						//break;
					}
				}

				m = p.matcher( line );
				if ( m.matches() )
				{
					durl = m.group( 1 );
					m = t.matcher( durl );
					if ( ! m.matches() )
					{
						durl += ":large";
						System.out.println( durl );
						add.put( durl, 0 );
					}
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
