package org.example;

import org.bot.MainBot;
import org.bot.service.WeatherApiClient;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MainBot("6198483587:AAG-Lb6SL6NypbGDT2ogEC5IxwrQCP8q80A"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}