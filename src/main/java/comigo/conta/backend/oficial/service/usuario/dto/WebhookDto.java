package comigo.conta.backend.oficial.service.usuario.dto;

import org.springframework.lang.Nullable;

public class WebhookDto {
    @Nullable
    private String webhookUrl;

    public WebhookDto(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public WebhookDto() {
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}
