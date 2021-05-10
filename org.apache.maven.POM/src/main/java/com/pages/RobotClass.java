package com.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import com.base.Base;


public class RobotClass extends Base {
	
	public void selectfile(String path) throws InterruptedException, AWTException
	{
		
		//Transferable File Name declaration
	  	StringSelection contents = new StringSelection(path);
	  	
	  	//Getting toolkit
	  	Toolkit toolKit = Toolkit.getDefaultToolkit();
	  	
	  	//Getting clipboard as file upload window
	  	Clipboard clipBoard = toolKit.getSystemClipboard();
	  	
	  	//Copying string file name to the file upload window
	  	clipBoard.setContents(contents, null);
	  			
	  	System.out.println("File Selection- " +contents);
	  	  
	 }
	public void uploadfile() throws InterruptedException, AWTException
	{
	  	
	  	Robot robot = new Robot();
	  	robot.keyPress(KeyEvent.VK_ENTER);
	  	robot.keyRelease(KeyEvent.VK_ENTER);
	  	robot.keyPress(KeyEvent.VK_CONTROL);
	  	robot.keyPress(KeyEvent.VK_V);
	  	robot.keyRelease(KeyEvent.VK_CONTROL);
	  	robot.keyRelease(KeyEvent.VK_V);
	  	robot.keyPress(KeyEvent.VK_ENTER);
	  	robot.keyRelease(KeyEvent.VK_ENTER);
		
	}
	
}


