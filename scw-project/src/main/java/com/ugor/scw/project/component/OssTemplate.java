package com.ugor.scw.project.component;

import java.io.InputStream;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Data
@Slf4j
public class OssTemplate {
	// Endpoint以杭州为例，其它Region请按实际情况填写。
	String endpoint;
	// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录
	// https://ram.console.aliyun.com 创建RAM账号。
	String accessKeyId;
	String accessKeySecret;
	String bucket;
	
	
	public String upload(String fileName, InputStream is) {
		try {
			// 创建OSSClient实例。
			OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

			// 上传文件流。
			ossClient.putObject(bucket, "pic/" + fileName, is);

			// 关闭OSSClient。
			ossClient.shutdown();
				
			String filePath ="https://"+bucket+"."+endpoint+"/pic/"+fileName;
			log.debug("上传成功-{}",filePath);
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("上传失败-{}",fileName);
			return null;
		} 
	}
}
