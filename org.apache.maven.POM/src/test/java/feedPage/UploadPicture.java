package feedPage;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.base.DerivedBase;
import com.pages.FeedPage;
import jxl.read.biff.BiffException;
public class UploadPicture extends DerivedBase{
	FeedPage picture;
	@Test(dataProvider ="dp")
	public void uploadImage(String email) throws InterruptedException, IOException
	{
		picture = new FeedPage();
		int wallposts = picture.createPost(data.get("PostText"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement imageuploadbutton = driver.findElement(By.cssSelector(".feather-image"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".feather-image")));
		//element1.click();
		//js.executeScript("arguments[0].click()",imageuploadbutton);
		imageuploadbutton.click();
		Thread.sleep(2000);
		//Actions a = new Actions(driver);
		//a.moveToElement(imageuploadbutton).sendKeys("C:/Users/n.fathima/Desktop/Strength Quotes You Need for Your Wallpaper _ Mash Elle.jpg").build().perform();
		
		//System.out.println(driver.switchTo().activeElement());
		driver.switchTo().activeElement().sendKeys("C:/Users/n.fathima/Desktop/image.jpg");
		Thread.sleep(3000);
		//imageuploadbutton.sendKeys("C:/Users/n.fathima/Desktop/automation_picture.jpg");
	    //frame.sendKeys("C:/Users/n.fathima/Desktop/automation_picture.jpg");
	    //frame.findElement(By.cssSelector(".feather-image")).sendKeys("C:/Users/n.fathima/Desktop/automation_picture.jpg");
		driver.switchTo().defaultContent();
		
		//picture.ImageUpload(data.get("picturepath"));
		Thread.sleep(5000);
		System.out.println("sssasasasa");
		//picture.submitPost(wallposts);
		
	}
	@DataProvider(parallel = true)
	public Object[][] dp() throws BiffException, IOException, InterruptedException {
		getTestData("WallpostPictureUpload");
		return new Object[][] { { data.get("LoginEMailId") } };
	}
}
