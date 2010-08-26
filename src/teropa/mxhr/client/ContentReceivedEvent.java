package teropa.mxhr.client;

import com.google.gwt.event.shared.GwtEvent;

public class ContentReceivedEvent extends GwtEvent<ContentReceivedHandler> {

	public static final Type<ContentReceivedHandler> TYPE = new Type<ContentReceivedHandler>();
	
	public final String mimeType;
	public final String payload;
	
	public ContentReceivedEvent(String mimeType, String payload) {
		this.mimeType = mimeType;
		this.payload = payload;
	}
	
	@Override
	protected void dispatch(ContentReceivedHandler handler) {
		handler.onContentReceived(this);
	}
	
	@Override
	public Type<ContentReceivedHandler> getAssociatedType() {
		return TYPE;
	}
}
