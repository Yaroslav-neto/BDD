package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.*;

public class UnregistredTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldLoginUnregisteredUser() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var otherAuthInfo = DataHelper.getOtherAuthInfo(authInfo);
        var verificationPage = loginPage.validLogin(otherAuthInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(otherAuthInfo);
        verificationPage.validVerify(verificationCode);

        var transferPage = new TransferPage();
        transferPage.getReloadButton();
        $$(By.cssSelector("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']")).shouldHave(size(0)); // Проверка отсутствия элемента
        $$(By.cssSelector("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']")).shouldHave(size(0)); // Проверка отсутствия элемента
    }
}
