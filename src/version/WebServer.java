package version;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;
public class WebServer {

	public static void main(String[] args) {
		
		File htmlFile = new File("web01.html");
		ServerSocket server = null;
		try {
			server = new ServerSocket(9999);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("服务器建立完成");
		while (true) {
			Socket socket = null;
			try {
				socket = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.err.println("接收到请求！");
			BufferedReader input = null;
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			Integer bufferSize = 1024;
			char[] buffer = new char[bufferSize];
			Integer count = bufferSize;
			while (count.equals(bufferSize)) {
				try {
					count = input.read(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sb.append(buffer);
			}
			System.err.println("获得的请求报文如下：\n"+sb);
			PrintStream output = null;
			try {
				output = new PrintStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("开始响应请求");

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(htmlFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] b = new byte[1024];

			int len;

			try {
				while((len = fis.read(b)) != -1){
					output.write(b,0,len);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			output.print("MSS Studio");

			
			try {
				fis.close();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.err.println("成功响应本次请求\n\n\n");
		}
	}
}
