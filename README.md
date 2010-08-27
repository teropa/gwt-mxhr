# gwt-mxhr

Gwt-mxhr is a  GWT port of the [DUI.Stream](http://github.com/digg/stream) library.

It leverages a technique called Multipart XMLHttpRequest (described in a [blog post by the Digg guys](http://about.digg.com/blog/duistream-and-mxhr))
which is useful in situations where a large amount of dynamic resources (images, CSS, JavaScript, ect.) need to 
be fetched from the server. It eliminates the HTTP overhead by streaming the resources back in
a single HTTP response.

## Installation

There are no releases yet, so you'll have to build the thing yourself.

1. Clone the git repo
1. `mvn package` in the project directory
1. Copy `target/mxhr-0.0.1-SNAPSHOT.jar` to your project libraries
1. Add the following to your GWT XML descriptor:
    
        <inherits name='teropa.mxhr.GwtMxhr'/>

## Usage

See [Demo.java](http://github.com/teropa/gwt-mxhr/blob/master/src/teropa/mxhr/client/Demo.java)

    MXHR request = new MXHR();
    
    request.addContentReceivedHandler(new ContentReceivedHandler() {
      public void onContentReceived(ContentReceivedEvent evt) {
        // do stuff with evt.mimeType and evt.payload
      }
    });
    
    request.addContentCompleteHandler(new ContentCompleteHandler() {
      public void onContentComplete(ContentCompleteEvent evt) {
        // everything received!
      }
    });
    
    request.load("/some/url");
    
### Server-side

The server must have a service that is capable of sending back multipart responses.
The DUI.Stream repo has [examples](http://github.com/digg/stream/tree/master/examples/) in Java, Perl, Python, and Ruby.

## Hacking

Just clone the Git repo and import it to your Eclipse workspace. 
You should have [Google Plugin for Eclipse](http://code.google.com/eclipse/) installed.

## License

Copyright (c) 2010 Tero Parviainen

Logic based on DUI.Stream, Copyright (c) 2009, Digg, Inc.

MIT license (see [LICENSE](http://github.com/teropa/globetrotter/blob/master/LICENSE)).
