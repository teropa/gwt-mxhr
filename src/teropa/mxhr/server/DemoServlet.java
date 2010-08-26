package teropa.mxhr.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("serial")
public class DemoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("MIME-Version", "1.0");
		resp.setContentType("multipart/mixed; boundary=\"|||\"");
		
		Writer writer = resp.getWriter();
		
		String payload = new String(Base64.encodeBase64(getImage()));
		
		for (int i=0 ; i<300 ; i++) {
			writer.write("\n--|||\n");
			writer.write("Content-Type: image/gif\n");
			writer.write(payload);
			writer.flush();
		}
		writer.write("\n--|||--\n");
		writer.flush();
		writer.close();
		
	}

	private byte[] getImage() throws IOException {
		File f = new File("32x32-digg-guy.gif");
		byte[] data = new byte[(int)f.length()];
		FileInputStream fs = new FileInputStream(f);
		fs.read(data);
		fs.close();
		return data;
	}
	
}
