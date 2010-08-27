package teropa.mxhr.client;

import com.google.gwt.event.shared.EventHandler;

public interface ContentReceivedHandler extends EventHandler {
	public void onContentReceived(ContentReceivedEvent evt);
}