package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.CardReplenishmentPage;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegativeTransferTest {
    @Test
    void shoulTransferToFirstCardTestOverInitialBalance() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();//получаем данные пользователя
        var verificationPage = loginPage.validLogin(authInfo);//заполняем поля и жмякаем продолжить
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);//получаем код
        verificationPage.validVerify(verificationCode);//вводим код и жмякаем продолжить


        TransferPage page = new TransferPage();
        var cardTo = DataHelper.getFirstCardInfo();//получаем данные  карты НА КОТОРУЮ ПЕРЕВОДИМ
        DashBoardPage dashBoardPage = new DashBoardPage();
        var initialBalanceCardTo = dashBoardPage.getCardBalance(cardTo);//начальный баланс карты To
        var initialBalanceCardFrom = dashBoardPage.getCardBalance(DataHelper.getOtherCard(cardTo)); //начальный баланс карты From

        TransferPage.replenishCard(cardTo);//нажимаеи пополнить карту
        DataHelper.CardInfo cardFrom = DataHelper.getOtherCard(cardTo);// Находим карту "откуда" — другую


        String amount = String.valueOf(initialBalanceCardFrom + initialBalanceCardFrom / 10);// Создаем страницу, указываем сумму и карты
        CardReplenishmentPage pay = new CardReplenishmentPage(cardFrom, amount);// Передаем карту "откуда"
        pay.fillFromCard(cardFrom); // Заполняем поле "откуда" нужной картой

        pay.transferFunds();// Выполняем перевод
        page.clickReload();//клик обновить

        assertEquals(10000, dashBoardPage.getCardBalance(cardTo));//проверка бананса карты пополнения
        assertEquals(10000, dashBoardPage.getCardBalance(cardFrom));//проверка бананса карты извлечения

        //обратный перевод
        TransferPage.replenishCard(cardFrom);//нажимаем кнопку пополнить From
        DataHelper.CardInfo reverseCardFrom = DataHelper.getOtherCard(cardFrom);// Находим карту "откуда" — другую
        CardReplenishmentPage reversePay = new CardReplenishmentPage(cardFrom, amount); // Передаем карту "To"
        reversePay.clearAmountField(); // очищаем поле суммы
        reversePay.clearFromCardField(); // очищаем поле номера карты
        reversePay.fillFromCard(reverseCardFrom); // Заполняем поле "откуда" нужной картой

        reversePay.transferFunds(); // Выполняем обратный перевод

        assertEquals(initialBalanceCardTo, dashBoardPage.getCardBalance(cardTo));//проверка бананса карты пополнения
        assertEquals(initialBalanceCardFrom, dashBoardPage.getCardBalance(cardFrom));//проверка бананса карты извлечения
    }
}
