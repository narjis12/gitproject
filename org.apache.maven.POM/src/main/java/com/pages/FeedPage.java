package com.pages;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.base.DerivedBase;
public class FeedPage extends DerivedBase {
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
	By like1 = By.xpath("//div[@class= 'fa icon fa-thumbs-up icon--right ']");
	By like = By.xpath("//div[@id='app']/div/div/section/div[2]/section/div[2]/div/section/div/div[2]/div/div/div[3]/button[2]/span");//*[@id="app"]/div/div[1]/section/div[2]/section/div[2]/div/section/div/div[2]/div[1]/div[1]/div[3]/button[2]
	By comment = By.xpath("//div[2]/div/div/button");
	By commentField = By.id("comment");
	By postComment = By.cssSelector("i.fa.fa-arrow-right");
	
	
	public int createPost(String posttext) throws InterruptedException
	{
		System.out.println("createwallpost Thread ID: " + Thread.currentThread().getId());
		int curentNumberOfWallposts = driver.findElements(By.xpath("//div[@class='post-type-wrap wall-post__content']")).size();
		System.out.println("before"+curentNumberOfWallposts);
		driver.findElement(postarea).sendKeys(posttext);
		return curentNumberOfWallposts;
		
	}
	
	
	public void submitPost(int curentNumberOfWallposts) throws InterruptedException
	{
		driver.findElement(SubmitPost).click();
		Thread.sleep(3000);
		int UpatedNumberOfWallposts = driver.findElements(By.xpath("//div[@class='post-type-wrap wall-post__content']")).size();
		System.out.println("after"+UpatedNumberOfWallposts);
		Assert.assertNotEquals(curentNumberOfWallposts, UpatedNumberOfWallposts);
		//waitForCssElementsNumberToGrow(".mb-4", curentNumberOfWallposts);
		//*[@id="app"]/div/div[1]/section/div[2]/section/div[2]/div/section/div/div[2]/div[1]
		//*[@id="app"]/div/div[1]/section/div[2]/section/div[2]/div/section/div/div[2]/div[2]
	}
	public void likePost()
	{
		
			driver.findElement(like).click();
	}
	
	public void commentOnPOst()
	{
		System.out.println("comment " + Thread.currentThread().getId());
		int currentNumberComments = driver.findElements(By.cssSelector(".comment")).size();
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("scroll(0, 250)");
		driver.findElement(comment).click();
		driver.findElement(commentField).sendKeys(data.get("comment"));
		driver.findElement(postComment).click();
		waitForCssElementsNumberToGrow(".comment", currentNumberComments);
	}
	
	public void ImageUpload(String picturepath) throws IOException, InterruptedException
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement imageuploadbutton = driver.findElement(By.cssSelector(".feather-image"));
		//js.executeScript("arguments[0].click()",driver.findElement(By.cssSelector(".feather-image")) );
		imageuploadbutton.click();
		
		WebElement frame1 = driver.switchTo().activeElement();
	    System.out.println(new File(picturepath).getCanonicalPath());
	    frame1.sendKeys((new File(picturepath).getCanonicalPath()));
			
		//waitForCssElement(".fa-circle-o-notch");
		driver.switchTo().defaultContent();
		
	}
	public void FileUpload(String filePath) throws IOException
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		WebElement fileuploadbutton = driver.findElement(Fileupload);
		js.executeScript("arguments[0].click()", fileuploadbutton);
		WebElement frame1 = driver.switchTo().activeElement();
		frame1.sendKeys((new File(filePath).getCanonicalPath()));
		driver.switchTo().defaultContent();
	}
}
