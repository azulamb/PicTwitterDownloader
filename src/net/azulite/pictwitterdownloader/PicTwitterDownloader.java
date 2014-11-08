package net.azulite.pictwitterdownloader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class PicTwitterDownloader
{
	JFrame frame;
	JTextArea srcad, imgad;
	DownloadPicture dp;

	public static void main( String[] args )
	{
		PicTwitterDownloader ptd = new PicTwitterDownloader();
		ptd.initGUI();
	}
	
	public PicTwitterDownloader()
	{
		dp = new DownloadPicture();
		dp.init();
	}
	public void initGUI()
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		frame.setLayout( new BorderLayout() );

		srcad = new JTextArea();
		imgad = new JTextArea();

		JPanel mainpanel = new JPanel();
		mainpanel.setLayout( new BoxLayout( mainpanel, BoxLayout.X_AXIS));//new GridLayout( 1, 3 ) );
		mainpanel.add( new JScrollPane( srcad ) );
		mainpanel.add( new LoadImgAddress(">>", srcad, imgad, dp ) );
		mainpanel.add( new JScrollPane( imgad ) );

		frame.add( mainpanel, BorderLayout.CENTER );
		frame.pack();
		frame.setVisible( true );
	}
}

class LoadImgAddress extends JButton implements ActionListener
{
	private static final long serialVersionUID = 1L;
	JTextArea srcad, imgad;
	DownloadPicture dp;
	public LoadImgAddress(String label, JTextArea in, JTextArea out, DownloadPicture dp)
	{
		super( label );
		this.dp = dp;
		this.srcad = in;
		this.imgad = out;
		this.addActionListener( this );
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String[] lines = srcad.getText().split( "\n" );
		imgad.setText( "" );
		String ads = "";
		String[] adlist;
		int i;
		boolean join;
		for ( i = 0 ; i < lines.length; ++i )
		{
			adlist = AnalysisPicTwitter.analysis( lines[i] );
			join = false;
			for(String ad: adlist)
			{
				if (join){ads+=",";}
				ads += ad;
				imgad.setText( ads );
				dp.download( ad );
				join = true;
			}
			ads += "\n";
		}
	}
}
