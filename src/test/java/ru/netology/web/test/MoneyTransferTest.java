package ru.netology.web.test;


import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.CardReplenishmentPage;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoneyTransferTest {
    @Test
    void shoulTransferToFirstCardTest1_10FromInitialBalance() {
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


        String amount = String.valueOf(initialBalanceCardFrom / 10);// Создаем страницу, указываем сумму и карты
        CardReplenishmentPage pay = new CardReplenishmentPage(cardFrom, amount);// Передаем карту "откуда"
        pay.fillFromCard(cardFrom); // Заполняем поле "откуда" нужной картой

        pay.transferFunds();// Выполняем перевод
        page.clickReload();//клик обновить

        assertEquals(11000, dashBoardPage.getCardBalance(cardTo));//проверка бананса карты пополнения
        assertEquals(9000, dashBoardPage.getCardBalance(cardFrom));//проверка бананса карты извлечения

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


    @Test
    void shouldCancelTransferAndNotChangeBalances() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo(); // Получаем данные пользователя
        var verificationPage = loginPage.validLogin(authInfo); // Заполняем поля и жмем продолжить
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo); // Получаем код
        verificationPage.validVerify(verificationCode); // Вводим код и жмем продолжить

        DashBoardPage dashBoardPage = new DashBoardPage();
        var cardTo = DataHelper.getFirstCardInfo(); // Получаем данные карты, на которую переводим
        var initialBalanceCardTo = dashBoardPage.getCardBalance(cardTo); // Начальный баланс карты To
        var initialBalanceCardFrom = dashBoardPage.getCardBalance(DataHelper.getOtherCard(cardTo)); // Начальный баланс карты From

        TransferPage.replenishCard(cardTo); // Нажимаем пополнить карту
        DataHelper.CardInfo cardFrom = DataHelper.getOtherCard(cardTo); // Находим карту "откуда" — другую

        String amount = String.valueOf(initialBalanceCardFrom / 10); // Создаем сумму для перевода
        CardReplenishmentPage pay = new CardReplenishmentPage(cardFrom, amount); // Передаем карту "откуда"
        pay.fillFromCard(cardFrom); // Заполняем поле "откуда" нужной картой

        // Заполняем поле суммы
        pay.fillAmount(amount); // Вызываем метод fillAmount для заполнения суммы

        pay.clickCancelButton(); // Нажимаем на кнопку отмены

        // Проверяем, что балансы не изменились
        assertEquals(initialBalanceCardTo, dashBoardPage.getCardBalance(cardTo));
        assertEquals(initialBalanceCardFrom, dashBoardPage.getCardBalance(cardFrom));
    }

    @Test
    void shouldLoginUnregisteredUser() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var otherAuthInfo = DataHelper.getOtherAuthInfo(authInfo);
        var verificationPage = loginPage.validLogin(otherAuthInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(otherAuthInfo);
        var dashBoardPage = verificationPage.validVerify(verificationCode);

        var transferPage = new TransferPage();

        transferPage.getTransferHeader().shouldBe(Condition.visible);

        transferPage.getReloadButton().shouldBe(Condition.visible, Condition.enabled);
    }

}
