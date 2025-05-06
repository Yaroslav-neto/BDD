package ru.netology.web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class NegativeTransferTest {
    private int initialBalanceCardFirst;
    private int initialBalanceCardSecond;

    private DashBoardPage dashBoardPage;


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
            TransferPage.replenishCard(DataHelper.getSecondCardInfo()); //нажать кнопку какую какую пополняем

            TransferPage transferPage = new TransferPage(DataHelper.getSecondCardInfo(), String.valueOf(diffFirst));

            transferPage.fillFromCard(DataHelper.getFirstCardInfo());
            transferPage.transferFunds();
        } else if (diffSecond > 0) {
            TransferPage.replenishCard(DataHelper.getFirstCardInfo());
            // Переводим со второй, если баланс вырос
            TransferPage transferPage = new TransferPage(DataHelper.getSecondCardInfo(), String.valueOf(diffSecond));
            transferPage.fillFromCard(DataHelper.getSecondCardInfo());
            transferPage.transferFunds();

        }
        assertAll(
                () -> assertEquals(initialBalanceCardFirst, dashBoardPage.getCardBalance(DataHelper.getFirstCardInfo()), "Баланс первой карты после восстановления"),
                () -> assertEquals(initialBalanceCardSecond, dashBoardPage.getCardBalance(DataHelper.getSecondCardInfo()), "Баланс второй карты после восстановления"));
    }

    @Test
    void shoulTransferToFirstCardTestOverInitialBalance() {
        DashBoardPage dashBoardPage = new DashBoardPage();

        var cardFirst = DataHelper.getFirstCardInfo(); // карта, на которую переводим
        TransferPage.replenishCard(cardFirst); // нажимаем пополнить
        DataHelper.CardInfo cardFrom = DataHelper.getSecondCardInfo(); // карта "откуда"
        String amountStr = String.valueOf(initialBalanceCardSecond + 10); // сумма больше баланса
        int amount = Integer.parseInt(amountStr);

        TransferPage pay = new TransferPage(cardFrom, amountStr);
        pay.fillFromCard(cardFrom);
        pay.fillAmount(amountStr);

        boolean exceptionThrown = false;

        try {
            pay.transferFunds();
        } catch (RuntimeException e) {
            exceptionThrown = true;

            assertTrue(e.getMessage().contains("Превышен допустимый баланс"), "Сообщение исключения не соответствует");
        }

        // Проверяем, выбросилось ли исключение — если нет, то фиксируем ошибку
        if (!exceptionThrown) {
            fail("Ожидалось исключение, но оно не было выброшено");
        }

        assertAll(
                () -> assertEquals(initialBalanceCardFirst, dashBoardPage.getCardBalance(DataHelper.getFirstCardInfo()),
                        "Баланс карты-цели после пополнения"),
                () -> assertEquals(initialBalanceCardSecond - amount, dashBoardPage.getCardBalance(cardFrom),
                        "Баланс карты-откуда после пополнения")
        );
    }

}

