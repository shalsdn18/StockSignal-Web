package com.stocksignal.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "system_config")
@Getter @Setter
public class SystemConfig {
    @Id
    private Long id = 1L;
    private String telegramBotToken;
    private String telegramChatId;
    private String tossClientId;
    private String tossClientSecret;
    private String tossAccountSeq = "1";
}
