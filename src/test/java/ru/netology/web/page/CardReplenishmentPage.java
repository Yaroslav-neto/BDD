package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class CardReplenishmentPage {
    private SelenideElement cardReplenishmentHeader = $("h1");
    private SelenideElement amountField = $("[data-test-id='amount'] input");  //поле ввод суммы
    private SelenideElement payFromField = $("[data-test-id='from'] input"); //поле откуда-номер карты
    private SelenideElement replenishmentButton = $("[data-test-id='action-transfer']");//кнопка пополнить
    private SelenideElement cancelButton = $("[data-test-id='action-cancel'] button");// кноапка отмена

    private DataHelper.CardInfo cardFrom;
    private String amount;

    public CardReplenishmentPage(DataHelper.CardInfo cardFrom, String amount) {
        this.cardFrom = cardFrom;
        this.amount = amount;
        this.cardReplenishmentHeader.should(Condition.exactText("Пополнение карты"));// Проверяем, что заголовок страницы "Личный кабинет" и видим
        this.cardReplenishmentHeader.shouldBe(Condition.visible);
    }

    public CardReplenishmentPage() {
    }

    public void fillFromCard(DataHelper.CardInfo otherCard) {// Метод для заполнения поля "откуда" другой картой
        payFromField.setValue(otherCard.getNumber());
    }

    public void transferFunds() {
        amountField.setValue(amount);
        replenishmentButton.click();
    }

    public void clearFromCardField() {
        // Очистка поля через комбинацию клавиш Ctrl+A и Backspace
        payFromField.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Выделяем все
        payFromField.sendKeys(Keys.BACK_SPACE); // Удаляем выделенное
    }

    public void clearAmountField() {
        // Очистка поля через комбинацию клавиш Ctrl+A и Backspace
        amountField.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Выделяем все
        amountField.sendKeys(Keys.BACK_SPACE); // Удаляем выделенное
    }

}

