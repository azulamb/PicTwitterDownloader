package net.azulite.pictwitterdownloader;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DownloadPicture
{
	public void init()
	{
		File newdir = new File( "img" );
		if ( ! newdir.isDirectory() )
		{
			newdir.mkdir();
		}
	}
	public boolean download( String address )
	{
		if ( address == null ){ return false; }
		try
		{
			URL url = new URL( address );

			Pattern p = Pattern.compile( "http.+/(.+):large.*" );
			Matcher m = p.matcher( address );
			String filename;
			if ( ! m.matches() )
			{
				System.out.println("error");
				return false;
			}

			filename = "img" + File.separator + m.group( 1 );

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setAllowUserInteraction(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.connect();

			int httpStatusCode = conn.getResponseCode();
			if(httpStatusCode != HttpURLConnection.HTTP_OK){
				return true;
			}

			DataInputStream dataInStream = new DataInputStream( conn.getInputStream() );

			DataOutputStream dataOutStream = new DataOutputStream( new BufferedOutputStream( new FileOutputStream( filename )));

			// Read Data
			byte[] b = new byte[4096];
			int readByte = 0;

			while(-1 != (readByte = dataInStream.read(b))){
				dataOutStream.write(b, 0, readByte);
			}

			dataInStream.close();
			dataOutStream.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
