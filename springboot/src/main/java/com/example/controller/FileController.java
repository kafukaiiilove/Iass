package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/api/manager/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    // 文件上传存储路径
    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.upload.markdown.path}")
    private String mdPath;

    // 图片最大宽度
    private static final int MAX_WIDTH = 800;
    // 图片最大高度
    private static final int MAX_HEIGHT = 600;
    // 图片质量（0-1）
    private static final float QUALITY = 0.8f;

    @Value("${server.port:9090}")
    private String port;

    @Value("${ip:localhost}")
    private String ip;

    /**
     * 获取markdown文件存储路径
     */
    public String getMdPath() {
        return mdPath;
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        System.out.println("开始处理文件上传请求...");
        String flag;
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        }
        String fileName = file.getOriginalFilename();
        System.out.println("原始文件名: " + fileName);
        
        try {
            // 检查文件名是否为空
            if (StrUtil.isBlank(fileName)) {
                System.out.println("文件名不能为空");
                return Result.error("500", "文件名不能为空");
            }

            // 检查文件是否为空
            if (file.isEmpty()) {
                System.out.println("文件为空");
                return Result.error("500", "文件为空");
            }

            // 检查文件大小
            long fileSize = file.getSize();
            System.out.println("文件大小: " + fileSize + " bytes");
            if (fileSize > 5 * 1024 * 1024) { // 5MB
                System.out.println("文件大小超过限制: " + fileSize);
                return Result.error("500", "文件大小不能超过5MB");
            }

            if (!FileUtil.isDirectory(filePath)) {
                System.out.println("创建文件目录: " + filePath);
                FileUtil.mkdir(filePath);
            }

            // 检查是否为图片文件
            if (isImageFile(fileName)) {
                System.out.println("开始处理图片文件...");
                try {
                    // 处理图片文件
                    System.out.println("读取图片文件...");
                    BufferedImage originalImage = ImageIO.read(file.getInputStream());
                    if (originalImage == null) {
                        System.out.println("无效的图片文件");
                        return Result.error("500", "无效的图片文件");
                    }

                    System.out.println("原始图片尺寸: " + originalImage.getWidth() + "x" + originalImage.getHeight());

                    // 计算新的尺寸
                    int newWidth = originalImage.getWidth();
                    int newHeight = originalImage.getHeight();
                    if (newWidth > MAX_WIDTH) {
                        newHeight = (int) (newHeight * ((float) MAX_WIDTH / newWidth));
                        newWidth = MAX_WIDTH;
                    }
                    if (newHeight > MAX_HEIGHT) {
                        newWidth = (int) (newWidth * ((float) MAX_HEIGHT / newHeight));
                        newHeight = MAX_HEIGHT;
                    }
                    System.out.println("调整后的图片尺寸: " + newWidth + "x" + newHeight);

                    // 创建新的图片
                    System.out.println("创建新的图片...");
                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = resizedImage.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                    g.dispose();

                    // 保存压缩后的图片
                    String fullFileName = flag + "-" + fileName;
                    System.out.println("保存文件: " + fullFileName);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    String formatName = getFileExtension(fileName);
                    System.out.println("图片格式: " + formatName);
                    
                    if (!ImageIO.write(resizedImage, formatName, baos)) {
                        System.out.println("图片格式不支持: " + formatName);
                        return Result.error("500", "图片格式不支持");
                    }
                    
                    FileUtil.writeBytes(baos.toByteArray(), filePath + fullFileName);
                    System.out.println("文件保存成功");

                    String http = "http://" + ip + ":" + port + "/api/manager/files/";
                    String fullUrl = http + fullFileName;
                    System.out.println("返回完整URL: " + fullUrl);
                    return Result.success(fullUrl);
                } catch (Exception e) {
                    System.err.println("图片处理失败: " + e.getMessage());
                    e.printStackTrace();
                    return Result.error("500", "图片处理失败: " + e.getMessage());
                }
            } else {
                System.out.println("处理非图片文件...");
                // 处理非图片文件
                String fullFileName = flag + "-" + fileName;
                System.out.println("保存文件: " + fullFileName);
                FileUtil.writeBytes(file.getBytes(), filePath + fullFileName);
                System.out.println("文件保存成功");

                String http = "http://" + ip + ":" + port + "/api/manager/files/";
                String fullUrl = http + fullFileName;
                System.out.println("返回完整URL: " + fullUrl);
                return Result.success(fullUrl);
            }
        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("500", "文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 判断是否为图片文件
     */
    private boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || 
               extension.equals("png") || extension.equals("gif");
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件
     *
     * @param flag
     * @param response
     */
    @GetMapping("/{flag}")   //  1697438073596-avatar.png
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(flag)) {
                System.out.println("Accessing file: " + flag);
                System.out.println("Full file path: " + filePath + flag);
                
                // 获取文件扩展名
                String extension = flag.substring(flag.lastIndexOf(".") + 1).toLowerCase();
                String contentType;
                switch (extension) {
                    case "jpg":
                    case "jpeg":
                        contentType = "image/jpeg";
                        break;
                    case "png":
                        contentType = "image/png";
                        break;
                    case "gif":
                        contentType = "image/gif";
                        break;
                    default:
                        contentType = "application/octet-stream";
                }
                
                response.setContentType(contentType);
                response.setHeader("Cache-Control", "max-age=31536000"); // 缓存一年
                byte[] bytes = FileUtil.readBytes(filePath + flag);
                if (bytes == null || bytes.length == 0) {
                    System.err.println("File not found or empty: " + filePath + flag);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                System.out.println("File served successfully: " + flag);
            }
        } catch (Exception e) {
            System.err.println("File access error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param flag
     */
    @DeleteMapping("/{flag}")
    public void delFile(@PathVariable String flag) {
        FileUtil.del(filePath + flag);
        System.out.println("删除文件" + flag + "成功");
    }

    /**
     * 保存markdown文件
     */
    public String saveMarkdownFile(String username, String title, String content) {
        try {
            // 确保目录存在
            String baseDir = mdPath;
            File dir = new File(baseDir);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    log.error("创建目录失败: {}", baseDir);
                    throw new CustomException("500", "创建markdown文件目录失败");
                }
                log.info("创建目录成功: {}", baseDir);
            }

            // 检查目录权限
            if (!dir.canWrite()) {
                log.error("目录没有写入权限: {}", baseDir);
                throw new CustomException("500", "markdown目录没有写入权限");
            }

            // 创建用户目录
            String userDir = baseDir + "/" + username;
            File userDirFile = new File(userDir);
            if (!userDirFile.exists()) {
                boolean userDirCreated = userDirFile.mkdirs();
                if (!userDirCreated) {
                    log.error("创建用户目录失败: {}", userDir);
                    throw new CustomException("500", "创建用户markdown目录失败");
                }
                log.info("创建用户目录成功: {}", userDir);
            }

            // 清理文件名，移除特殊字符
            String cleanTitle = title.replaceAll("[\\\\/:*?\"<>|]", "_");
            // 生成文件名，添加时间戳避免重复
            String timestamp = System.currentTimeMillis() + "";
            String fileName = cleanTitle + "_" + timestamp + ".md";
            String filePath = userDir + "/" + fileName;
            File file = new File(filePath);

            // 检查文件是否已存在
            if (file.exists()) {
                log.warn("文件已存在，将被覆盖: {}", filePath);
            }

            // 写入文件
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
                log.info("成功保存markdown文件: {}", filePath);
            } catch (IOException e) {
                log.error("写入markdown文件失败: {}", filePath, e);
                throw new CustomException("500", "写入markdown文件失败: " + e.getMessage());
            }

            // 返回相对路径，格式为：/markdown/username/filename
            String relativePath = "/markdown/" + username + "/" + fileName;
            log.info("生成文件相对路径: {}", relativePath);
            return relativePath;
        } catch (Exception e) {
            log.error("保存markdown文件失败", e);
            throw new CustomException("500", "保存markdown文件失败: " + e.getMessage());
        }
    }

    /**
     * 下载markdown文件
     */
    @GetMapping("/markdown/{username}/{filename}")
    public void downloadMarkdown(@PathVariable String username, @PathVariable String filename, HttpServletResponse response) {
        try {
            String filePath = mdPath + "/" + username + "/" + filename;
            File file = new File(filePath);
            if (!file.exists()) {
                log.error("文件不存在: {}", filePath);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.setContentType("text/markdown");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()));
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            byte[] bytes = FileUtil.readBytes(file);
            OutputStream os = response.getOutputStream();
            os.write(bytes);
            os.flush();
            os.close();
            log.info("成功下载markdown文件: {}", filePath);
        } catch (Exception e) {
            log.error("下载markdown文件失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}

