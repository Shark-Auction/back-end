package com.exe.sharkauction.components.notifications;

import com.exe.sharkauction.components.constants.TemplateMail;
import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.UserEntity;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
@Service
public class ThymeleafService {
    private static final String MAIL_TEMPLATE_BASE_NAME = "mail/MailMessages";
    private static final String MAIL_TEMPLATE_PREFIX = "/templates/";
    private static final String MAIL_TEMPLATE_SUFFIX = ".html";
    private static final String UTF_8 = "UTF-8";
    private static TemplateEngine templateEngine;

    static {
        templateEngine = emailTemplateEngine();
    }

    private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }

    private static ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    public String getVerifyContent(UserEntity user, String url) {
        final Context context = new Context();

        context.setVariable("firstName", user.getEmail());
        context.setVariable("url", url);

        return templateEngine.process(TemplateMail.VERIFY_MAIL, context);
    }

    public String getResetPasswordContent(UserEntity user, String url) {
        final Context context = new Context();

        context.setVariable("firstName", user.getEmail());
        context.setVariable("url", url);

        return templateEngine.process(TemplateMail.RESET_PASSWORD_MAIL, context);
    }

    public String getAuctionEndContent(AuctionEntity auction) {
        final Context context = new Context();

        context.setVariable("sellerName", auction.getProduct().getSeller().getFull_name());
        context.setVariable("productName", auction.getProduct().getName());
        context.setVariable("winnerName", auction.getWinner().getFull_name());
        context.setVariable("finalBidPrice", auction.getCurrentPrice());
        context.setVariable("status", auction.getStatus());

        context.setVariable("dashboardUrl", "http://sharkauction.online/");

        return templateEngine.process(TemplateMail.AUCTION_END_MAIL, context);
    }
    public String getAuctionWinContent(AuctionEntity auction) {
        final Context context = new Context();

        context.setVariable("sellerName", auction.getProduct().getSeller().getFull_name());
        context.setVariable("productName", auction.getProduct().getName());
        context.setVariable("winnerName", auction.getWinner().getFull_name());
        context.setVariable("finalBidPrice", auction.getCurrentPrice());
        context.setVariable("status", auction.getStatus());
        context.setVariable("dashboardUrl", "http://sharkauction.online/");
        return templateEngine.process(TemplateMail.AUCTION_WIN_MAIL, context);
    }

    public String getConfirmProductContent(ProductEntity product) {
        final Context context = new Context();

        context.setVariable("sellerName", product.getSeller().getFull_name());
        context.setVariable("productName", product.getName());
        context.setVariable("dashboardUrl", "http://sharkauction.online/");
        return templateEngine.process(TemplateMail.CONFIRM_PRODUCT, context);
    }

}
