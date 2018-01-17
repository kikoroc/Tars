import com.qq.tars.web.udb.UdbLoginConfigAdapterLocalData;
import com.qq.tars.web.udb.UdbLoginUtil;
import com.qq.tars.web.udb.YaoGuoUdbLoginConfigAdapterImpl;

/**
 * Created by Administrator on 2018/1/3 0003.
 */
public class UnitTest {

    public static void main(String[] args) {
        String uri = "/";
        UdbLoginConfigAdapterLocalData udbLoginConfigAdapterLocalData = new UdbLoginConfigAdapterLocalData(new YaoGuoUdbLoginConfigAdapterImpl());

        boolean matchFilter = UdbLoginUtil.matchFilterUrlPattern(uri, udbLoginConfigAdapterLocalData.filterUrlPattern);
        System.out.println("matchFilter->" + matchFilter);

        boolean isExcluded = UdbLoginUtil.isExcludedUrl(uri, udbLoginConfigAdapterLocalData.excludedUrl);
        System.out.println("isExcluded->" + isExcluded);
    }
}
