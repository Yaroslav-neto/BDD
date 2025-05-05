package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginHeader = $("h2");
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        verifyLoginHeader();
        enterCredentials(info);
        clickLoginButton();
        return new VerificationPage();
    }

    private void verifyLoginHeader() {
        loginHeader.shouldBe(Condition.visible);
        loginHeader.shouldHave(Condition.exactText("Интернет Банк"));
    }

    private void enterCredentials(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
    }

    private void clickLoginButton() {
        loginButton.click();
    }
}
