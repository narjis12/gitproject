package com.base;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.fail;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import junit.framework.Assert;
import jxl.read.biff.BiffException;
public class DerivedBase extends Base {
	static Base url;
	@Test(dataProvider = "dp")
	public static void login(String email) throws Exception {
		// getTestData("Login");
		// WebDriverWait wait = new WebDriverWait(driver, 10);
		// wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button/span")));
		// element.click();
		WebDriverWait wait = new WebDriverWait(driver, 70);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".login-logout-navigation .flex-auto")));
		element.click();
		//driver.findElement(By.cssSelector(".flex:nth-child(1) > .login-navigation .block")).click();
		String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
		String subWindowHandler = null;
		Set<String> handles = driver.getWindowHandles(); // get all window handles
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()) {
			subWindowHandler = iterator.next();
		}
		driver.switchTo().window(subWindowHandler);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("login-email")));
		driver.findElement(By.id("login-email")).sendKeys(email);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.id("login-password")).sendKeys("Najju@2017");
		driver.findElement(By.cssSelector(".btn")).click();
		driver.switchTo().window(parentWindowHandler);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".form-text")));
		//(new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".page-template")));
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		int s = driver.findElements(By.cssSelector(".form-text")).size();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		if (s > 0) {
			System.out.println("logged in successfully");
			url = new Base();
			driver.get("https://portal-dev.my-portal.io/settings/general");
			Thread.sleep(4000);
			int lan = driver.findElements(By.name("Select Language")).size();
			if (lan > 0) {
				System.out.println("English is already selected as portal language");
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
				url.openWebsite();
				Thread.sleep(5000);
			} else {
				waitForId("Sprache ändern");
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
				Select language = new Select(driver.findElement(By.name("Sprache ändern")));
				language.selectByValue("en");
				driver.findElement(By.xpath("//div[2]/aside/div[3]/button/span")).click();
				Thread.sleep(4000);
				url.openWebsite();
				Thread.sleep(5000);
			}
			
			
		} else {
			int paymnet = driver.findElements(By.cssSelector(".payment__title")).size();
			int setup = driver.findElements(By.cssSelector(".setup__subtitle")).size();
			System.out.println(paymnet);
			System.out.println(setup);
			if (setup > 0) {
				System.out.println("subscription is not activated yet");
				js.executeScript("scroll(0,700)");
				driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div/div[2]/div[2]/button/span")).click();
				Thread.sleep(2000);
				driver.findElement(By.id("Salutation")).sendKeys("Mr");
				driver.findElement(By.id("First Name")).sendKeys("test");
				driver.findElement(By.id("Last Name")).sendKeys("automation");
				js.executeScript("scroll(0,700)");
				driver.switchTo().frame(driver.findElement(By.id("paymentFrameIframe")));
				driver.findElement(By.id("cardName")).sendKeys("test");
				Thread.sleep(3000);
				driver.findElement(By.id("cardNumber")).sendKeys("4200000000000000");
				driver.findElement(By.id("expirationMonth")).sendKeys("10");
				driver.findElement(By.id("expirationYear")).sendKeys("2020");
				driver.findElement(By.id("verification")).sendKeys("123");
				driver.switchTo().defaultContent();
				Thread.sleep(3000);
				js.executeScript("scroll(0,1050)");
				Thread.sleep(3000);
				WebElement e1 = driver.findElement(By.xpath("//div[2]/button/span"));
				js.executeScript("arguments[0].click();", e1);
				// driver.findElement(By.xpath("//div[@id='paymentFrameForm']/div[3]/div[2]/button/span")).click();
				Thread.sleep(5000);
				int post = driver.findElements(By.id("createPost")).size();
				if (post > 0) {
					System.out.println("logged in successfully");
				} else {
					fail("user did not navigate to userfeed after payment");
				}
			} else if (paymnet > 0) {
				System.out.println("payment failed/subscription expired");
				String expired = driver.findElement(By.xpath("//div[@id='app']/div/div/main/div[3]/div/div[1]/h1")).getText();
				String[] subexp = expired.split("[\\,]");
				String fail = driver.findElement(By.xpath("//div[@id='app']/div/div/main/div[3]/div/div[1]/h1")).getText();
				String[] subfail = fail.split("[\\,]");
				// subscription expired page
				if (subexp[1].equalsIgnoreCase(" Your subscription has expired!")
						|| subexp[1].equalsIgnoreCase(" Ihr Abonnement ist abgelaufen!")) {
					js.executeScript("scroll(0,700)");
					driver.findElement(By.cssSelector(".submit-order-btn > span")).click();
					Thread.sleep(3000);
					int post = driver.findElements(By.id("createPost")).size();
					if (post > 0) {
						System.out.println("logged in successfully");
					} else {
						fail("user did not navigate to userfeed after payment");
					}
				} else if (subfail[1].equalsIgnoreCase(" Your payment failed!")
						|| subfail[1].equalsIgnoreCase(" Ihre Zahlung ist fehlgeschlagen!")) {
					js.executeScript("scroll(0,700)");
					driver.findElement(By.cssSelector(".btn--right > span")).click();
					Thread.sleep(3000);
					int post = driver.findElements(By.id("createPost")).size();
					if (post > 0) {
						System.out.println("logged in successfully");
					} else {
						fail("user did not navigate to userfeed after payment");
					}
				}
			} else {
				driver.quit();
				fail("login failed/incorrect credentials");
			}
		}
		/*
		int lan = driver.findElements(By.id("Select Language")).size();
		if (lan > 0) {
			System.out.println("English is already selected as portal language");
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
		} else {
			waitForId("Sprache ändern");
			Select language = new Select(driver.findElement(By.name("Sprache ändern")));
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
			language.selectByValue("en");
			waitForCssElement(".dashboard-list__item:nth-child(1) > .dashboard-list__label");
			String languagetext = driver.findElement(By.cssSelector(".dashboard-list__item:nth-child(1) > .dashboard-list__label")).getText();
			Assert.assertEquals(languagetext, "Network");
		}
		*/
	}
}