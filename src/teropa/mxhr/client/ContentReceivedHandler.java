package teropa.mxhr.client;

import com.google.gwt.event.shared.EventHandler;

public abstract class ContentReceivedHandler implements EventHandler {
	
	final String mimeType;
	
	public ContentReceivedHandler(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public abstract void onContentReceived(ContentReceivedEvent evt);
}