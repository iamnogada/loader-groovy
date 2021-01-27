import static net.grinder.script.Grinder.grinder
import static org.junit.Assert.*
import static org.hamcrest.Matchers.*
import net.grinder.plugin.http.HTTPRequest
import net.grinder.plugin.http.HTTPPluginControl
import net.grinder.script.GTest
import net.grinder.script.Grinder
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
// import static net.grinder.util.GrinderUtils.* // You can use this if you're using nGrinder after 3.2.3
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import java.util.Date
import java.util.List
import java.util.ArrayList

import HTTPClient.Cookie
import HTTPClient.CookieModule
import HTTPClient.HTTPResponse
import HTTPClient.NVPair

/**
 * A simple example using the HTTP plugin that shows the retrieval of a
 * single page via HTTP.
 *
 * This script is automatically generated by ngrinder.
 *
 * @author admin
 */
@RunWith(GrinderRunner)
class TestRunner {

  public static GTest test
  public static HTTPRequest request
  public static NVPair[] headers = []
  public static NVPair[] params = []
  public static Cookie[] cookies = []
  public static boolean bLog = false

    @BeforeProcess
  public static void beforeProcess() {
    HTTPPluginControl.getConnectionDefaults().timeout = 60000
    test = new GTest(1, 'prod-optimize')
    request = new HTTPRequest()
    grinder.logger.info('before process.')
  }

    @BeforeThread
  public void beforeThread() {
    test.record(this, 'test')
    grinder.statistics.delayReports = true

    	def json ='''
    {
      "mbrId" : "test" ,
      "mbrPwd" : "1111"
    }
    '''
    def threadContext = HTTPPluginControl.getThreadHTTPClientContext()
    headers = [new NVPair('Content-type', 'application/json;charset=UTF-8')];
    request.setHeaders(headers)
    if(0 < cookies.size()) return 
	  HTTPResponse result = request.POST('https://www.widtus.com/api/auth/login',json.getBytes(),headers)
    cookies = CookieModule.listAllCookies(threadContext)
    grinder.logger.info(cookies.toString())
    grinder.logger.info('before thread.')

  }

    @Before
  public void before() {
		def threadContext = HTTPPluginControl.getThreadHTTPClientContext()
     cookies.each {
    CookieModule.addCookie(it ,threadContext)
    grinder.logger.info("{}", it)
    }
    grinder.logger.info('before. init headers and cookies')
  }

    @Test
  public void test() {
    def json ='''
{
    "input_value": [
        {
            "MI": 14.44,
            "TS": 146.07,
            "FM": 611.24,
            "Izod23": 0,
            "Izod-30": 10.63,
            "HDT": 99.06,
            "Shrinkage.1.1000": 8.57
        }
    ],
    "model_path": "skgc/blobs/model/m-201215141000111/universalmodel.pickle"
}
		'''
    def threadContext = HTTPPluginControl.getThreadHTTPClientContext()
    HTTPResponse result = request.POST('https://workspace.widtus.com/api/vrdlab/recipe/models/m-201215141000111',json.getBytes(),headers)
    // HTTPResponse result = request.POST('http://10.242.18.125/api/auth/login',json.getBytes(),headers)
    cookies = CookieModule.listAllCookies(threadContext)
    grinder.logger.info(cookies.toString())
    if (result.statusCode == 301 || result.statusCode == 302) {
      grinder.logger.warn('Warning. The response may not be correct. The response code was {}.', result.statusCode)
        } else {
      assertThat(result.statusCode, is(200))
    }
  }
  

}