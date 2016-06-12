package limeng32.mirage.account.service;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;

public class AliyunForAccount {

	private static AliyunForAccount aliyun = new AliyunForAccount();
	private String accessKeyId;
	private String accessKeySecret;
	private String ossBucket;
	private String ossEndpoint;
	private String ossFilepath;
	private String ossPortraitlayout;
	private static OSSClient ossClient;

	private AliyunForAccount() {
		super();
	}

	public static AliyunForAccount getInstance() {
		return aliyun;
	}

	public void initOSS() {
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(5000);
		conf.setMaxErrorRetry(10);
		ossClient = new OSSClient(ossEndpoint, accessKeyId, accessKeySecret,
				conf);
	}

	public static OSSClient getOSSClient() {
		return ossClient;
	}

	public String getOssBucket() {
		return ossBucket;
	}

	public String getOssEndpoint() {
		return ossEndpoint;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public void setOssBucket(String ossBucket) {
		this.ossBucket = ossBucket;
	}

	public void setOssEndpoint(String ossEndpoint) {
		this.ossEndpoint = ossEndpoint;
	}

	public String getOssFilepath() {
		return ossFilepath;
	}

	public void setOssFilepath(String ossFilepath) {
		this.ossFilepath = ossFilepath;
	}

	public String getOssPortraitlayout() {
		return ossPortraitlayout;
	}

	public void setOssPortraitlayout(String ossPortraitlayout) {
		this.ossPortraitlayout = ossPortraitlayout;
	}

	public String ossUrl(String fileName) {
		return "http://" + ossBucket + "." + ossFilepath + "/" + fileName;
	}

	public String buildPortraitFileName(String normalFileName) {
		if (ossPortraitlayout != null && !"".equals(ossPortraitlayout)) {
			return ossPortraitlayout + "/" + normalFileName;
		} else {
			return normalFileName;
		}
	}
}
