package com.mingxing.mtg.mail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 纯 Java SMTP 邮件发送器(基于 SmtpMailer.java 移植)
 * 支持 SMTPS(SSL 465) / SMTP+STARTTLS(587 / 25) / 明文
 * 认证: LOGIN / PLAIN
 */
@Component
public class SmtpMailService {

    private static final Logger log = LoggerFactory.getLogger(SmtpMailService.class);

    @Value("${mail.smtp.host:}")
    private String host;

    @Value("${mail.smtp.port:587}")
    private int port;

    @Value("${mail.smtp.username:}")
    private String username;

    @Value("${mail.smtp.password:}")
    private String password;

    @Value("${mail.smtp.from:}")
    private String from;

    @Value("${mail.smtp.mode:starttls}")
    private String mode;

    /**
     * 发送纯文本邮件
     *
     * @param to      收件人(多个用逗号分隔)
     * @param subject 主题
     * @param body    正文
     * @return 是否发送成功
     */
    public boolean sendTextMail(String to, String subject, String body) {
        return sendMail(to, null, subject, body, "text/plain; charset=UTF-8");
    }

    /**
     * 发送 HTML 邮件
     */
    public boolean sendHtmlMail(String to, String subject, String body) {
        return sendMail(to, null, subject, body, "text/html; charset=UTF-8");
    }

    private boolean sendMail(String to, String cc, String subject, String body, String contentType) {
        if (host == null || host.isEmpty() || username == null || username.isEmpty()
                || password == null || password.isEmpty() || from == null || from.isEmpty()) {
            log.warn("SMTP 配置不完整，邮件未发送。host={}, from={}", host, from);
            return false;
        }
        if (to == null || to.isEmpty()) {
            log.warn("收件人为空，邮件未发送");
            return false;
        }
        try {
            java.security.Security.setProperty("jdk.tls.disabledAlgorithms",
                    "SSLv3, RC4, DES, MD5withRSA, DH keySize < 1024, EC keySize < 224, anon, NULL");
            String mime = buildMime(subject, from, to, cc, body, contentType);
            SmtpConnection conn = new SmtpConnection(host, port, username, password, from, mode);
            conn.connect();
            conn.mail(mime, to, cc);
            conn.quit();
            log.info("邮件发送成功: to={}, subject={}", to, subject);
            return true;
        } catch (Exception e) {
            log.error("邮件发送失败: to={}, subject={}, error={}", to, subject, e.getMessage(), e);
            return false;
        }
    }

    // ---------------- MIME 构造 ----------------

    private static String b64(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static String buildMime(String subject, String from, String to, String cc,
                                    String body, String contentType) {
        StringBuilder m = new StringBuilder();

        m.append("Date: ").append(formatRfc822Date()).append("\r\n");
        m.append("From: ").append(from).append("\r\n");
        m.append("To: ").append(to).append("\r\n");
        if (cc != null && !cc.isEmpty()) {
            m.append("Cc: ").append(cc).append("\r\n");
        }
        m.append("Subject: =?UTF-8?B?").append(b64(subject)).append("?=\r\n");
        m.append("MIME-Version: 1.0\r\n");
        m.append("Message-ID: <").append(System.nanoTime()).append("@localhost>\r\n");
        m.append("Content-Type: ").append(contentType).append("\r\n");
        m.append("Content-Transfer-Encoding: base64\r\n\r\n");
        m.append(base64Chunked(body));
        return m.toString();
    }

    private static String base64Chunked(String s) {
        String b = b64(s);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length(); i += 76) {
            sb.append(b, i, Math.min(i + 76, b.length())).append("\r\n");
        }
        return sb.toString();
    }

    private static String formatRfc822Date() {
        SimpleDateFormat f = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        return f.format(new Date());
    }

    // ---------------- SMTP 客户端 ----------------

    static class SmtpConnection {
        private final String smtpHost, smtpUser, smtpPassword, smtpFrom;
        private final int port;
        private final boolean useSsl;
        private final boolean startTls;
        private Socket socket;
        private BufferedWriter writer;
        private BufferedReader reader;

        SmtpConnection(String host, int port, String user, String password, String from, String mode) throws Exception {
            this.smtpHost = host;
            this.port = port;
            this.smtpUser = user;
            this.smtpPassword = password;
            this.smtpFrom = from;
            this.useSsl = "ssl".equalsIgnoreCase(mode) || port == 465;
            this.startTls = "starttls".equalsIgnoreCase(mode) || (port == 587 && !useSsl);
            socket = useSsl ? openSsl(host, port) : new Socket(host, port);
            socket.setSoTimeout(30000);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1));
        }

        private static SSLContext sslContext() throws Exception {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            return ctx;
        }

        private static Socket openSsl(String host, int port) throws Exception {
            SSLSocket s = (SSLSocket) sslContext().getSocketFactory().createSocket(host, port);
            s.setEnabledProtocols(sslProtocols(s));
            return s;
        }

        private static String[] sslProtocols(SSLSocket s) {
            String[] supported = s.getSupportedProtocols();
            StringBuilder allowed = new StringBuilder();
            for (String p : supported) {
                if (p.startsWith("TLS")) allowed.append(p).append(' ');
            }
            return allowed.toString().trim().split(" ");
        }

        private static class TrustAllManager implements X509TrustManager {
            public void checkClientTrusted(X509Certificate[] c, String a) {}
            public void checkServerTrusted(X509Certificate[] c, String a) {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        }

        private String readReply() throws IOException {
            StringBuilder sb = new StringBuilder();
            String line;
            boolean more = true;
            while (more) {
                line = reader.readLine();
                if (line == null) throw new IOException("连接被服务器关闭");
                sb.append(line).append('\n');
                more = (line.length() >= 4 && line.charAt(3) == '-');
            }
            return sb.toString();
        }

        private void send(String cmd) throws IOException {
            writer.write(cmd + "\r\n");
            writer.flush();
        }

        private void check(int expected, String reply) throws IOException {
            String code = reply.substring(0, 3);
            if (!String.valueOf(expected).equals(code)) {
                throw new IOException("SMTP 服务器返回错误码 " + code + ": " + reply);
            }
        }

        void connect() throws Exception {
            check(220, readReply());
            send("EHLO localhost");
            check(250, readReply());

            if (startTls) {
                send("STARTTLS");
                check(220, readReply());
                upgradeToTls();
                send("EHLO localhost");
                check(250, readReply());
            }

            send("AUTH LOGIN");
            check(334, readReply());
            send(b64(smtpUser));
            check(334, readReply());
            send(b64(smtpPassword));
            check(235, readReply());
        }

        private void upgradeToTls() throws Exception {
            try {
                SSLSocketFactory factory = sslContext().getSocketFactory();
                SSLSocket ssl = (SSLSocket) factory.createSocket(socket, smtpHost, socket.getPort(), true);
                ssl.setSoTimeout(30000);
                ssl.setEnabledProtocols(sslProtocols(ssl));
                ssl.startHandshake();
                this.socket = ssl;
                this.reader = new BufferedReader(new InputStreamReader(ssl.getInputStream(), StandardCharsets.ISO_8859_1));
                this.writer = new BufferedWriter(new OutputStreamWriter(ssl.getOutputStream(), StandardCharsets.ISO_8859_1));
            } catch (SSLException e) {
                throw new IOException("STARTTLS 握手失败: " + e.getMessage(), e);
            }
        }

        void mail(String mime, String to, String cc) throws IOException {
            send("MAIL FROM:<" + extractAddr(smtpFrom) + ">");
            check(250, readReply());

            List<String> rcpts = new ArrayList<>();
            if (to != null) {
                for (String t : to.split("[;, ]+")) {
                    if (!t.isEmpty()) rcpts.add(extractAddr(t));
                }
            }
            if (cc != null) {
                for (String c : cc.split("[;, ]+")) {
                    if (!c.isEmpty()) rcpts.add(extractAddr(c));
                }
            }
            for (String r : rcpts) {
                send("RCPT TO:<" + r + ">");
                check(250, readReply());
            }

            send("DATA");
            check(354, readReply());
            writer.write(mime + "\r\n.\r\n");
            writer.flush();
            check(250, readReply());
        }

        void quit() {
            try {
                send("QUIT");
                readReply();
            } catch (IOException ignored) {}
        }

        private static String extractAddr(String s) {
            int lt = s.indexOf('<');
            int gt = s.indexOf('>');
            if (lt >= 0 && gt > lt) return s.substring(lt + 1, gt);
            return s.trim();
        }
    }
}
