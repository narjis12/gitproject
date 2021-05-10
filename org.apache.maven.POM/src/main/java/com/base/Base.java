package com.base;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import com.browserstack.local.Local;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
	
	public class Base {
		  public static WebDriver driver;
		  private Local l;
		  protected static Map<String, String> data = null;
		  protected static int dataIndex = 0;
		  private JSONObject config;
		  private String sessionId;
		  public String domain = System.getenv("DOMAIN");

		  @BeforeClass(alwaysRun = true)
		  @org.testng.annotations.Parameters(value = { "config", "environment" })
		  public void setUp(@Optional("") String config_file, @Optional("") String environment) throws Exception {
		    String useBrowserstack = System.getenv("USE_BROWSERSTACK");
		    if (
		      "true".equalsIgnoreCase(useBrowserstack) && 
		      !"".equalsIgnoreCase(config_file) && 
		      !"".equalsIgnoreCase(environment)
		    ) {
		      driver = this.setupBrowserstackDriver(config_file, environment);
		    } else {
		      driver = this.setupLocalDriver();
		    }
		    this.openWebsite();
		  }

		  public void openWebsite() throws Exception {
		    if (domain == null) {
		      domain = "portal-dev.cmp-df.net";
		    }
		    driver.get("http://" + domain + "/?user=demo&password=portal");
		    (new WebDriverWait(driver, 30))
		        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".flex-none .pb-4")));
		    JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("localStorage.user = 'demo'; localStorage.password = 'portal'");
		    driver.manage().window().maximize();
		  }

		  public  void getTestData(String testName) throws BiffException, IOException, InterruptedException {
		    data = new HashMap<String, String>();
		    Sheet dataSheet = Workbook.getWorkbook(new File(("./TestData.xls"))).getSheet("testsheet");
		    dataIndex = dataSheet.findCell(testName).getRow();
		    for (int i = 1; i < dataSheet.getColumns(); i++) {
		      for (int j = dataIndex - 1; j < dataSheet.getRows(); j++) {
		        String key = dataSheet.getCell(i, j).getContents();
		        String value = dataSheet.getCell(i, dataIndex).getContents();
		        data.put(key, value);

		      }
		    }
		  }
		  
		  public static String getFullPath (String dataFieldName) throws IOException {
		    return (new File(data.get(dataFieldName))).getCanonicalPath();
		  }
		  public static void waitForCssElement(String css) {
		    (new WebDriverWait(driver, 30))
		        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(css)));
		  }
		  public static void waitForCssElementsNumberToGrow(String css) {
		    int currentNumberOfElements = driver.findElements(By.cssSelector(css)).size();
		    new WebDriverWait(driver, 30).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(css), currentNumberOfElements));
		  }
		  public static void waitForCssElementsNumberToGrow(String css, int numberOfElements) {
		    new WebDriverWait(driver, 30).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(css), numberOfElements));
		  }
		  public static void waitForCssElementClickable(String css) {
		    (new WebDriverWait(driver, 30))
		        .until(ExpectedConditions.elementToBeClickable(By.cssSelector(css)));
		  }
		  
		  public static void waitForXpathElement(String xpath) {
		    (new WebDriverWait(driver, 30))
		        .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		  }
		  public static void waitForClassElement(String className) {
			    (new WebDriverWait(driver, 30))
			        .until(ExpectedConditions.presenceOfElementLocated(By.className(className)));
			  }
		  public static void waitForLinkText(String linkText) {
		    (new WebDriverWait(driver, 30))
		        .until(ExpectedConditions.presenceOfElementLocated(By.linkText(linkText)));
		  }
		  
		  public static void waitForId(String Id) {
		    (new WebDriverWait(driver, 30))
		        .until(ExpectedConditions.presenceOfElementLocated(By.id(Id)));
		  }

		  @AfterClass(alwaysRun = true)
		  public void tearDown() throws Exception {
		    driver.quit();
		    if (l != null)
		      l.stop();
		  }
		  public void assertAndVerifyElement(By element) throws InterruptedException {
		      boolean isPresent = false;

		      for (int i = 0; i < 5; i++) {
		          try {
		              if (driver.findElement(element) != null) {
		                  isPresent = true;
		                  break;
		              }
		          } catch (Exception e) {
		              // System.out.println(e.getLocalizedMessage());
		              Thread.sleep(1000);
		          }
		      }
		      Assert.assertTrue(isPresent, "\"" + element + "\" is not present.");
		  }
		  private WebDriver setupLocalDriver() {
		    ChromeOptions chromeOptions = new ChromeOptions();

		    String os = System.getProperty("os.name").toLowerCase();
		    if (os.contains("win")) {
		      // Operating system is based on Windows
		      String driverPath = "C:\\Users\\public\\Desktop\\chromedriver.exe";
		      System.setProperty("webdriver.chrome.driver", driverPath);
		      chromeOptions.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
		      
		    } else if (os.contains("osx")) {
		      // Operating system is Apple OSX based
		    } else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
		      // Operating system is based on Linux/Unix/*AIX
		    }

		    WebDriver returnDriver = new ChromeDriver(chromeOptions);
		    returnDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		    return returnDriver;
		  }

		  private WebDriver setupBrowserstackDriver(String config_file, String environment) throws Exception {
		    JSONParser parser = new JSONParser();
		    config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + config_file));
		    JSONObject envs = (JSONObject) config.get("environments");

		    DesiredCapabilities capabilities = new DesiredCapabilities();

		    String domain = System.getenv("DOMAIN");
		    if (domain != null) {
		      capabilities.setCapability("project", domain);
		    }
		    
		    String className = this.getClass().getSimpleName();
		    String packageName = this.getClass().getPackage().getName();
		    String name = packageName + "." + className;
		    if (name != null) {
		      capabilities.setCapability("name", name);
		    }

		    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM. HH:mm");
		    LocalDateTime localDateTime = LocalDateTime.now();
		    capabilities.setCapability("build", dtf.format(localDateTime));

		    String build = System.getenv("BUILD");
		    if (build != null) {
		      capabilities.setCapability("build", build);
		    }

		    Map<String, String> envCapabilities = (Map<String, String>) envs.get(environment);
		    Iterator it = envCapabilities.entrySet().iterator();
		    while (it.hasNext()) {
		      Map.Entry pair = (Map.Entry) it.next();
		      capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
		    }

		    Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
		    it = commonCapabilities.entrySet().iterator();
		    while (it.hasNext()) {
		      Map.Entry pair = (Map.Entry) it.next();
		      if (capabilities.getCapability(pair.getKey().toString()) == null) {
		        capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
		      }
		    }

		    String username = System.getenv("BROWSERSTACK_USERNAME");
		    if (username == null) {
		      username = (String) config.get("user");
		    }

		    String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
		    if (accessKey == null) {
		      accessKey = (String) config.get("key");
		    }

		    String app = System.getenv("BROWSERSTACK_APP_ID");
		    if (app != null && !app.isEmpty()) {
		      capabilities.setCapability("app", app);
		    }

		    if (capabilities.getCapability("browserstack.local") != null
		        && capabilities.getCapability("browserstack.local") == "true") {
		      l = new Local();
		      Map<String, String> options = new HashMap<String, String>();
		      options.put("key", accessKey);
		      l.start(options);
		    }
		    RemoteWebDriver returnDriver = new RemoteWebDriver(
		        new URL("http://" + username + ":" + accessKey + "@" + config.get("server") + "/wd/hub"), capabilities);
		    returnDriver.setFileDetector(new LocalFileDetector());
		    returnDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		    sessionId = returnDriver.getSessionId().toString();
		    return returnDriver;
		  }
		  
		  @AfterMethod(alwaysRun = true)
			public void markBrowserstack(ITestResult result) throws URISyntaxException, UnsupportedEncodingException, IOException {
		    String useBrowserstack = System.getenv("USE_BROWSERSTACK");
		    if ("true".equalsIgnoreCase(useBrowserstack)) {
		      if (ITestResult.FAILURE == result.getStatus()) {
		        String user = (String) config.get("user");
		        String key = (String) config.get("key");
		        URI uri = new URI("https://" + user + ":" + key + "@api.browserstack.com/automate/sessions/" + sessionId + ".json");
		        HttpPut putRequest = new HttpPut(uri);

		        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        nameValuePairs.add((new BasicNameValuePair("status", "failed")));
		        nameValuePairs.add((new BasicNameValuePair("reason", result.toString())));
		        putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        HttpClientBuilder.create().build().execute(putRequest);
		  		}
		    }
			
		  }
	}

