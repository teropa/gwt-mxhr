package teropa.mxhr.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;

public class MultiXMLHttpRequest {

	private XMLHttpRequest req;
	
	private Timer pong;
	private int lastLength = 0;
	private String boundary;
	private String currentStream;
	
	private HandlerManager handlers = new HandlerManager(this);
	
	public void load(String url) {
		req = createReq();
		req.open("GET", url);
		req.setOnReadyStateChange(new ReadyStateChangeHandler() {
			public void onReadyStateChange(XMLHttpRequest xhr) {
				readyStateNanny();
			}
		});
	}

	private void readyStateNanny() {
		if (req.getReadyState() == XMLHttpRequest.LOADING && this.pong == null) {
			String contentTypeHeader = req.getResponseHeader("Content-Type");
			if (!contentTypeHeader.contains("multipart/mixed")) {
				throwUnsupportedContentTypeWhenReady();
			} else {
				boundary = "--" + contentTypeHeader.split("\"")[1];
				pong = new Timer() {
					public void run() {
						ping();
					}
				};
				pong.scheduleRepeating(15);
			}
		}
		if (req.getReadyState() == XMLHttpRequest.DONE) {
			if (pong != null) {
				pong.cancel();
			}
			ping();
			// Fire complete
			req.clearOnReadyStateChange();
		}
	}

	private void ping() {
		int length = req.getResponseText().length();
		String packet = req.getResponseText().substring(lastLength, length);
		processPacket(packet);
		lastLength = length;
	}
	
	private void processPacket(String packet) {
		if (packet.isEmpty()) return;
		
		int startFlag = packet.indexOf(boundary);
		int endFlag = -1;
		
		if (startFlag > -1) {
			if (this.currentStream != null) {
				// If there's an open stream, that's an endFlag, not a startFlag
				endFlag = startFlag;
				startFlag = -1;
			} else {
				// No open stream? Ok, valid startFlag. Let's try find an endFlag then.
				endFlag = packet.indexOf(boundary, startFlag + boundary.length());
			}
		}
		
		if (currentStream == null) {
			currentStream = "";
			if (startFlag > -1) {
				if (endFlag > -1) {
					String payload = packet.substring(startFlag, endFlag);
					currentStream += payload;
					closeCurrentStream();
					packet = packet.replace(payload, "");
					processPacket(packet);
				} else {
					currentStream += packet.substring(startFlag);
				}
			}
		} else {
			if (endFlag > -1) {
				String chunk = packet.substring(0, endFlag);
				currentStream += chunk;
				closeCurrentStream();
				packet = packet.replace(chunk, "");
				processPacket(packet);
			} else {
				currentStream += packet;
			}
		}
	}
	
	private void closeCurrentStream() {
		currentStream = currentStream.replace(boundary + "\n", "");
		String[] mimeAndPayload = currentStream.split("\n");
		String mime = mimeAndPayload[0].split("Content-Type:", 2)[1].split(";", 1)[0].replace(" ", "");
		String payload = "";
		for (int i=1 ; i<mimeAndPayload.length ; i++) {
			payload += mimeAndPayload[i];
			if (i < mimeAndPayload.length - 1) payload += "\n";
		}
		handlers.fireEvent(new ContentReceivedEvent(mime, payload));
		this.currentStream = null;
	}
	
	private void throwUnsupportedContentTypeWhenReady() {
		req.setOnReadyStateChange(new ReadyStateChangeHandler() {
			public void onReadyStateChange(XMLHttpRequest xhr) {
				req.clearOnReadyStateChange();
				throw new IllegalStateException("Response content type is something else than multipart/mixed");
			}
		});
	}
	
	public HandlerRegistration registerContentReceivedHandler(ContentReceivedHandler handler) {
		return handlers.addHandler(ContentReceivedEvent.TYPE, handler);
	}
	
	private static native XMLHttpRequest createReq() /*-{
		try {
			return new ActiveXObject('MSXML2.XMLHTTP.6.0');
		} catch(nope) {
            try {
            	return new ActiveXObject('MSXML3.XMLHTTP');
            } catch(nuhuh) {
                try {
                	return new XMLHttpRequest();
                } catch(noway) {
                    throw new UnsupportedOperationException('Could not find supported version of XMLHttpRequest.');
                }
            }
        }
	}-*/;

}
