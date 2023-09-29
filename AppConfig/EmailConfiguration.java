package eBIS.AppConfig;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {
	
	private String ProductionSpringMailHost=EMailPropertiesBundleFile.getValueFromKey("production.spring.mail.host");
	
	private String ProductionSpringMailPort=EMailPropertiesBundleFile.getValueFromKey("production.spring.mail.port");
	
	private String ProductionSpringMailUsername=EMailPropertiesBundleFile.getValueFromKey("production.spring.mail.username");
	
	private String ProductionSpringMailPassword=EMailPropertiesBundleFile.getValueFromKey("production.spring.mail.password");
	
	private String LocalSpringMailHost=EMailPropertiesBundleFile.getValueFromKey("local.spring.mail.host");
	
	private String LocalSpringMailPort=EMailPropertiesBundleFile.getValueFromKey("local.spring.mail.port");
	
	private String LocalSpringMailUsername=EMailPropertiesBundleFile.getValueFromKey("local.spring.mail.username");
	
	private String LocalSpringMailPassword=EMailPropertiesBundleFile.getValueFromKey("local.spring.mail.password");
	
	@Autowired
	CheckhostIP checkhost;
	@Bean(name="SendMail")
	public JavaMailSender getMailSender() {
		String SpringMailHost="";
		String SpringMailPort="";
		String SpringMailUsername="";
		String SpringMailPassword="";
		JavaMailSenderImpl mailSender =new JavaMailSenderImpl();
		int flag = checkhost.gethost();
		if (flag==1) {
			SpringMailHost=ProductionSpringMailHost;
			SpringMailPort=ProductionSpringMailPort;
			SpringMailUsername=ProductionSpringMailUsername;
			SpringMailPassword=ProductionSpringMailPassword;
		}else {
			SpringMailHost=LocalSpringMailHost;
			SpringMailPort=LocalSpringMailPort;
			SpringMailUsername=LocalSpringMailUsername;
			SpringMailPassword=LocalSpringMailPassword;
		}
        mailSender.setHost(SpringMailHost);
        mailSender.setPort(Integer.valueOf(SpringMailPort));
        mailSender.setUsername(SpringMailUsername);
        mailSender.setPassword(SpringMailPassword);
        System.out.println("MailHost: "+SpringMailHost);
        System.out.println("MailPort: "+SpringMailPort);
        System.out.println("MailUsername: "+SpringMailUsername);
        System.out.println("MailPassword: "+SpringMailPassword);
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");
        javaMailProperties.put("mail.smtp.socketFactory.port", SpringMailPort);
        javaMailProperties.put("mail.mime.charset", "UTF-8");
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.smtp.socketFactory.fallback", "true");
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

}
