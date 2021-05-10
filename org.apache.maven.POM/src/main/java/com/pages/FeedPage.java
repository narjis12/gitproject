package com.pages;
import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.base.DerivedBase;
public class FeedPage extends DerivedBase {
	RobotClass robot;
	//static WebDriver driver;
	
	public void feedpage(WebDriver driver) throws IOException 
	{
		this.driver = driver;
		
	}
	By postarea = By.cssSelector(".form-text");
	By imageupload = By.cssSelector(".feather-image");
	By Fileupload = By.cssSelector(".feather-file-plus");
	By Adhocbutton = By.cssSelector(".hidden .slider");
	By SubmitPost = By.cssSelector(".mr-2");
	By webconference = By.cssSelector(".relative:nth-child(2) > .relative .flex-auto:nth-child(3) .absolute");
	By like1 = By.xpath("//button[contains(text(), 'Like')]");
	By like = By.xpath(
			"//div[@id='app']/div/div/section/div[2]/section/div[2]/div/section/div/div[2]/div/div/div[3]/button[2]/span");// *[@id="app"]/div/div[1]/section/div[2]/section/div[2]/div/section/div/div[2]/div[1]/div[1]/div[3]/button[2]
	By comment = By.xpath("//div[2]/div/div/button");
	By commentField = By.id("comment");
	By postComment = By.cssSelector("i.fa.fa-arrow-right");
	By sharePost = By.cssSelector("div:nth-child(1) > div > .box > .relative .relative .flex");
	By shareOutside = By.linkText("Share to the outside");
	By shareFb = By.cssSelector(".fa-facebook-official");
	By wallpostsCount = By.xpath("//div[@id='app']/div[1]/div[1]/section/div[2]/div/section/div/div/section/div/div[2]/div[1]/div");
	By postprivacy = By.xpath("//div[@id='app']/div[1]/div[1]/section/div[2]/div/section/div/div/section/div/div[1]/form/div[2]/div/div[2]/div[2]/div/div/button");
	By privatepost = By.cssSelector(".w-full:nth-child(3) > .flex > .flex-auto");

	public int createPost(String posttext) throws InterruptedException {
		System.out.println("createwallpost Thread ID: " + Thread.currentThread().getId());
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		Thread.sleep(4000);
		int curentNumberOfWallposts = driver.findElements(wallpostsCount).size();
		System.out.println("before  " + curentNumberOfWallposts);
		driver.findElement(postarea).sendKeys(posttext);
		return curentNumberOfWallposts;
		
	}
	
	public int privatePost(String posttext) throws InterruptedException
	{
		System.out.println("createwallpost Thread ID: " + Thread.currentThread().getId());
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		Thread.sleep(4000);
		int curentNumberOfWallposts = driver.findElements(wallpostsCount).size();
		System.out.println("before  " + curentNumberOfWallposts);
		waitForCssElement(".flex > .feather-globe > path");
		driver.findElement(postprivacy).click();
		driver.findElement(privatepost).click();
		driver.findElement(postarea).sendKeys(posttext);
		return curentNumberOfWallposts;
		
	}
	

	public int adhocPost(String posttext) throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		waitForXpathElement("//div[@class='post-type-wrap wall-post__content']");
		System.out.println("createwallpost Thread ID: " + Thread.currentThread().getId());
		int curentNumberOfWallposts = driver.findElements(wallpostsCount).size();
		System.out.println("no.of wallposts before posting" + curentNumberOfWallposts);
		driver.findElement(Adhocbutton).click();
		driver.findElement(postarea).sendKeys(posttext);
		return curentNumberOfWallposts;

	}

	public int sharePostInternally(String posttext) throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		int curentNumberOfWallposts = createPost(posttext);
		submitPost(curentNumberOfWallposts);
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("div:nth-child(1) > .box .stacked-text__bottom--link > span")).click();
		Thread.sleep(1000);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("scroll(0,200)");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@class = 'share__wrapper']")).click();
		driver.findElement(By.cssSelector(".btn--secondary > span")).click();
		return curentNumberOfWallposts;
	}

	public void submitPost(int curentNumberOfWallposts) throws InterruptedException {
		driver.findElement(SubmitPost).click();
		Thread.sleep(5000);
		int UpatedNumberOfWallposts = driver.findElements(wallpostsCount).size();
		System.out.println("no.of wallposts after posting  " +  UpatedNumberOfWallposts);
		Assert.assertNotEquals(curentNumberOfWallposts, UpatedNumberOfWallposts);
	}

	public void likePost() {

		driver.findElement(like1).click();
	}

	public void commentOnPOst() {
		System.out.println("comment " + Thread.currentThread().getId());
		int currentNumberComments = driver.findElements(By.cssSelector(".comment")).size();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 250)");
		driver.findElement(comment).click();
		driver.findElement(commentField).sendKeys(data.get("comment"));
		driver.findElement(postComment).click();
		waitForCssElementsNumberToGrow(".comment", currentNumberComments);
	}

	public void ImageUpload(String picturepath) throws IOException, InterruptedException, AWTException
	{
		 WebElement fileElement=driver.findElement(By.xpath("//*[@id='imageControl']"));
         fileElement .click();
         robot = new RobotClass();
         robot.selectfile( new File(picturepath).getAbsolutePath());
  	     robot.uploadfile();
         waitForXpathElement("//div[@class='thumbnail']");
	}

	public void sharePostExternally(String text) throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		int curentNumberOfWallposts = createPost(text);
		submitPost(curentNumberOfWallposts);
		Thread.sleep(3000);
		driver.findElement(sharePost).click();
		driver.findElement(shareOutside).click();
		driver.findElement(shareFb).click();
		
	}
	public void FileUpload(String filePath) throws IOException, AWTException, InterruptedException {
		 WebElement fileElement=driver.findElement(By.xpath("//*[@id='docControl']"));
         fileElement .click();
         robot = new RobotClass();
         robot.selectfile( new File(filePath).getAbsolutePath());
  	     robot.uploadfile();
        }
}
