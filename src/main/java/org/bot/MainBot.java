package org.bot;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class MainBot extends TelegramLongPollingBot {

    BotTools botTools = new BotTools();
    private static final Logger LOGGER = Logger.getLogger(MainBot.class.getName());
    public MainBot(String botToken) {
        super(botToken);
        initializeLogger();
    }
    private void initializeLogger() {
        // Create console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());

        // Add console handler to logger
        LOGGER.addHandler(consoleHandler);
        LOGGER.setLevel(Level.ALL);
    }




    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received update: " + update);
        // We check if the update has a message and the message has text
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();

            if (update.getMessage().hasLocation()) {
                // Process location message
                Location location = update.getMessage().getLocation();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

//                update.setRequestLocation(true);
                // Call weather API with latitude and longitude
                String weatherInfo = botTools.getWeatherInfo(longitude, latitude);

                // Send weather information to the user
                sendMessage(chatId, weatherInfo);
            } else if (update.getMessage().hasText()
                    && (update.getMessage().getText().equalsIgnoreCase("/start")
                    || update.getMessage().getText().equalsIgnoreCase("/help"))) {
                // send help window
                String helpMessage = new StringBuilder().append("\uD83C\uDF24").
                        append("Welcome to WeatherBot!\n\n")
                        .append("To get the weather information for a specific city, simply type the name of the city.\nExample: Bukhara\n\n")
                        .append("To share your location and get the weather information or\nuse the /location command and \nclick the 'Share Location' button below. \uD83D\uDC47\uD83D\uDC47\uD83D\uDC47")
                        .toString();

                sendMessage(chatId, helpMessage);

            } else if (update.getMessage().hasText() && update.getMessage().getText().equals("/location")) {
                // Create a ReplyKeyboardMarkup
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);

                    // Create a KeyboardRow
                    KeyboardRow keyboardRow = new KeyboardRow();

                    // Create a KeyboardButton with the request location option
                    KeyboardButton requestLocationButton = new KeyboardButton("Share Location");
                    requestLocationButton.setRequestLocation(true);

                    // Add the button to the KeyboardRow
                    keyboardRow.add(requestLocationButton);

                    // Add the KeyboardRow to the ReplyKeyboardMarkup
                    List<KeyboardRow> keyboardRows = new ArrayList<>();
                    keyboardRows.add(keyboardRow);
                    replyKeyboardMarkup.setKeyboard(keyboardRows);

                    // Send a message with the location request
                    sendMessageWithKeyboard(chatId, "Please share your location:", replyKeyboardMarkup);
            }else {

                // Process text message
                String text = update.getMessage().getText();
                try{
                    // Call weather API with city name
                    String weatherInfo = botTools.getWeatherInfoByCity(text);

                    // Send weather information to the user
                    sendMessage(chatId, weatherInfo);
                }catch (Exception e){
                    sendMessage(chatId,"Please enter the correct city name\nTry again....");
                }

            }
        }
    }

    // send message function
    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // send message with keyboard
    private void sendMessageWithKeyboard(long chatId, String message, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }




    @Override
    public String getBotUsername() {
        return "WeatherBot";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
