package com.example.TechWearBot.service;


import com.example.TechWearBot.config.BotConfig;
import com.example.TechWearBot.model.LotteryTableStatus.LotteryStatus;
import com.example.TechWearBot.model.LotteryTableStatus.LotteryStatusRepository;
import com.example.TechWearBot.model.UserLotteryTable.Lottery;
import com.example.TechWearBot.model.UserLotteryTable.LotteryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private LotteryRepository lotteryRepository;
    @Autowired
    private LotteryStatusRepository lotteryStatusRepository;
    final BotConfig config;

    static final String helpText = "Этот бот поможет Вам подобрать товар или принять участие в розыгрыше от Techwear Lab.\n\n" +
            "Вы можете выполнить команды, выбрав их в меню слева, или написав команду боту: \n\n" +
            "Для получения подборки одежды Вам необходимо ввести Ваши параметры, это можно сделать отправив сюда команду *кнопка ввода параметров*.\n\n" +
            "Команда для изменения параметров: *кнопка изм. параметров*.\n\n" +
            "Для регистрации в розыгрыше Вы можите использовать команду /lottery (необходимое условие - быть участником оффициального канала Teckwear Lab: t.me/TechWearLab).\n\n" +
            "Команда для показа номера участника: /ticket. \n\n" +
            "Команда для показа времени до проведения розыгрыша: /lotterytime \n\n" +
            "Любые другие вопросы можно задать консультанту: @VladislavTechWear";

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
            if (messageText.contains("/setlotterydata")) {
                var data = messageText.substring(messageText.indexOf(" "));
                saveData(data,update);
            } else if (messageText.contains("/setwinner")) {
                var winTicket = messageText.substring(messageText.indexOf(" "));
                var trueTicket = Integer.valueOf(winTicket.substring(1));
                sendMessage(chatId, "Билет:" + trueTicket);
                selectWinner(trueTicket,chatId);
            } else{
                switch (messageText) {
                    case "/start":
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;

                    case "/help":
                        helpMessage(chatId);
                        break;

                    case "/lottery":
                        User user = update.getMessage().getFrom();
                        var userId = user.getId();
                        GetChatMember getMember = new GetChatMember();
                        getMember.setUserId(userId);
                        getMember.setChatId("-1001871441739");
                        ChatMember theChatMember;
                        try {
                            theChatMember = execute(getMember);
                            if ("left".equalsIgnoreCase(theChatMember.getStatus())) {
                                sendMessage(chatId, "Для участия в розыгрыше Вам необходимо подписаться на канал t.me/TechWearLab");
                            } else {
                                registerLottery(update.getMessage());
                            }
                        } catch (TelegramApiException e) {
                            log.error("Ошибка:" + e.getMessage());
                        }
                        break;

                    case "/ticket":

                            sendTicket(update.getMessage());

                        break;

                    case "/setlottery":
                        createLottery(chatId);
                        break;

                    default:
                        sendMessage(chatId, "Неизвестная команда, проверьте правильность написания в /help");
                }
            }

        }
    }

    private void selectWinner(int trueTicket,Long chatId) {
        if (lotteryStatusRepository.getActive() != null && lotteryStatusRepository.getActive()) {
            LotteryStatus lotteryStatus = new LotteryStatus();
            var creatorId = lotteryStatusRepository.getCreatorId();
            var day = lotteryStatusRepository.getDay();
            var month = lotteryStatusRepository.getMonth();
            var hour = lotteryStatusRepository.getHour();
            var minute = lotteryStatusRepository.getMinute();
            lotteryStatus.setLotteryId(1);
            lotteryStatus.setLotteryActive(true);
            lotteryStatus.setLotteryDateDay(day);
            lotteryStatus.setLotteryDateMonth(month);
            lotteryStatus.setLotteryDateHour(hour);
            lotteryStatus.setLotteryDateMinute(minute);
            lotteryStatus.setLotteryCreatorId(creatorId);
            lotteryStatus.setLotteryWinnerTicket(trueTicket);
            lotteryStatusRepository.save(lotteryStatus);
//        return lotteryStatus;
        } else {
            sendMessage(chatId, "Розыгрыш не создан");
        }
    }

    private void saveData(String data,Update update){
        User user = update.getMessage().getFrom();
        var userId = user.getId();
        LotteryStatus lotteryStatus = new LotteryStatus();
        var day = data.substring(1,3);
        var month = data.substring(3,5);
        var hour = data.substring(5,7);
        var minute = data.substring(7);
        lotteryStatus.setLotteryId(1);
        lotteryStatus.setLotteryActive(true);
        lotteryStatus.setLotteryCreatorId(userId);
        lotteryStatus.setLotteryDateDay(Integer.valueOf(day));
        lotteryStatus.setLotteryDateMonth(Integer.valueOf(month));
        lotteryStatus.setLotteryDateHour(Integer.valueOf(hour));
        lotteryStatus.setLotteryDateMinute(Integer.valueOf(minute));
        lotteryStatusRepository.save(lotteryStatus);
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
            if (lotteryStatusRepository.getActive() != null && lotteryStatusRepository.getActive()) {
            if (lotteryRepository.findById(message.getChatId()).isEmpty()) {
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
                sendMessage(chatId, "Вы уже участвуете в розыгрыше, номер Вашего билета: " + lotteryRepository.getTicket(chatId));
                sendMessage(chatId, "Время сообщения: " + message.getDate());
                log.info("Пользователь уже был");
            }
       } else {
            var chatId = message.getChatId();
            sendMessage(chatId, "В данный момент нет активного розыгрыша");
        }
    }


    public void createLottery(long chatId){
        LotteryStatus lotteryStatus = new LotteryStatus();
        lotteryStatus.setLotteryId(1);
        lotteryStatusRepository.save(lotteryStatus);
        sendMessage(chatId, "Для записи даты проведения розыгрыша одновременно введите команду /setdata , дату: (день, месяц) и  время: (час, минута) проведения розыгрыша:\n"+
                "К примеру дата 4 сентября 08:05 следует записать как:\n"+
                "День - 04 \n"+
                "Месяц - 09 \n"+
                "Час - 08 \n"+
                "Минута - 05 \n\n" +
                "Тогда надо ввести\n" +
                "/setdata 04090805\n\n" +
                "А дата 15 ноября 19:30 будет записана как \n" +
                "/setdata 15111930\n\n" +
                "Между командой и датой надо поставить один пробел");
        sendMessage(chatId, "Для выбора победителя розыгрыша одновременно введите команду /setwinner и номер билета победителя:\n"+
                "К примеру выйгрышный билет номер 5 следует записать как:\n"+
                "/setwinner 5\n\n" +
                "А билет № 342 будет записана как \n" +
                "/setwinner 342\n\n" +
                "Между командой и датой надо поставить один пробел");
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
