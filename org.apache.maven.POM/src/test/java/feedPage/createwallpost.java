package feedPage;
import java.io.IOException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.base.DerivedBase;
import com.pages.FeedPage;
import jxl.read.biff.BiffException;

public class createwallpost extends DerivedBase {
	FeedPage post;

	@Test(dataProvider = "dp")
	public void post(String email) throws InterruptedException {
		post = new FeedPage();
		int wallposts = post.createPost(data.get("createwallpost"));
		post.submitPost(wallposts);

	}

	@DataProvider(parallel = true)
	public Object[][] dp() throws BiffException, IOException, InterruptedException {
		getTestData("CreateWallpost_on_NewsFeed");
		return new Object[][] { { data.get("Login") } };
	}
}
