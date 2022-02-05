package com.cc.lab_teach.util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.StringWriter;
import java.util.UUID;

public class OfficeConvertUtil {
    private static final Logger LOG = LoggerFactory.getLogger(OfficeConvertUtil.class);

    // doc转换为html
    public static String docToHtml(MultipartFile file) throws Exception {
        LOG.info("正在处理：将 .doc 文件装换成 .html 格式");
        // 图片的存储路径
        String image_path = ResourceUtils.getURL("classpath:").getPath() + "static/image/doc/";

        File newFile = new File(image_path);
        // 如果文件夹不存在、则新建
        if (!newFile.exists()) newFile.mkdirs();

        HWPFDocument wordDocument = new HWPFDocument(file.getInputStream());
        org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
        //保存图片，并返回图片的相对路径
        wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
            String suffix = name.substring(name.lastIndexOf("."));
            String filename = UUID.randomUUID().toString().replaceAll("-", "");
            try (FileOutputStream out = new FileOutputStream(image_path + filename + suffix)) {
                out.write(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOG.info("处理图片文件，储存路径为: image/doc/{}", filename + suffix);
            return "image/doc/" + filename + suffix;
        });
        // 处理 word 文件
        wordToHtmlConverter.processDocument(wordDocument);
        org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);
        // 以 字符串流 的形式，暂存与 内存 中
        StreamResult streamResult = new StreamResult(new StringWriter());
        // 转换成 html 格式
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);

        return streamResult.getWriter().toString();
    }

    // docx转换为html
    public static String docxToHtml(MultipartFile file) throws Exception {
        LOG.info("正在处理：将 .docx 文件装换成 .html 格式");
        // 图片的存储路径
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String image_path = ResourceUtils.getURL("classpath:").getPath() + "static/image/docx/" + uuid;

        File newFile = new File(image_path);
        // 如果文件夹不存在、则新建
        if (!newFile.exists()) newFile.mkdirs();

        StringWriter stringWriter = null;
        String htmlContent = null;
        try {
            XWPFDocument document = new XWPFDocument(file.getInputStream());
            XHTMLOptions options = XHTMLOptions.create();
            // 存放图片的文件夹
            options.setExtractor(new FileImageExtractor(new File(image_path)));
            // html中图片的路径
            options.URIResolver(new BasicURIResolver("image/docx/" + uuid));
            stringWriter = new StringWriter();
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, stringWriter, options);

            htmlContent = stringWriter.toString();
        } finally {
            if (stringWriter != null) {
                stringWriter.close();
            }
        }
        return htmlContent;
    }
}