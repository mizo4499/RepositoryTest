import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTPClient;

public class main {

	public static void main(String[] args) {

		String localPath = "C:\\temp\\upload";
		String ipaddr = "192.168.11.101";
		int port = 21;
		String user = "test";
		String passwd = "test";
		String relativePath = "test_dir/";
		String tempExtension = ".tmp";

		try {
			// ローカルファイル取得
		    File path = new File(localPath);
		    File[] localFiles =  path.listFiles();

		    // FTP接続
			FTPClient ftpclient = new FTPClient();
			ftpclient.setDataTimeout(3600000);
			ftpclient.connect(ipaddr, port);
			ftpclient.login(user, passwd);

			// ローカルファイルに一時拡張子(.tmp)を付与してアップロード
			int fileNum = localFiles.length;
		    for (int i = 0; i < fileNum; i++) {
		    	if(localFiles[i].isFile()) {
		    		ftpclient.storeFile(relativePath + localFiles[i].getName() + tempExtension, new FileInputStream(localFiles[i]));
		    	}
		    }

		    // アップロードファイルから一時拡張子(.tmp)を切り取る
		    String[] remoteFiles = ftpclient.listNames(relativePath);
		    for(int i = 0; i < remoteFiles.length; i++) {
		    	if(remoteFiles[i].endsWith(tempExtension)) {
		    		ftpclient.rename(remoteFiles[i], remoteFiles[i].substring(0, remoteFiles[i].length() - tempExtension.length()));
		    	}
		    }

		    // FTP切断
	        ftpclient.logout();
	        ftpclient.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
