package feedPage;

import java.awt.AWTException;
import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.DerivedBase;
import com.pages.FeedPage;

import jxl.read.biff.BiffException;

public class FileUpload extends DerivedBase{
	FeedPage file;
	@Test(dataProvider ="dp")
	public void uploadfile(String email) throws InterruptedException, IOException, AWTException
	{
		file = new FeedPage();
		int wallposts = file.createPost(data.get("wallpostText"));
		file.FileUpload(data.get("pdfpath"));
		file.submitPost(wallposts);
		
	}
	@DataProvider(parallel = true)
	public Object[][] dp() throws BiffException, IOException, InterruptedException {
		getTestData("WallpostFileUpload");
		return new Object[][] { { data.get("Email") } };
	}
}
