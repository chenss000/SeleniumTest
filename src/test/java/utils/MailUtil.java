package utils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MailUtil {
    public static void sendReportMail(String reportPath) {

        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            LogUtil.error("报告文件不存在：" + reportPath);
            return;
        }

        Map<String, Object> mailConfig = YamlUtil.getMailConfig();
        if (mailConfig == null) {
            LogUtil.error("无法加载邮件配置，配置文件为空！");
            return;
        }

        String from = (String) mailConfig.get("from");
        List<String> toList = (List<String>) mailConfig.get("to");
        String host = (String) mailConfig.get("host");
        String user = (String) mailConfig.get("user");
        String password = (String) mailConfig.get("password");
        int port = (int) mailConfig.get("port");
        boolean ssl = (boolean) mailConfig.get("ssl");
        String protocols = (String) mailConfig.get("protocols");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.enable", String.valueOf(ssl));
        props.put("mail.smtp.ssl.protocols", protocols);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            // 多个收件人
            InternetAddress[] addresses = toList.stream()
                    .map(email -> {
                        try {
                            return new InternetAddress(email);
                        } catch (AddressException e) {
                            LogUtil.error("无效邮箱地址: " + email);
                            return null;
                        }
                    })
                    .filter(addr -> addr != null)
                    .toArray(InternetAddress[]::new);

            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject("自动化测试报告");

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent("你好，请查收测试报告，如附件所示。", "text/html;charset=UTF-8");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(reportPath);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName("ExtentReport.html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            Transport.send(message);
            LogUtil.info("测试报告邮件发送成功！");
        } catch (MessagingException e) {
            e.printStackTrace();
            LogUtil.error("邮件发送失败：" + e.getMessage());
        }
    }
}