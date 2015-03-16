package zenproject.meditation.android;


import android.annotation.SuppressLint;
import android.app.Activity;

@SuppressLint("NewApi")
public class ZenMeditation extends Activity {   /**
	//Button Action Constants
	static final int INK = 0;
	static final int WATER = 1;
	static final int SHARE = 2;
	static final int CLEAN = 3;
	static final int INFO = 4;

	//Button Coordinates
	int inkInitX;
	int inkInitY;
	int shareInitX;
	int shareInitY;
	int waterInitX;
	int waterInitY;
	int resetInitX;
	int resetInitY;
	int infoInitX;
	int infoInitY;

	//Canvas coordinates
	int canvasAHeight;
	int canvasBHeight;
	int buttonHeight;
	int infoHeight;

	//Ink attributes
	float radius = 1;
	int paintCounter=0;
	float maxAccount = 50000;
	float inkAccount = maxAccount;
	float x, y, xD, yD, xVel, yVel,xOld, yOld;
	float spring = 0.1f;
	float damp = 0.6f;

	//Sound attributes
	float volumeCounter = 0.0f;
	MediaPlayer mp;

	//Images
	PImage water;
	PImage ink;
	PImage reset;
	PImage share;
	PImage texture;
	PImage canvas;
	PImage canvasdown;
	PImage info;
	PImage information;
	PGraphics backupCanvas; //To backup the canvas at information show.

	//Actions variables.
	boolean first = true;
	boolean remainPainting = false;
	boolean isPainting = false;
	boolean isCleaning = false;
	boolean isInformating = false;
	boolean isRestoring = false;
	boolean infoShowed = false;
	int cleaningLevel=20; //To know when the sketch is cleaned

	//The branches drawer.
	ArrayList<BranchDrawer> brancDrawers = new ArrayList<BranchDrawer>();

	//The drawing handler

	DrawingHandler drawHandler;
	//This is done when the app is launched (super.onCreate())
	public void setup()
	{
		orientation(LANDSCAPE);  // the hot dog way
		smooth(4);
		frameRate(60);
		backupCanvas = createGraphics(width,height);
		image(canvas,0,0,width,canvasAHeight);
		image(canvasdown,0,canvasAHeight,width,canvasBHeight);
		image(ink,inkInitX,inkInitY,buttonHeight,buttonHeight);
		image(share,shareInitX,shareInitY,buttonHeight,buttonHeight);
		image(water,waterInitX,waterInitY,buttonHeight,buttonHeight);
		image(reset,resetInitX,resetInitY,buttonHeight,buttonHeight);
		image(info,infoInitX, infoInitY, infoHeight, infoHeight);
	}

	//This is the main loop.
	public void draw()
	{
		if(isCleaning && !isInformating && !isRestoring)
		{
			drawHandler.drawCleaning();
		}
		else if(!isCleaning && isInformating && !isRestoring)
		{
			drawHandler.drawInformation();
		}
		else if(!isCleaning && !isInformating && isRestoring)
		{
			drawHandler.drawRestoring();
		}
		else if(!isPainting && mousePressed  && getButtonPressed() == INK)
		{
			increaseInkLevel(false);
		}
		else if(!isPainting && mousePressed  && getButtonPressed() == WATER)
		{
			decreaseInkLevel(false);
		}
		else
		{
			drawHandler.drawPainting();
			drawHandler.drawBranches();
		}

		if(!mousePressed)
		{
			increaseInkLevel(true);
		}

		drawHandler.drawInkContainer();
		updateVolume();
	}

	//Which button was pressed?
	private int getButtonPressed()
	{
		if(mouseX > inkInitX && mouseX<(inkInitX+buttonHeight) && mouseY>inkInitY && mouseY <(inkInitY+buttonHeight))
		{
			return INK;
		}
		else if(mouseX > waterInitX && mouseX<(waterInitX+buttonHeight) && mouseY>waterInitY && mouseY <(waterInitY+buttonHeight))
		{
			return WATER;
		}
		else if(mouseX > shareInitX && mouseX<(shareInitX+buttonHeight) && mouseY>shareInitY && mouseY <(shareInitY+buttonHeight))
		{
			return SHARE;
		}
		else if(mouseX > resetInitX && mouseX<(resetInitX+buttonHeight) && mouseY>resetInitY && mouseY <(resetInitY+buttonHeight))
		{
			return CLEAN;
		}
		else if(mouseX > infoInitX && mouseX<(infoInitX+buttonHeight) && mouseY>infoInitY && mouseY <(infoInitY+buttonHeight))
		{
			return INFO;
		}
		return -1;
	}

	//Updates the volume
	private void updateVolume()
	{
		if(isPainting && inkAccount>0 && !remainPainting)
		{
			volumeCounter+=.02f;
			volumeCounter=volumeCounter>1f?1f:volumeCounter;
			mp.setVolume(volumeCounter, volumeCounter);
		}
		else
		{
			volumeCounter-=brancDrawers.size()>0?0.005:0.0075f;
			volumeCounter=volumeCounter<0.0f?0.0f:volumeCounter;
			mp.setVolume(volumeCounter, volumeCounter);
		}
	}


	//Action when water button is pressed.
	private void decreaseInkLevel(boolean auto)
	{
		if(!remainPainting)
		{
			if(auto)
			{
				inkAccount=inkAccount<100?1:inkAccount/1.01f;
			}
			else
			{
				inkAccount=inkAccount<100?1:inkAccount/1.05f;
			}
			inkAccount=inkAccount>maxAccount?maxAccount:inkAccount;
		}
	}

	//Action when ink button is pressed.
	private void increaseInkLevel(boolean auto)
	{
		if(!remainPainting)
		{
			if(auto)
			{
				inkAccount=inkAccount<2500?2500:inkAccount*1.01f;
			}
			else
			{
				inkAccount=inkAccount<2500?2500:inkAccount*1.05f;
			}
			inkAccount=inkAccount>maxAccount?maxAccount:inkAccount;
		}
	}
	//Actions on mousePressed
	public void mousePressed()
	{

		if(getButtonPressed()==SHARE || getButtonPressed()==INK || getButtonPressed()==WATER || getButtonPressed()==CLEAN || getButtonPressed()==INFO )
		{
			  remainPainting=false;
			  radius=1;
			  x=-1;
			  y=-1;
			  xOld=-1;
			  yOld=-1;
		}
		//Share button
		if( getButtonPressed()==SHARE)
		{
			doShareSketch();
		}
		//Clean button
		else if(!isRestoring && !isInformating && !isCleaning && !isPainting && getButtonPressed()==CLEAN)
		{
			remainPainting=false;
			isPainting=false;
			isInformating = false;
			isRestoring = false;
			cleaningLevel=20;
			isCleaning = true;
			inkAccount=maxAccount;
			radius=1;
			paintCounter=0;
			brancDrawers.clear();
		}
		//When pressing info button
		else if(getButtonPressed()==INFO && !isCleaning && !isRestoring && !isInformating)
		{
			if(isInformating && !isRestoring && !isCleaning)
			{
				isInformating=false;
				isRestoring=true;
			}
			else if(isRestoring && !isCleaning && !isInformating)
			{
				isInformating=true;
				isRestoring=false;
			}
			else //no animation running
			{
				if(infoShowed) //info is showed
				{
					isInformating=false;
					isRestoring=true;
				}
				else //info not showed; let�s backup our canvas and draw information
				{
					isRestoring=false;
					loadPixels();
					backupCanvas.beginDraw();
					backupCanvas.loadPixels();
					System.arraycopy(pixels, 0, backupCanvas.pixels, 0, pixels.length);
					backupCanvas.updatePixels();
					backupCanvas.endDraw();
					isInformating=true;
				}
			}
			cleaningLevel=20;
		}
	}

	//Action when pressing our device back button
	public boolean surfaceKeyDown(int code, KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
		  	 if(!isRestoring && !isCleaning)
		  	 {
		  		 if(paintCounter==0)
		  		 {
		  			moveTaskToBack(true);
		  	        return true;
		  	     }

		     	 cleaningLevel=20;
		    	 isCleaning = true;
		    	 isPainting=false;
		         isInformating = false;
		         isRestoring = false;
		         inkAccount=maxAccount;
		         radius=1;
		         paintCounter=0;
		         brancDrawers.clear();
		  	 }
		  	 else if(infoShowed)
		  	 {
		  		 isRestoring=true;
		  	 }
		  	 else if(isInformating)
		  	 {
		  		 isInformating=false;
		  		 cleaningLevel=20;
		  		 isRestoring=true;
		  	 }
	         return true;
	  }
	  return super.surfaceKeyDown(code, event);
	}

	//Auxiliar method.
	public boolean surfaceKeyUp(int code, KeyEvent event)
	{
		return super.surfaceKeyDown(code, event);
	}

	//Sharing the sketch
	private void doShareSketch()
	{
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
				File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/Zen/");
				root.mkdirs();
				saveImage(root.getAbsolutePath()+File.separator+System.currentTimeMillis()+"zenSketch.jpeg");
		}
		else
		{
			File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Zen/");
			root.mkdirs();
			saveImage(root.getAbsolutePath()+File.separator+System.currentTimeMillis()+"zenSketch.jpeg");
		}
	}

	//Save the image
	private void saveImage(String path)
	{
		boolean success = false;

	    // Make sure the pixel data is ready to go
	    loadPixels();

	    OutputStream output = new BufferedOutputStream(createOutput(path), 16 * 1024);

	    String lower = path.toLowerCase(Locale.ENGLISH);
	    String extension = lower.substring(lower.lastIndexOf('.') + 1);
	    if (extension.equals("jpg") || extension.equals("jpeg"))
	    {
	    	Bitmap outgoing = Bitmap.createBitmap(pixels, width,height , Config.ARGB_8888);
	       	outgoing = Bitmap.createBitmap(outgoing,0,0,width-height/16-1,canvasAHeight);
	        success = outgoing.compress(CompressFormat.JPEG, 100, output);
	        outgoing.recycle();
	    }
	    if (!success)
	    {
	      System.err.println("Could not write the image to " + path);
	    }

	    Intent share = new Intent(Intent.ACTION_SEND);
		share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+path));
		share.setType("image/*");
		share.putExtra(Intent.EXTRA_SUBJECT,"My Zen Sketch");
		share.putExtra(Intent.EXTRA_TEXT,"I tried Juanky Soriano�s Zen! App. It�s amazing!\n Check it out at http://www.juankysoriano.com");
		startActivity(Intent.createChooser(share, "Zen your friends"));
	}



	public int sketchWidth() { return displayWidth; }

	public int sketchHeight() { return displayHeight; }

	public void onCreate(Bundle savedInstanceState)
	{
		//I did some modifications to the PApplet class in processing,
		//in order to compute the sketch dimensions for android at the start of onCreate
		//this method needs to be called.
		getDisplayMetrics();
		drawHandler = new DrawingHandler(this);
		try
		{
			mp = MediaPlayer.create(getCurrentContext(), R.raw.zen);
			mp.start();
			mp.setLooping(true);
			mp.setVolume(volumeCounter, volumeCounter);

		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}

		canvasAHeight = 4*displayHeight/5;
		canvasBHeight = displayHeight/5;
		buttonHeight = displayHeight/6;
		infoHeight = displayHeight/10;
		inkInitX = 0;
		inkInitY=6*displayHeight/7;
		waterInitX = buttonHeight;
		waterInitY = 6*displayHeight/7;
		resetInitX = displayWidth-buttonHeight;
		resetInitY = 6*displayHeight/7;
		shareInitX = resetInitX-buttonHeight;
		shareInitY = 6*displayHeight/7;
		infoInitX = displayWidth-displayHeight/11;
		infoInitY = displayHeight/48;

		canvas = loadImage(R.drawable.canvas,displayWidth,canvasAHeight,Bitmap.Config.RGB_565);
		canvasdown = loadImage(R.drawable.canvasdown,displayWidth,canvasBHeight,Bitmap.Config.RGB_565);
		information = loadImage(R.drawable.information,displayWidth,displayHeight,Bitmap.Config.ARGB_4444);
		texture = loadImage(R.drawable.texture,80,80,Bitmap.Config.ARGB_4444);
		ink = loadImage(R.drawable.ink,buttonHeight,buttonHeight, Bitmap.Config.ARGB_4444);
		share = loadImage(R.drawable.share,buttonHeight,buttonHeight,Bitmap.Config.ARGB_4444);
		water = loadImage(R.drawable.water,buttonHeight,buttonHeight,Bitmap.Config.ARGB_4444);
		reset = loadImage(R.drawable.reset,buttonHeight,buttonHeight,Bitmap.Config.ARGB_4444);
		info = loadImage(R.drawable.info,infoHeight,infoHeight,Bitmap.Config.ARGB_4444);

		super.onCreate(savedInstanceState);
	}


	public void onStart()
	{
		if(!mp.isPlaying())
		{
			mp.start();
		}
		super.onStart();
	}

	public void onRestart()
	{
		if(!mp.isPlaying())
		{
			mp.start();
		}
		super.onRestart();
	}


	public void onResume()
	{
		if(!mp.isPlaying())
		{
			mp.start();
		}
		super.onResume();
	}


	public void onPause()
	{
		if(mp.isPlaying())
		{
			mp.pause();
		}
		super.onPause();
	}

	public void onStop()
	{
		if(mp.isPlaying())
		{
			mp.pause();
		}
		super.onStop();
	}

	public void onDestroy()
	{
		if(mp.isPlaying())
		{
			mp.stop();
		}
		mp.release();
		super.onDestroy();
	} **/
}


//THANK YOU FOR READING.