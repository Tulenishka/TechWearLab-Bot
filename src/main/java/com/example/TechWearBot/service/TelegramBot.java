package com.example.TechWearBot.service;


import com.example.TechWearBot.config.BotConfig;
import com.example.TechWearBot.model.ItemTable.Item;
import com.example.TechWearBot.model.ItemTable.ItemRepository;
import com.example.TechWearBot.model.LotteryTableStatus.LotteryStatus;
import com.example.TechWearBot.model.LotteryTableStatus.LotteryStatusRepository;
import com.example.TechWearBot.model.UserLotteryTable.Lottery;
import com.example.TechWearBot.model.UserLotteryTable.LotteryRepository;
import com.example.TechWearBot.model.UserSizeTable.SizeRepository;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
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



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ItemRepository itemRepository;
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
        listOfCommands.add(new BotCommand("/showtime","Показать оставшееся время до розыгрыша"));
        listOfCommands.add(new BotCommand("/help","Получить пояснительную информацию"));
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
        Long tulenishka = 1010657995L;
        Long Vseross11 = 438558550L;
        Long plugkiiid = 11L;
        Long VladislavTechWear = 5398823847L;
        Long testChannel = -1001953170310L;
        Long testGroup = -1001936189450L;
        Long techWearLab = -1001871441739L;


        if (update.hasChannelPost()) {
            if (Objects.equals(update.getChannelPost().getChatId(), techWearLab)) {
                if (update.getChannelPost().getCaption() != null) {
                    saveMessage(update);
                }
            }
        } else if (update.hasMessage()){
                String messageText = update.getMessage().getText();
                User user = update.getMessage().getFrom();
                var userId = user.getId();
                var chatId = update.getMessage().getChatId();
                if (messageText.contains("/setdata") && isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                    var data = messageText.substring(messageText.indexOf(" "));
                    setData(data, chatId, update);
                } else if (messageText.contains("/setwinner") && isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                    var winTicket = messageText.substring(messageText.indexOf(" "));
                    var trueTicket = Integer.valueOf(winTicket.substring(1));
                    selectWinner(trueTicket, chatId, update);
                } else {
                    switch (messageText) {
                        case "/start":
                            startCommand(chatId, update.getMessage().getChat().getFirstName());
                            break;
                        case "/help":
                            helpMessage(chatId);
                            break;
                        case "/lottery":
                            callLottery(userId,techWearLab,chatId,update);
                            break;
                        case "/ticket":
                            sendTicket(update.getMessage());
                            break;
                        case "/setlottery":
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                createLottery(chatId, update);
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                            break;
                        case "/showlotterysettings":
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                showLottery(update.getMessage());
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                            break;
                        case "/showtime":
                            timestamp(chatId, update.getMessage());
                            break;
                        case "/editlottery":
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                editLottery(chatId);
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                            break;
                        case "/deletelottery":
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                deleteLottery(userId, chatId);
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                            break;
                        case "/sendforward":
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                forwardMessage(tulenishka, update);
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                            break;
                        case "/getitems":
                            getItemKeyboard(chatId);
                            break;
                        default:
                            sendMessage(chatId, "Неизвестная команда, проверьте правильность написания в /help");
                    }
                }
            } else if (update.hasCallbackQuery()){
            
        }
    }



    private void getItemKeyboard(Long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Подборку каких вещей вы хотели бы получить?");
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
        var topButton = new InlineKeyboardButton();
        topButton.setText("Одежда верх");
        topButton.setCallbackData("top");
        var shoesButton = new InlineKeyboardButton();
        shoesButton.setText("Обувь");
        shoesButton.setCallbackData("shoes");
        FirstRowInLine.add(topButton);
        FirstRowInLine.add(shoesButton);
        rowsInLine.add(FirstRowInLine);

        List<InlineKeyboardButton> SecondRowInLine = new ArrayList<>();
        var botButton = new InlineKeyboardButton();
        botButton.setText("Одежда низ");
        botButton.setCallbackData("bot");
        var specificButton = new InlineKeyboardButton();
        specificButton.setText("Аксессуары");
        specificButton.setCallbackData("specific");
        SecondRowInLine.add(botButton);
        SecondRowInLine.add(specificButton);
        rowsInLine.add(SecondRowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        try{
            execute(message);
        }
        catch (TelegramApiException e){
            log.error("Ошибка:" + e.getMessage());
        }
    }

    private void callLottery(Long userId, Long techWearLab, Long chatId, Update update){
        GetChatMember getMember = new GetChatMember();
        getMember.setUserId(userId);
        getMember.setChatId(techWearLab);
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
    }

    private void forwardMessage(Long techWearLab, Update update) {
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setFromChatId(techWearLab);
        forwardMessage.setChatId(update.getMessage().getChatId());
        forwardMessage.setMessageId(1074);
        try {
            execute(forwardMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка:" + e.getMessage());
        }
    }

    private void saveMessage(Update update){
        var messageId = update.getChannelPost().getMessageId();
        Item item = new Item();
        item.setMessageId(messageId);
        String postText = update.getChannelPost().getCaption();
        if (postText.contains("#S")){
            item.setSizeS(true);}
        if (postText.contains("#M")){
            item.setSizeM(true);
        }
        if (postText.contains("#L")){
            item.setSizeL(true);
        }
        if (postText.contains("#XL")){
            item.setSizeXL(true);
        }
        if (postText.contains("#XXl")){
            item.setSizeXXL(true);
        }
        if (postText.contains("#36")){
            item.setSize36(true);
        }
        if (postText.contains("#36")){
            item.setSize36(true);
        }
        if (postText.contains("#37")){
            item.setSize37(true);
        }
        if (postText.contains("#38")){
            item.setSize38(true);
        }
        if (postText.contains("#39")){
            item.setSize39(true);
        }
        if (postText.contains("#40")){
            item.setSize40(true);
        }
        if (postText.contains("#41")){
            item.setSize41(true);
        }
        if (postText.contains("#42")){
            item.setSize42(true);
        }
        if (postText.contains("#43")){
            item.setSize43(true);
        }
        if (postText.contains("#44")){
            item.setSize44(true);
        }
        if (postText.contains("#45")){
            item.setSize45(true);
        }
        if (postText.contains("#кроссовки") || postText.contains("#тапочки") || postText.contains("#сандали") || postText.contains("#сланцы")
                || postText.contains("#ботинки") || postText.contains("#лоферы") || postText.contains("#шлепки") || postText.contains("#тапки")
                || postText.contains("#шлёпки")){
            item.setItemType("shoes");
        } else if (postText.contains("#худи") || postText.contains("#свитшот") || postText.contains("#ветровка") || postText.contains("#куртка")
                || postText.contains("#лонгслив") || postText.contains("#пуховик") || postText.contains("#парка") || postText.contains("#кофта")
                || postText.contains("#кардиган") || postText.contains("#майка") || postText.contains("#термо") || postText.contains("#футболка")
                || postText.contains("#жилетка") || postText.contains("#анорак") || postText.contains("#зипка") || postText.contains("#халфзип")
                || postText.contains("#дождевик") || postText.contains("#плащ") || postText.contains("#водолазка") || postText.contains("#бомбер")
                || postText.contains("#шерпа") || postText.contains("#рубашка") || postText.contains("#пуловер") || postText.contains("#толстовка")
                || postText.contains("#поло")){
            item.setItemType("top");
        } else if (postText.contains("#штаны") || postText.contains("#шорты") || postText.contains("#джоггеры") || postText.contains("#джинсы")
                || postText.contains("#подтрусники") || postText.contains("#боксеры")){
            item.setItemType("bot");
        } else if (postText.contains("сумка") || postText.contains("#клатч") || postText.contains("#перчатки") || postText.contains("#духи")
                || postText.contains("#часы") || postText.contains("#рюкзак") || postText.contains("#сумка") || postText.contains("#панама")
                || postText.contains("#кошелек") || postText.contains("#кошелёк") || postText.contains("#кепка") || postText.contains("#бандана")
                || postText.contains("#мяч") || postText.contains("#лего") || postText.contains("#чемодан") || postText.contains("#шапка")
                || postText.contains("#снуд") || postText.contains("#балаклава") || postText.contains("#ремень") || postText.contains("#очки")
                || postText.contains("#зонт") || postText.contains("#маска")){
            item.setItemType("specific");
        }
        itemRepository.save(item);
        log.info("Сохранена новая вещь: " + item.toString());
    }

    private void deleteLottery(Long userId,Long chatId) {
        if (lotteryStatusRepository.getActive() != null && lotteryStatusRepository.getActive()) {
            LotteryStatus lotteryStatus = new LotteryStatus();
            lotteryStatus.setLotteryId(1);
            lotteryStatus.setLotteryActive(false);
            lotteryStatus.setLotteryCreatorId(userId);
            lotteryStatus.setLotteryDateDay(0);
            lotteryStatus.setLotteryDateMonth(0);
            lotteryStatus.setLotteryDateYear(0);
            lotteryStatus.setLotteryDateHour(0);
            lotteryStatus.setLotteryDateMinute(0);
            lotteryStatus.setLotteryWinnerTicket(0);
            lotteryStatusRepository.save(lotteryStatus);
            lotteryRepository.deleteLottery();
            sendMessage(chatId, "Розыгрыш удален");
        } else {
            sendMessage(chatId, "Розыгрыш не создан");
        }
    }

    private void editLottery(Long chatId){
        sendMessage(chatId, """
                Для изменения даты проведения розыгрыша одновременно введите команду /setdata , дату: (день, месяц, год) и  время: (час, минута) проведения розыгрыша:
                К примеру дата 4 сентября 2023 08:05 следует записать как:
                День - 04\s
                Месяц - 09\s
                Год - 2023\s
                Час - 08\s
                Минута - 05\s

                Тогда надо ввести
                /setdata 040920230805

                А дата 15 ноября 2036 19:30 будет записана как\s
                /setdata 151120361930

                Между командой и датой надо поставить один пробел""");
        sendMessage(chatId, """
                Для изменения номера победителя розыгрыша одновременно введите команду /setwinner и номер билета победителя:
                К примеру выйгрышный билет номер 5 следует записать как:
                /setwinner 5

                А билет № 342 будет записана как\s
                /setwinner 342

                Между командой и номером билета надо поставить один пробел""");
    }

    public Boolean isAdministrator(Long userId,Long tulenishka,Long Vseross11,Long VladislavTechWear,Long plugkiiid) {
        if (userId.equals(tulenishka) || userId.equals(Vseross11) || userId.equals(VladislavTechWear) || userId.equals(plugkiiid)) {
        return true;
        } else {
            return false;
        }
    }

    public void timestamp(Long chatId, Message message) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");    //HHmm
            Date parsedDate = formatter.parse(dateToString());
            var unixDate = parsedDate.getTime()/1000;
            var timeLeft = ((unixDate - message.getDate())/60);
            var daysLeft = timeLeft/1440;
            var hoursLeft = (timeLeft-(daysLeft*1440))/60;
            var minutesLeft = timeLeft - (daysLeft*1440) - (hoursLeft*60);
            sendMessage(chatId, "До лотереи осталось  \n" +
                    "Дней: " + daysLeft + "  Часов: " + hoursLeft + "  Минут: " + minutesLeft);
        } catch(Exception e) {
          log.error("Ошибка" + e.getMessage());
        }
    }

    public String dateToString(){
        String time = "";
       if (lotteryStatusRepository.getDay()<10){
           time = time + "0";
       }
       time = time + String.valueOf(lotteryStatusRepository.getDay());
       if (lotteryStatusRepository.getMonth()<10){
           time = time + "0";
       }
       time = time + String.valueOf(lotteryStatusRepository.getMonth());
       time = time + String.valueOf(lotteryStatusRepository.getYear());
       if (lotteryStatusRepository.getHour()<10){
           time = time + "0";
       }
       time = time + String.valueOf(lotteryStatusRepository.getHour());
       if (lotteryStatusRepository.getMinute()<10){
           time = time + "0";
       }
       time = time + String.valueOf(lotteryStatusRepository.getMinute());
        return time;
   }

    private void showLottery(Message message) {
        try {
            var chatId = message.getChatId();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
            Date parsedDate = formatter.parse(dateToString());
            sendMessage(chatId, "Id последнего редактора лотереи: " + lotteryStatusRepository.getCreatorId() + "\n" +
                    "Дата проведения лотереи: " + parsedDate + "\n" +
                    "Номер победителя лотереи: " + lotteryStatusRepository.getWinner());
        } catch(Exception e) {
            log.error("Ошибка" + e.getMessage());
        }
    }

    private void selectWinner(int trueTicket,Long chatId,Update update) {
        if (lotteryStatusRepository.getActive() != null && lotteryStatusRepository.getActive()) {
            User user = update.getMessage().getFrom();
            var userId = user.getId();
            LotteryStatus lotteryStatus = new LotteryStatus();
            var day = lotteryStatusRepository.getDay();
            var month = lotteryStatusRepository.getMonth();
            var year = lotteryStatusRepository.getYear();
            var hour = lotteryStatusRepository.getHour();
            var minute = lotteryStatusRepository.getMinute();
            lotteryStatus.setLotteryId(1);
            lotteryStatus.setLotteryActive(true);
            lotteryStatus.setLotteryCreatorId(userId);
            lotteryStatus.setLotteryDateDay(day);
            lotteryStatus.setLotteryDateMonth(month);
            lotteryStatus.setLotteryDateYear(year);
            lotteryStatus.setLotteryDateHour(hour);
            lotteryStatus.setLotteryDateMinute(minute);
            lotteryStatus.setLotteryWinnerTicket(trueTicket);
            lotteryStatusRepository.save(lotteryStatus);
            showLottery(update.getMessage());
        } else {
            sendMessage(chatId, "Розыгрыш не создан");
        }
    }

    private void setData(String data,Long chatId,Update update) {
        if (lotteryStatusRepository.getActive() != null && lotteryStatusRepository.getActive()) {
            User user = update.getMessage().getFrom();
            var userId = user.getId();
            LotteryStatus lotteryStatus = new LotteryStatus();
            var day = data.substring(1, 3);
            var month = data.substring(3, 5);
            var year = data.substring(5,9);
            var hour = data.substring(9, 11);
            var minute = data.substring(11);
            var winner = (int)lotteryStatusRepository.getWinner();
            lotteryStatus.setLotteryId(1);
            lotteryStatus.setLotteryActive(true);
            lotteryStatus.setLotteryCreatorId(userId);
            lotteryStatus.setLotteryDateDay(Integer.valueOf(day));
            lotteryStatus.setLotteryDateMonth(Integer.valueOf(month));
            lotteryStatus.setLotteryDateHour(Integer.valueOf(hour));
            lotteryStatus.setLotteryDateMinute(Integer.valueOf(minute));
            lotteryStatus.setLotteryDateYear(Integer.valueOf(year));
            lotteryStatus.setLotteryWinnerTicket(winner);
            lotteryStatusRepository.save(lotteryStatus);
            showLottery(update.getMessage());
        } else {
            sendMessage(chatId, "Розыгрыш не создан");
        }
    }

    public void createLottery(long chatId,Update update) {
        if (lotteryStatusRepository.getActive() != null && lotteryStatusRepository.getActive()) {
            sendMessage(chatId, "Розыгрыш уже создан");
        } else {
            User user = update.getMessage().getFrom();
            LotteryStatus lotteryStatus = new LotteryStatus();
            Lottery lottery = new Lottery();
            lotteryStatus.setLotteryId(1);
            lotteryStatus.setLotteryActive(true);
            lotteryStatus.setLotteryCreatorId(user.getId());
            lotteryStatus.setLotteryDateDay(0);
            lotteryStatus.setLotteryDateMonth(0);
            lotteryStatus.setLotteryDateYear(0);
            lotteryStatus.setLotteryDateHour(0);
            lotteryStatus.setLotteryDateMinute(0);
            lotteryStatus.setLotteryWinnerTicket(0);
            lotteryStatusRepository.save(lotteryStatus);
            if (lotteryRepository.findById(chatId).isEmpty()) {
                lottery.setChatId(chatId);
                lottery.setUserName(update.getMessage().getChat().getUserName());
                lottery.setTicket(1);
                lotteryRepository.save(lottery);
            }
            sendMessage(chatId, "Для записи даты проведения розыгрыша одновременно введите команду /setdata , дату: (день, месяц, год) и  время: (час, минута) проведения розыгрыша:\n" +
                    "К примеру дата 4 сентября 2023 08:05 следует записать как:\n" +
                    "День - 04 \n" +
                    "Месяц - 09 \n" +
                    "Год - 2023 \n" +
                    "Час - 08 \n" +
                    "Минута - 05 \n\n" +
                    "Тогда надо ввести\n" +
                    "/setdata 040920230805\n\n" +
                    "А дата 15 ноября 2036 19:30 будет записана как \n" +
                    "/setdata 151120361930\n\n" +
                    "Между командой и датой надо поставить один пробел");
            sendMessage(chatId, "Для выбора победителя розыгрыша одновременно введите команду /setwinner и номер билета победителя:\n" +
                    "К примеру выйгрышный билет номер 5 следует записать как:\n" +
                    "/setwinner 5\n\n" +
                    "А билет № 342 будет записана как \n" +
                    "/setwinner 342\n\n" +
                    "Между командой и датой надо поставить один пробел");
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
                    log.info("Пользователь уже был");
                }
            } else {
                var chatId = message.getChatId();
                sendMessage(chatId, "В данный момент нет активного розыгрыша");
            }
        }

    private void startCommand(long chatId, String name) {
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