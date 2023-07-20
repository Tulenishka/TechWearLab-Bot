package com.example.TechWearBot.service;


import com.example.TechWearBot.config.BotConfig;
import com.example.TechWearBot.model.LotteryTable.Lottery;
import com.example.TechWearBot.model.LotteryTable.LotteryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private LotteryRepository lotteryRepository;
    final BotConfig config;

    static final String helpText = "Этот бот поможет Вам подобрать товар или принять участие в розыгрыше от Techwear Lab.\n\n" +
            "Вы можете выполнить команды, выбрав их в меню слева, или написав команду боту: \n\n" +
            "Для получения подборки одежды Вам необходимо ввести Ваши параметры, это можно сделать отправив сюда команду *кнопка ввода параметров*.\n\n" +
            "Команда для изменения параметров: *кнопка изм. параметров*.\n\n" +
            "Для регистрации в розыгрыше Вы можите использовать команду /lottery (необходимое условие - быть участником оффициального канала Teckwear Lab: t.me/TechWearLab).\n\n" +
            "Команда для показа номера участника: /ticket. \n\n" +
            "Команда для показа времени до проведения розыгрыша: /lotterytime \n\n" +
            "Любые другие вопросы можно задать консультанту: *Ссылка на живого человека*";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start","Получить преветственное сообщение"));
        listOfCommands.add(new BotCommand("/lottery","Получить номер участника для розыгрыша"));
        listOfCommands.add(new BotCommand("/ticket","Показать Ваш номер участника розыгрыша"));
        listOfCommands.add(new BotCommand("/help","Пояснительаня информация"));
        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            log.error("Ошибка создания меню:" + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken(){
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {

                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "/help":
                    helpMessage(chatId);
                    break;

                case "/lottery":
                    registerLottery(update.getMessage());
                    break;

                case "/ticket":
                    sendTicket(update.getMessage());
                    break;

                case "/":

                    break;

                default:
                        sendMessage(chatId, "Неизвестная команда, проверьте правильность написания в /help");
            }
        }
    }

    private void sendTicket(Message message) {
        if(lotteryRepository.findById(message.getChatId()).isEmpty()){
            var chatId = message.getChatId();
            sendMessage(chatId, "Для получения билета необходимо зарегистрироваться с помощью команды /lottery");
        } else {
            var chatId = message.getChatId();
            log.info( "selected: " + lotteryRepository.getTicket(chatId));
            sendMessage(chatId, "Номер Вашего билета: "+ lotteryRepository.getTicket(chatId));
        }
    }


    private void registerLottery(Message message) {
       if(lotteryRepository.findById(message.getChatId()).isEmpty()){
            var chatId = message.getChatId();
            var chat = message.getChat();
            Lottery lottery = new Lottery();
            lottery.setChatId(chatId);
            lottery.setUserName(chat.getUserName());
            lottery.setTicket(lotteryRepository.getMaxTicket() + 1);
            lotteryRepository.save(lottery);
            log.info("Билет участника сохранен: " + lottery);
            sendMessage(chatId, "Вы зарегистрировались, номер Вашего билета: " + lotteryRepository.getTicket(chatId));
            log.info("selected: " + lotteryRepository.getTicket(chatId));
        } else {
            var chatId = message.getChatId();
            sendMessage(chatId, "Вы уже участвуете в розыгрыше, номер Вашего билета: "+ lotteryRepository.getTicket(chatId));
            log.info("Пользователь уже был");
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Ну привет, " + name + ", наконец-то кто-то дробрался пощупать бота";
        sendMessage(chatId, answer);
        log.info("Приветственное сообщение доставлено:" + name);
    }
    private void helpMessage(long chatId){
        sendMessage(chatId,helpText);
        log.info("Пояснительная информация доставлена.");
    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try{
            execute(message);
        }
        catch (TelegramApiException e){
            log.error("Ошибка:" + e.getMessage());
        }
    }
}
