package teropa.mxhr.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xhr.client.XMLHttpRequest;

public class Demo implements EntryPoint {

	private final RootPanel streamPanel = RootPanel.get("stream");
	private final RootPanel normalPanel = RootPanel.get("normal");
	
	private int imgsLoaded = 0;
	
	public void onModuleLoad() {		
		loadStream();
	}

	private void loadStream() {
		MXHR req = new MXHR();
		final long streamStart = System.currentTimeMillis();
		
		req.addContentReceivedHandler(new ContentReceivedHandler() {
			public void onContentReceived(ContentReceivedEvent evt) {
				streamPanel.add(new Image("data:image/gif;base64,"+evt.payload));
			}
		});
		req.addContentCompleteHandler(new ContentCompleteHandler() {
			public void onContentComplete(ContentCompleteEvent evt) {
				String msg = "Stream took: "+(System.currentTimeMillis() - streamStart)+"ms";
				streamPanel.insert(new HTML("<p>"+msg+"</p>"), 0);				
				loadNormal();
			}
		});
		
		req.load(GWT.getHostPageBaseURL() + "demoServlet");
	}

	private void loadNormal() {
		final long normalStart = System.currentTimeMillis();
		LoadHandler loadHandler = new LoadHandler() {
			public void onLoad(LoadEvent event) {
				imgsLoaded++;
				if (imgsLoaded == 300) {
					String msg = "Normal, uncached took: "+(System.currentTimeMillis() - normalStart)+"ms";
					normalPanel.insert(new HTML("<p>"+msg+"</p>"), 0);
				}
			}
		};
		for (int i=0 ; i<300 ; i++) {
			Image img = new Image(GWT.getHostPageBaseURL() + "32x32-digg-guy.gif?nocache="+(System.currentTimeMillis() * Math.random()));
			img.addLoadHandler(loadHandler);
			normalPanel.add(img);
		}
	}

}
