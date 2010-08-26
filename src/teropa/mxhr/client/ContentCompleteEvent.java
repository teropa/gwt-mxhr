package teropa.mxhr.client;

import com.google.gwt.event.shared.GwtEvent;

public class ContentCompleteEvent extends GwtEvent<ContentCompleteHandler> {

	public static final Type<ContentCompleteHandler> TYPE = new Type<ContentCompleteHandler>();
	
	@Override
	protected void dispatch(ContentCompleteHandler handler) {
		handler.onContentComplete(this);
	}
	
	@Override
	public Type<ContentCompleteHandler> getAssociatedType() {
		return TYPE;
	}
}
