package lucky.yc.community.provider;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import lucky.yc.community.dto.FileDTO;
import lucky.yc.community.exception.CustomizeErrorCode;
import lucky.yc.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class AliyunProvider {
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    @Value("${aliyun.endpoint}")
    private String endpoint;
    @Value("${aliyun.bucketname}")
    private String bucketName;
    @Value("${aliyun.accesskeysecret}")
    private String accessKeySecret;
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    @Value("${aliyun.accesskeyid}")
    private String accessKeyId;

    /**
     * 上传到oss
     * @param mimeType   文件类型
     * @param fileStream 上传文件的数据流
     * @param fileName   文件名
     * @return
     */
    public String upload(String mimeType, InputStream fileStream, String fileName) {

//        文件命名
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            return null;
        }

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        // 填写Bucket名称、Object完整路径和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, generatedFileName, fileStream);

        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();

        try {
            // 创建OSSClient实例。
            OSS ossClient1 = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 将图片缩放为固定宽高100 px后，再旋转90°。
            String style = "image/resize,m_fixed,w_200,h_100/rotate,90";
            // 指定签名URL过期时间为一天。
            Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 * 144);
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, generatedFileName, HttpMethod.GET);

            if (req != null && req.getKey() != null) {
                req.setExpiration(expiration);
//                req.setProcess(style);
                String signedUrl = String.valueOf(ossClient1.generatePresignedUrl(req));
                System.out.println("图片链接：" + signedUrl);
                // 关闭OSSClient。
                ossClient1.shutdown();
                return signedUrl;
            } else {
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        } catch (ClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (CustomizeException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }

    }

}
