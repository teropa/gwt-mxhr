package teropa.mxhr.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xhr.client.XMLHttpRequest;

public class Demo implements EntryPoint {

	private final RootPanel streamPanel = RootPanel.get("stream");
	
	public void onModuleLoad() {
		
		MultiXMLHttpRequest req = new MultiXMLHttpRequest();
		
		req.registerContentReceivedHandler(new ContentReceivedHandler() {
			public void onContentReceived(ContentReceivedEvent evt) {
				streamPanel.add(new Image("data:image/gif;base64,"+evt.payload));
			}
		});
		
		req.load(GWT.getHostPageBaseURL() + "demoServlet");
		
		
		XMLHttpRequest.create().open("GET", "index.html");
	}
	
}
