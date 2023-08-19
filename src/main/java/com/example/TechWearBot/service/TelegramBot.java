package com.example.TechWearBot.service;


import com.example.TechWearBot.config.BotConfig;
import com.example.TechWearBot.model.ItemTable.Item;
import com.example.TechWearBot.model.ItemTable.ItemRepository;
import com.example.TechWearBot.model.LotteryTableStatus.LotteryStatus;
import com.example.TechWearBot.model.LotteryTableStatus.LotteryStatusRepository;
import com.example.TechWearBot.model.UserLotteryTable.Lottery;
import com.example.TechWearBot.model.UserLotteryTable.LotteryRepository;
import com.example.TechWearBot.model.UserSizeTable.Size;
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
/*
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private LotteryRepository lotteryRepository;
    @Autowired
    private LotteryStatusRepository lotteryStatusRepository;  */
    final BotConfig config;

    static final String adminText = """
            Список администраторких команд:\s

            /setlottery - Создание нового розыгрыша\s
            
            /editlottery - Редактирование настроек лотереи\s
            
            /deletelottery - Удаление существующей лотереи\s
            
            /sendwinner - Отправление сообщения в канал об окончании розыгрыша и победном билете\s
            """;
    static final String helpText = """
            Этот бот поможет Вам подобрать товар или принять участие в розыгрыше от Techwear Lab.

            Вы можете выполнить команды, выбрав их в меню слева, или написав команду боту:\s

            Для получения подборки одежды Вы можете воспользоваться командой /getitems.

            Команда для изменения параметров: /changesize.

            Для регистрации в розыгрыше Вы можите использовать команду /lottery (необходимое условие - быть участником оффициального канала Teckwear Lab: t.me/TechWearLab).

            Команда для показа номера участника: /ticket.\s

            Команда для показа времени до проведения розыгрыша: /showtime\s

            Любые другие вопросы можно задать консультанту: @VladislavTechWear""";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start","Получить преветственное сообщение"));
        listOfCommands.add(new BotCommand("/lottery","Получить номер участника для розыгрыша"));
        listOfCommands.add(new BotCommand("/ticket","Показать Ваш номер участника розыгрыша"));
        listOfCommands.add(new BotCommand("/showtime","Показать оставшееся время до розыгрыша"));
        listOfCommands.add(new BotCommand("/getitems","Получить подборку одежды"));
        listOfCommands.add(new BotCommand("/changesize","Изменить Ваши параметры"));
        listOfCommands.add(new BotCommand("/help","Получить пояснительную информацию"));
        listOfCommands.add(new BotCommand("/adminlist","Получить список команд для администратора"));
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
     //   Long testChannel = -1001953170310L;
     //   Long testGroup = -1001936189450L;
        Long techWearLab = -1001871441739L;


        if (update.hasChannelPost()) {
    /*        if (Objects.equals(update.getChannelPost().getChatId(), techWearLab)) {
                if (update.getChannelPost().getCaption() != null) {
                    saveMessage(update.getChannelPost().getMessageId(),update.getChannelPost().getCaption());
                }
            }
        } else if (update.hasEditedChannelPost()) {
            if (Objects.equals(update.getEditedChannelPost().getChatId(), techWearLab)) {
                if (update.getEditedChannelPost().getCaption() != null) {
                    saveMessage(update.getEditedChannelPost().getMessageId(), update.getEditedChannelPost().getCaption());
                }
            } */
        }
        else if (update.hasMessage()){
                String messageText = update.getMessage().getText();
                User user = update.getMessage().getFrom();
                var userId = user.getId();
                var chatId = update.getMessage().getChatId();
     /*           if (messageText.contains("/setdata") && isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                    var data = messageText.substring(messageText.indexOf(" "));
                    setData(data, chatId, update);
                } else if (messageText.contains("/setwinner") && isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                    var winTicket = messageText.substring(messageText.indexOf(" "));
                    var trueTicket = Integer.parseInt(winTicket.substring(1));
                    selectWinner(trueTicket, chatId, update);
                } else {
                    switch (messageText) {
                        case "/start" -> startCommand(chatId, update.getMessage().getChat().getFirstName());
                        case "/help" -> helpMessage(chatId);
                        case "/lottery" -> callLottery(userId, techWearLab, chatId, update);
                        case "/ticket" -> sendTicket(update.getMessage());
                        case "/setlottery" -> {
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                createLottery(chatId, update);
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                        }
                        case "/showlotterysettings" -> {
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                showLottery(update.getMessage());
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                        }
                        case "/showtime" -> timestamp(chatId, update.getMessage());
                        case "/editlottery" -> {
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                editLottery(chatId);
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                        }
                        case "/deletelottery" -> {
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                deleteLottery(userId, chatId);
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                        }
                        case "/getitems" -> getItemKeyboard(chatId);
                        case "/changesize" -> changeSize(chatId);
                        case "/sendwinner" -> sendWinMessage(techWearLab);
                        case "/adminlist" -> {
                            if (isAdministrator(userId, tulenishka, Vseross11, VladislavTechWear, plugkiiid)) {
                                adminList(chatId);
                            } else {
                                sendMessage(chatId, "Для выполнения этой команды Вы должны обладать правами администратора");
                            }
                        }
                        default -> */sendMessage(chatId, "Неизвестная команда, проверьте правильность написания в /help");
                    }
                }
            
            /*else if (update.hasCallbackQuery()){
            var callBackData = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (callBackData) {
                case "top" -> topKeyboard(chatId);
                case "bot" -> botKeyboard(chatId);
                case "shoes" -> shoesKeyboard(chatId);
                case "changeOutfitSize" -> changeOutfitSize(chatId);
                case "changeBootSize" -> changeBootSize(chatId);
                case "continue" -> sendCompilation(chatId, sizeRepository.getLastCompilation(chatId));
                case "change" -> getItemKeyboard(chatId);
                case "topStock" -> {
                    if (sizeRepository.findById(update.getCallbackQuery().getMessage().getChatId()).isEmpty() || (sizeRepository.getOutfitSize(chatId) == null) || (Objects.equals(sizeRepository.getOutfitSize(chatId), ""))) {
                        setTopSize(chatId);
                    } else {
                        sendCompilation(chatId, callBackData);
                    }
                }
                case "botStock" -> {
                    if (sizeRepository.findById(update.getCallbackQuery().getMessage().getChatId()).isEmpty() || (sizeRepository.getOutfitSize(chatId) == null) || (Objects.equals(sizeRepository.getOutfitSize(chatId), ""))) {
                        setBotSize(chatId);
                    } else {
                        sendCompilation(chatId, callBackData);
                    }
                }
                case "shoesStock" -> {
                    if (sizeRepository.findById(update.getCallbackQuery().getMessage().getChatId()).isEmpty() || (sizeRepository.getBootSize(chatId) == null) || (sizeRepository.getBootSize(chatId) == 0)) {
                        setBootSize(chatId);
                    } else {
                        sendCompilation(chatId, callBackData);
                    }
                }
                case "sTop" -> sendCompilation(chatId, saveOutfitSize(update, "topStock", "s"));
                case "mTop" -> sendCompilation(chatId, saveOutfitSize(update, "topStock", "m"));
                case "lTop" -> sendCompilation(chatId, saveOutfitSize(update, "topStock", "l"));
                case "xlTop" -> sendCompilation(chatId, saveOutfitSize(update, "topStock", "xl"));
                case "xxlTop" -> sendCompilation(chatId, saveOutfitSize(update, "topStock", "xxl"));
                case "sBot" -> sendCompilation(chatId, saveOutfitSize(update, "botStock", "s"));
                case "mBot" -> sendCompilation(chatId, saveOutfitSize(update, "botStock", "m"));
                case "lBot" -> sendCompilation(chatId, saveOutfitSize(update, "botStock", "l"));
                case "xlBot" -> sendCompilation(chatId, saveOutfitSize(update, "botStock", "xl"));
                case "xxlBot" -> sendCompilation(chatId, saveOutfitSize(update, "botStock", "xxl"));
                case "36Boot" -> sendCompilation(chatId, saveBootSize(update, 36));
                case "37Boot" -> sendCompilation(chatId, saveBootSize(update, 37));
                case "38Boot" -> sendCompilation(chatId, saveBootSize(update, 38));
                case "39Boot" -> sendCompilation(chatId, saveBootSize(update, 39));
                case "40Boot" -> sendCompilation(chatId, saveBootSize(update, 40));
                case "41Boot" -> sendCompilation(chatId, saveBootSize(update, 41));
                case "42Boot" -> sendCompilation(chatId, saveBootSize(update, 42));
                case "43Boot" -> sendCompilation(chatId, saveBootSize(update, 43));
                case "44Boot" -> sendCompilation(chatId, saveBootSize(update, 44));
                case "45Boot" -> sendCompilation(chatId, saveBootSize(update, 45));
                case "sChange" -> saveOutfitSize(update, "", "s");
                case "mChange" -> saveOutfitSize(update, "", "m");
                case "lChange" -> saveOutfitSize(update, "", "l");
                case "xlChange" -> saveOutfitSize(update, "", "xl");
                case "xxlChange" -> saveOutfitSize(update, "", "xxl");
                case "36Change" -> saveBootSize(update, 36);
                case "37Change" -> saveBootSize(update, 37);
                case "38Change" -> saveBootSize(update, 38);
                case "39Change" -> saveBootSize(update, 39);
                case "40Change" -> saveBootSize(update, 40);
                case "41Change" -> saveBootSize(update, 41);
                case "42Change" -> saveBootSize(update, 42);
                case "43Change" -> saveBootSize(update, 43);
                case "44Change" -> saveBootSize(update, 44);
                case "45Change" -> saveBootSize(update, 45);
                default -> sendCompilation(chatId, callBackData);
            }
        }
    }  */

    private void adminList(Long chatId){
        sendMessage(chatId, TelegramBot.adminText);
    }
    /*
        private void sendWinMessage(Long techWearLab){
            sendMessage(techWearLab, "Розыгрыш окончен, выйгрышный билет : " + lotteryStatusRepository.getWinner() + " Поздравляем!");
        }

        private void setTopSize(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Выберите свой размер одежды");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var sButton = new InlineKeyboardButton();
            sButton.setText("S");
            sButton.setCallbackData("sTop");
            var mButton = new InlineKeyboardButton();
            mButton.setText("M");
            mButton.setCallbackData("mTop");
            var lButton = new InlineKeyboardButton();
            lButton.setText("L");
            lButton.setCallbackData("lTop");
            FirstRowInLine.add(sButton);
            FirstRowInLine.add(mButton);
            FirstRowInLine.add(lButton);
            rowsInLine.add(FirstRowInLine);
            List<InlineKeyboardButton> SecondRowInLine = new ArrayList<>();
            var xlButton = new InlineKeyboardButton();
            xlButton.setText("XL");
            xlButton.setCallbackData("xlTop");
            var xxlButton = new InlineKeyboardButton();
            xxlButton.setText("XXL");
            xxlButton.setCallbackData("xxlTop");
            SecondRowInLine.add(xlButton);
            SecondRowInLine.add(xxlButton);
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

        private void setBotSize(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Выберите свой размер одежды");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var sButton = new InlineKeyboardButton();
            sButton.setText("S");
            sButton.setCallbackData("sBot");
            var mButton = new InlineKeyboardButton();
            mButton.setText("M");
            mButton.setCallbackData("mBot");
            var lButton = new InlineKeyboardButton();
            lButton.setText("L");
            lButton.setCallbackData("lBot");
            FirstRowInLine.add(sButton);
            FirstRowInLine.add(mButton);
            FirstRowInLine.add(lButton);
            rowsInLine.add(FirstRowInLine);
            List<InlineKeyboardButton> SecondRowInLine = new ArrayList<>();
            var xlButton = new InlineKeyboardButton();
            xlButton.setText("XL");
            xlButton.setCallbackData("xlBot");
            var xxlButton = new InlineKeyboardButton();
            xxlButton.setText("XXL");
            xxlButton.setCallbackData("xxlBot");
            SecondRowInLine.add(xlButton);
            SecondRowInLine.add(xxlButton);
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

        private void setBootSize(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Выберите свой размер обуви");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var Button36 = new InlineKeyboardButton();
            Button36.setText("36");
            Button36.setCallbackData("36Boot");
            var Button37 = new InlineKeyboardButton();
            Button37.setText("37");
            Button37.setCallbackData("37Boot");
            var Button38 = new InlineKeyboardButton();
            Button38.setText("38");
            Button38.setCallbackData("38Boot");
            var Button39 = new InlineKeyboardButton();
            Button39.setText("39");
            Button39.setCallbackData("39Boot");
            FirstRowInLine.add(Button36);
            FirstRowInLine.add(Button37);
            FirstRowInLine.add(Button38);
            FirstRowInLine.add(Button39);
            rowsInLine.add(FirstRowInLine);
            List<InlineKeyboardButton> SecondRowInLine = new ArrayList<>();
            var Button40 = new InlineKeyboardButton();
            Button40.setText("40");
            Button40.setCallbackData("40Boot");
            var Button41 = new InlineKeyboardButton();
            Button41.setText("41");
            Button41.setCallbackData("41Boot");
            var Button42 = new InlineKeyboardButton();
            Button42.setText("42");
            Button42.setCallbackData("42Boot");
            SecondRowInLine.add(Button40);
            SecondRowInLine.add(Button41);
            SecondRowInLine.add(Button42);
            rowsInLine.add(SecondRowInLine);
            List<InlineKeyboardButton> ThirdRowInLine = new ArrayList<>();
            var Button43 = new InlineKeyboardButton();
            Button43.setText("43");
            Button43.setCallbackData("43Boot");
            var Button44 = new InlineKeyboardButton();
            Button44.setText("44");
            Button44.setCallbackData("44Boot");
            var Button45 = new InlineKeyboardButton();
            Button45.setText("45");
            Button45.setCallbackData("45Boot");
            ThirdRowInLine.add(Button43);
            ThirdRowInLine.add(Button44);
            ThirdRowInLine.add(Button45);
            rowsInLine.add(ThirdRowInLine);

            markupInLine.setKeyboard(rowsInLine);
            message.setReplyMarkup(markupInLine);
            try{
                execute(message);
            }
            catch (TelegramApiException e){
                log.error("Ошибка:" + e.getMessage());
            }
        }

        private void changeOutfitSize(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Выберите новый размер одежды");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var sButton = new InlineKeyboardButton();
            sButton.setText("S");
            sButton.setCallbackData("sChange");
            var mButton = new InlineKeyboardButton();
            mButton.setText("M");
            mButton.setCallbackData("mChange");
            var lButton = new InlineKeyboardButton();
            lButton.setText("L");
            lButton.setCallbackData("lChange");
            FirstRowInLine.add(sButton);
            FirstRowInLine.add(mButton);
            FirstRowInLine.add(lButton);
            rowsInLine.add(FirstRowInLine);
            List<InlineKeyboardButton> SecondRowInLine = new ArrayList<>();
            var xlButton = new InlineKeyboardButton();
            xlButton.setText("XL");
            xlButton.setCallbackData("xlChange");
            var xxlButton = new InlineKeyboardButton();
            xxlButton.setText("XXL");
            xxlButton.setCallbackData("xxlChange");
            SecondRowInLine.add(xlButton);
            SecondRowInLine.add(xxlButton);
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

        private void changeBootSize(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Выберите новый размер обуви");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var Button36 = new InlineKeyboardButton();
            Button36.setText("36");
            Button36.setCallbackData("36Change");
            var Button37 = new InlineKeyboardButton();
            Button37.setText("37");
            Button37.setCallbackData("37Change");
            var Button38 = new InlineKeyboardButton();
            Button38.setText("38");
            Button38.setCallbackData("38Change");
            var Button39 = new InlineKeyboardButton();
            Button39.setText("39");
            Button39.setCallbackData("39Change");
            FirstRowInLine.add(Button36);
            FirstRowInLine.add(Button37);
            FirstRowInLine.add(Button38);
            FirstRowInLine.add(Button39);
            rowsInLine.add(FirstRowInLine);
            List<InlineKeyboardButton> SecondRowInLine = new ArrayList<>();
            var Button40 = new InlineKeyboardButton();
            Button40.setText("40");
            Button40.setCallbackData("40Change");
            var Button41 = new InlineKeyboardButton();
            Button41.setText("41");
            Button41.setCallbackData("41Change");
            var Button42 = new InlineKeyboardButton();
            Button42.setText("42");
            Button42.setCallbackData("42Change");
            SecondRowInLine.add(Button40);
            SecondRowInLine.add(Button41);
            SecondRowInLine.add(Button42);
            rowsInLine.add(SecondRowInLine);
            List<InlineKeyboardButton> ThirdRowInLine = new ArrayList<>();
            var Button43 = new InlineKeyboardButton();
            Button43.setText("43");
            Button43.setCallbackData("43Change");
            var Button44 = new InlineKeyboardButton();
            Button44.setText("44");
            Button44.setCallbackData("44Change");
            var Button45 = new InlineKeyboardButton();
            Button45.setText("45");
            Button45.setCallbackData("45Change");
            ThirdRowInLine.add(Button43);
            ThirdRowInLine.add(Button44);
            ThirdRowInLine.add(Button45);
            rowsInLine.add(ThirdRowInLine);

            markupInLine.setKeyboard(rowsInLine);
            message.setReplyMarkup(markupInLine);
            try{
                execute(message);
            }
            catch (TelegramApiException e){
                log.error("Ошибка:" + e.getMessage());
            }
        }

        String saveOutfitSize(Update update,String data,String itemSize){
            Size size = new Size();
            int bootSize;
            if (sizeRepository.findById(update.getCallbackQuery().getMessage().getChatId()).isEmpty()){
                bootSize = 0;
            } else {
                bootSize = sizeRepository.getBootSize(update.getCallbackQuery().getMessage().getChatId());
            }
            size.setBootSize(bootSize);
            size.setChatId(update.getCallbackQuery().getMessage().getChatId());
            size.setOutfitSize(itemSize);
            sizeRepository.save(size);
            sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Вы ввели размер: " + itemSize);
            return(data);
        }

        String saveBootSize(Update update, Integer bootSize){
            Size size = new Size();
            String outfitSize;
            if (sizeRepository.findById(update.getCallbackQuery().getMessage().getChatId()).isEmpty()){
                outfitSize = "";
            } else {
                outfitSize = sizeRepository.getOutfitSize(update.getCallbackQuery().getMessage().getChatId());
            }
            size.setBootSize(bootSize);
            size.setChatId(update.getCallbackQuery().getMessage().getChatId());
            size.setOutfitSize(outfitSize);
            sizeRepository.save(size);
            sendMessage(update.getCallbackQuery().getMessage().getChatId(), "Вы ввели размер: " + bootSize);
            return("shoesStock");
        }

        private void sendCompilation(Long chatId,String itemType){
            var techWearLab = -1001871441739L;
            long[] itemPool;
            if (Objects.equals(itemType, "topOrder")){
                itemPool = itemRepository.topOrderPool();
            } else if (Objects.equals(itemType, "botOrder")){
                itemPool = itemRepository.botOrderPool();
            } else if (Objects.equals(itemType, "shoesOrder")){
                itemPool = itemRepository.shoesOrderPool();
            }  else if (Objects.equals(itemType, "topStock")){
                if (Objects.equals(sizeRepository.getOutfitSize(chatId), "s")) {
                    itemPool = itemRepository.topSPool();
                } else if (Objects.equals(sizeRepository.getOutfitSize(chatId), "m")){
                        itemPool = itemRepository.topMPool();
                } else if (Objects.equals(sizeRepository.getOutfitSize(chatId), "l")){
                    itemPool = itemRepository.topLPool();
                } else if (Objects.equals(sizeRepository.getOutfitSize(chatId), "xl")){
                    itemPool = itemRepository.topXLPool();
                } else {
                    itemPool = itemRepository.topXXLPool();
                }
            } else if (Objects.equals(itemType, "botStock")){
                if (Objects.equals(sizeRepository.getOutfitSize(chatId), "s")) {
                    itemPool = itemRepository.botSPool();
                } else if (Objects.equals(sizeRepository.getOutfitSize(chatId), "m")){
                    itemPool = itemRepository.botMPool();
                } else if (Objects.equals(sizeRepository.getOutfitSize(chatId), "l")){
                    itemPool = itemRepository.botLPool();
                } else if (Objects.equals(sizeRepository.getOutfitSize(chatId), "xl")){
                    itemPool = itemRepository.botXLPool();
                } else {
                    itemPool = itemRepository.botXXLPool();
                }
            } else if (Objects.equals(itemType, "shoesStock")){
                if (sizeRepository.getBootSize(chatId) == 36) {
                    itemPool = itemRepository.shoes36Pool();
                } else if(sizeRepository.getBootSize(chatId) == 37){
                    itemPool = itemRepository.shoes37Pool();
                } else if(sizeRepository.getBootSize(chatId) == 38){
                    itemPool = itemRepository.shoes38Pool();
                } else if(sizeRepository.getBootSize(chatId) == 39){
                    itemPool = itemRepository.shoes39Pool();
                } else if(sizeRepository.getBootSize(chatId) == 40){
                    itemPool = itemRepository.shoes40Pool();
                } else if(sizeRepository.getBootSize(chatId) == 41){
                    itemPool = itemRepository.shoes41Pool();
                } else if(sizeRepository.getBootSize(chatId) == 42){
                    itemPool = itemRepository.shoes42Pool();
                } else if(sizeRepository.getBootSize(chatId) == 43){
                    itemPool = itemRepository.shoes43Pool();
                } else if(sizeRepository.getBootSize(chatId) == 44){
                    itemPool = itemRepository.shoes44Pool();
                } else {
                    itemPool = itemRepository.shoes45Pool();
                }
            } else {
                itemPool = itemRepository.specificPool();
            }
            for (long l : itemPool) {
                var itemId = (int) l;
                forwardMessage(techWearLab, chatId, itemId);
            }
            sizeRepository.saveLastCompilation(chatId,itemType);
            sendReply(chatId);
        }

        private void changeSize(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Какой размер Вы хотите изменить?");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var OutfitButton = new InlineKeyboardButton();
            OutfitButton.setText("Размер одежды");
            OutfitButton.setCallbackData("changeOutfitSize");
            var bootButton = new InlineKeyboardButton();
            bootButton.setText("Размер обуви");
            bootButton.setCallbackData("changeBootSize");
            FirstRowInLine.add(OutfitButton);
            FirstRowInLine.add(bootButton);
            rowsInLine.add(FirstRowInLine);
            markupInLine.setKeyboard(rowsInLine);
            message.setReplyMarkup(markupInLine);
            try{
                execute(message);
            }
            catch (TelegramApiException e){
                log.error("Ошибка:" + e.getMessage());
            }
        }

        private void sendReply(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Продолжить просмотр подборки?");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var continueButton = new InlineKeyboardButton();
            continueButton.setText("Продолжить просмотр");
            continueButton.setCallbackData("continue");
            var changeButton = new InlineKeyboardButton();
            changeButton.setText("Изменить параметры подборки");
            changeButton.setCallbackData("change");
            FirstRowInLine.add(continueButton);
            FirstRowInLine.add(changeButton);
            rowsInLine.add(FirstRowInLine);
            markupInLine.setKeyboard(rowsInLine);
            message.setReplyMarkup(markupInLine);
            try{
                execute(message);
            }
            catch (TelegramApiException e){
                log.error("Ошибка:" + e.getMessage());
            }
        }

        private void topKeyboard(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Подборку верхней одежды составить из вещей:");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var topStockButton = new InlineKeyboardButton();
            topStockButton.setText("В наличии");
            topStockButton.setCallbackData("topStock");
            var topOrderButton = new InlineKeyboardButton();
            topOrderButton.setText("Под заказ");
            topOrderButton.setCallbackData("topOrder");
            FirstRowInLine.add(topStockButton);
            FirstRowInLine.add(topOrderButton);
            rowsInLine.add(FirstRowInLine);
            markupInLine.setKeyboard(rowsInLine);
            message.setReplyMarkup(markupInLine);
            try{
                execute(message);
            }
            catch (TelegramApiException e){
                log.error("Ошибка:" + e.getMessage());
            }
        }

        private void botKeyboard(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Подборку нижней одежды составить из вещей:");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var botStockButton = new InlineKeyboardButton();
            botStockButton.setText("В наличии");
            botStockButton.setCallbackData("botStock");
            var botOrderButton = new InlineKeyboardButton();
            botOrderButton.setText("Под заказ");
            botOrderButton.setCallbackData("botOrder");
            FirstRowInLine.add(botStockButton);
            FirstRowInLine.add(botOrderButton);
            rowsInLine.add(FirstRowInLine);
            markupInLine.setKeyboard(rowsInLine);
            message.setReplyMarkup(markupInLine);
            try{
                execute(message);
            }
            catch (TelegramApiException e){
                log.error("Ошибка:" + e.getMessage());
            }
        }

        private void shoesKeyboard(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Подборку обуви составить из вещей:");
            InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
            List<InlineKeyboardButton> FirstRowInLine = new ArrayList<>();
            var shoesStockButton = new InlineKeyboardButton();
            shoesStockButton.setText("В наличии");
            shoesStockButton.setCallbackData("shoesStock");
            var shoesOrderButton = new InlineKeyboardButton();
            shoesOrderButton.setText("Под заказ");
            shoesOrderButton.setCallbackData("shoesOrder");
            FirstRowInLine.add(shoesStockButton);
            FirstRowInLine.add(shoesOrderButton);
            rowsInLine.add(FirstRowInLine);
            markupInLine.setKeyboard(rowsInLine);
            message.setReplyMarkup(markupInLine);
            try{
                execute(message);
            }
            catch (TelegramApiException e){
                log.error("Ошибка:" + e.getMessage());
            }
        }

        private void getItemKeyboard(Long chatId){
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Подборку каких вещей Вы хотите получить?");
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

        private void forwardMessage(Long techWearLab, Long chatId, Integer itemId) {
            ForwardMessage forwardMessage = new ForwardMessage();
            forwardMessage.setFromChatId(techWearLab);
            forwardMessage.setChatId(chatId);
            forwardMessage.setMessageId(itemId);
            try {
                execute(forwardMessage);
            } catch (TelegramApiException e) {
                log.error("Ошибка:" + e.getMessage());
            }
        }

        private void saveMessage(Integer messageId, String postText){
            Item item = new Item();
            item.setMessageId(messageId);
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
            log.info("Сохранена новая вещь: " + item);
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
            return userId.equals(tulenishka) || userId.equals(Vseross11) || userId.equals(VladislavTechWear) || userId.equals(plugkiiid);
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
                sendMessage(chatId, """
                        Для записи даты проведения розыгрыша одновременно введите команду /setdata , дату: (день, месяц, год) и  время: (час, минута) проведения розыгрыша:
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
                        Для выбора победителя розыгрыша одновременно введите команду /setwinner и номер билета победителя:
                        К примеру выйгрышный билет номер 5 следует записать как:
                        /setwinner 5

                        А билет № 342 будет записана как\s
                        /setwinner 342

                        Между командой и датой надо поставить один пробел""");
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
            String answer = "Здравствуйте, " + name + ", этот бот поможет Вам получить подборку одежды или зарегистрироваться в розыгрыше. \n" +
                    "Что бы получить список команд воспользуйтесь меню слева или напишите /help";
            sendMessage(chatId, answer);
            log.info("Приветственное сообщение доставлено:" + name);
        }

        private void helpMessage(long chatId){
            sendMessage(chatId,helpText);
            log.info("Пояснительная информация доставлена.");
        }
    */
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