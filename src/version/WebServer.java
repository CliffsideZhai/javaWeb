package version;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;

public class WebServer {
	static Integer bufferSize = 1024;
	public static String findType(String fileTpye) {
		String type = fileTpye.substring(fileTpye.lastIndexOf(".")+1);
		if(type == "html") {
			return "text/html";
		}else if(type == "jpg") {
			return "image/jpeg";
		}else if(type == "png") {
			return "image/png";
		}else {
			return "";
		}
	}
	public static void outputDate(File file,String repsonsedHead,PrintStream output,FileInputStream fis) throws IOException {
		byte[] b = new byte[bufferSize];
		output.write(repsonsedHead.getBytes());
	
		int len;

		while((len = fis.read(b)) != -1){
			output.write(b,0,len);
		}
		
	}
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {

		ServerSocket server = new ServerSocket(9996);

		System.out.println("服务器建立完成");

		while (true) {

			Socket socket = server.accept();
			
			System.err.println("接收到请求！");
			InputStreamReader isr = new InputStreamReader(socket.getInputStream(),"UTF-8");
			BufferedReader input = new BufferedReader(isr);
			
			StringBuilder sb = new StringBuilder();
			
			char[] buffer = new char[bufferSize];
			
			Integer count = bufferSize;
			
			while (count.equals(bufferSize)) {
				count = input.read(buffer);
				sb.append(buffer);
			}
			//System.out.println(sb);			
			String[] array = (""+sb).split(" ");
			String filePath = array[1].substring(1);
			System.out.println(filePath);
			System.err.println("获得的请求报文如下：\n"+sb);

			PrintStream output = new PrintStream(socket.getOutputStream());

			System.out.println("开始响应请求");
			
			File file = new File(filePath);
			
			File page404 = new File("C:\\Java\\sevrver\\javaWeb\\page404.html");
			FileInputStream fis = null;
			String repsonsedHead;
			try {
				if(file.isDirectory()) {
					File[] files = file.listFiles();
					
					if(files.length == 0) {
						fis = new FileInputStream(page404);
						
						repsonsedHead = " HTTP/1.1 200 OK \r\n" + "Content-Type:text/html\r\n" + "\r\n";
						outputDate( file, repsonsedHead, output, fis);
						output.print("没有文件");
					}else {
						for(int i=0;i<files.length;i++) {
							
						fis = new FileInputStream(files[i]);
						System.out.println(files[i].getAbsolutePath().toString());
						repsonsedHead = "HTTP/1.1 200 OK \r\n" + "Content-Type:"+findType(files[1].getName())+"\r\n" + "\r\n";
						outputDate( file, repsonsedHead, output, fis);
						output.print("是一个文件目录");
						}
					}				
				}else {
					fis = new FileInputStream(file);
				
					repsonsedHead = "HTTP/1.1 200 OK \r\n" + "Content-Type:"+findType(file.getName())+"\r\n" + "\r\n";
					outputDate( file, repsonsedHead, output, fis);
					output.print("找到了文件");
				}
			}catch(IOException e) {
				
				fis = new FileInputStream(page404);
				
				repsonsedHead = "HTTP/1.1 200 OK \r\n" + "Content-Type:text/html\r\n" + "\r\n";
				outputDate( file, repsonsedHead, output, fis);
				output.print("错误");
			}finally {	
			output.close();
			System.err.println("成功响应本次请求\n\n\n");
			}
		}

	}
}