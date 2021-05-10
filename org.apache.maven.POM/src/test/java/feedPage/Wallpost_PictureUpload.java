package feedPage;
import static org.testng.Assert.fail;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.base.DerivedBase;
import com.pages.FeedPage;
import com.sun.mail.imap.Utility;

import jxl.read.biff.BiffException;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class Wallpost_PictureUpload extends DerivedBase {
	FeedPage picturepost;

	@Test(dataProvider = "dp", priority = 0)
	public void WallpostPictureUpload(String email) throws BiffException, IOException, InterruptedException {
		getTestData("WallpostPictureUpload");
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.MILLISECONDS);
		int currentNumberWallposts = driver.findElements(By.cssSelector(".wall-post")).size();
		driver.findElement(By.id("createPost")).sendKeys(data.get("PostText"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement uploadfile = driver.findElement(By.cssSelector(".fa-picture-o"));
		js.executeScript("arguments[0].click()", uploadfile);
		WebElement frame1 = driver.switchTo().activeElement();
		frame1.sendKeys((new File(data.get("picturepath"))).getCanonicalPath());
		driver.switchTo().defaultContent();
		waitForCssElement(".thumbnail__image");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		waitForCssElementsNumberToGrow(".wall-post", currentNumberWallposts);
		int i = driver.findElements((By.cssSelector(".create-post__errors li.alert-danger"))).size();
		if (i > 0) {
			fail("An unexpected error occurred! Please try again later.");
		} else {
			System.out.println("picture uploaded successfully");
		}
	}

	@DataProvider(parallel = true)
	public Object[][] dp() throws BiffException, IOException, InterruptedException {
		getTestData("WallpostPictureUpload");
		return new Object[][] { { data.get("LoginEMailId") } };
	}

}
