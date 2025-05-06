package ru.netology.web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    // Объявление переменных уровня класса
    private int initialBalanceCardFirst;
    private int initialBalanceCardSecond;

    private DashBoardPage dashBoardPage; // добавляем сюда

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);

        dashBoardPage = new DashBoardPage(); // инициализация
        //получаем начальный баланс  карт
        initialBalanceCardFirst = dashBoardPage.getCardBalance(DataHelper.getFirstCardInfo());
        initialBalanceCardSecond = dashBoardPage.getCardBalance(DataHelper.getSecondCardInfo());
    }

    @AfterEach
    void tearDown() {
        //получаем актуальные балансы
        int balanceFirst = dashBoardPage.getCardBalance(DataHelper.getFirstCardInfo());
        int balanceSecond = dashBoardPage.getCardBalance(DataHelper.getSecondCardInfo());
        int totalCurrent = balanceFirst + balanceSecond;
        int totalInitial = initialBalanceCardFirst + initialBalanceCardSecond;
        assertEquals(totalInitial, totalCurrent, "Общий баланс изменился после теста!");

        int diffFirst = balanceFirst - initialBalanceCardFirst;
        int diffSecond = balanceSecond - initialBalanceCardSecond;

        if (diffFirst > 0) {
            // Переводим с первой карты, если баланс вырос
            dashBoardPage.replenishCard(DataHelper.getSecondCardInfo()); //нажать кнопку какую какую пополняем
            TransferPage transferPage = new TransferPage(DataHelper.getFirstCardInfo(), String.valueOf(diffFirst));
            transferPage.transferFunds();
        } else if (diffSecond > 0) {
            // Переводим со второй, если баланс вырос
            dashBoardPage.replenishCard(DataHelper.getFirstCardInfo());
            TransferPage transferPage = new TransferPage(DataHelper.getSecondCardInfo(), String.valueOf(diffSecond));
            transferPage.transferFunds();

        }
        assertAll(
                () -> assertEquals(initialBalanceCardFirst, dashBoardPage.getCardBalance(DataHelper.getFirstCardInfo()), "Баланс первой карты после восстановления"),
                () -> assertEquals(initialBalanceCardSecond, dashBoardPage.getCardBalance(DataHelper.getSecondCardInfo()), "Баланс второй карты после восстановления"));
    }

    @Test
    void shouldTransferToFirstCardFromSecondCard() {
//        DashBoardPage dashBoardPage = new DashBoardPage();
        dashBoardPage.replenishCard(DataHelper.getFirstCardInfo()); // нажимаем пополнить

        String amountStr = String.valueOf(initialBalanceCardSecond / 10); // сумма для перевода
        int amount = Integer.parseInt(amountStr);

        TransferPage pay = new TransferPage(DataHelper.getSecondCardInfo(), amountStr);
        pay.transferFunds();

        dashBoardPage.clickReload(); // обновляем страницу
        // Проверка балансов после перевода
        assertAll(
                () -> assertEquals(initialBalanceCardFirst + amount, dashBoardPage.getCardBalance(DataHelper.getFirstCardInfo()), "Баланс карты-цели после пополнения"),
                () -> assertEquals(initialBalanceCardSecond - amount, dashBoardPage.getCardBalance(DataHelper.getSecondCardInfo()), "Баланс карты-откуда после пополнения")
        );
    }

    @Test
    void shouldTransferToSecondCardFromFirstCard() {
        dashBoardPage.replenishCard(DataHelper.getSecondCardInfo()); // нажимаем пополнить вторую карту
        String amountStr = String.valueOf(initialBalanceCardFirst / 20); // сумма для перевода
        int amount = Integer.parseInt(amountStr);

        TransferPage pay = new TransferPage(DataHelper.getFirstCardInfo(), amountStr);
        pay.transferFunds();
        dashBoardPage.clickReload(); // обновляем страницу

        // Проверка балансов после перевода
        assertAll(
                () -> assertEquals(initialBalanceCardSecond + amount, dashBoardPage.getCardBalance(DataHelper.getSecondCardInfo()), "Баланс второй карты после пополнения"),
                () -> assertEquals(initialBalanceCardFirst - amount, dashBoardPage.getCardBalance((DataHelper.getFirstCardInfo())), "Баланс первой карты после пополнения"));

    }

}
