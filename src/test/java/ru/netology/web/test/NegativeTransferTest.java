package ru.netology.web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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
        dashBoardPage = verificationPage.validVerify(verificationCode);

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
            TransferPage transferPage = new TransferPage();
            transferPage.fillAmount(String.valueOf(diffFirst));
            transferPage.fillFromCard(DataHelper.getFirstCardInfo());
            transferPage.transferFunds();

        } else if (diffSecond > 0) {
            dashBoardPage.replenishCard(DataHelper.getFirstCardInfo());
            // Переводим со второй, если баланс вырос
            TransferPage transferPage = new TransferPage();
            transferPage.fillAmount(String.valueOf(diffSecond));
            transferPage.fillFromCard(DataHelper.getSecondCardInfo());
            transferPage.transferFunds();
        }

        assertAll(
                () -> assertEquals(initialBalanceCardFirst, dashBoardPage.getCardBalance(DataHelper.getFirstCardInfo()), "Баланс первой карты после восстановления"),
                () -> assertEquals(initialBalanceCardSecond, dashBoardPage.getCardBalance(DataHelper.getSecondCardInfo()), "Баланс второй карты после восстановления"));
    }

    @Test
    void shoulTransferToFirstCardTestOverInitialBalance() {
        dashBoardPage.replenishCard(DataHelper.getFirstCardInfo()); // нажимаем пополнить
        int amount = initialBalanceCardSecond + 10;

        TransferPage pay = new TransferPage();
        pay.fillAmount(String.valueOf(amount));
        pay.fillFromCard(DataHelper.getSecondCardInfo());
        pay.transferFunds();

        pay.popupErrorMessage();

        assertAll(
                () -> assertEquals(initialBalanceCardFirst, dashBoardPage.getCardBalance(DataHelper.getFirstCardInfo()),
                        "Баланс карты-цели после пополнения"),
                () -> assertEquals(initialBalanceCardSecond, dashBoardPage.getCardBalance(DataHelper.getSecondCardInfo()),
                        "Баланс карты-откуда после пополнения")
        );
    }

}

