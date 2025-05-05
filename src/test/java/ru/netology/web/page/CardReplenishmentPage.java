package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class CardReplenishmentPage {
    private SelenideElement cardReplenishmentHeader = $("h1");
    private SelenideElement amountField = $("[data-test-id='amount'] input");  // Поле ввода суммы
    private SelenideElement payFromField = $("[data-test-id='from'] input"); // Поле "откуда" - номер карты
    private SelenideElement replenishmentButton = $("[data-test-id='action-transfer']"); // Кнопка пополнить
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']"); // Кнопка отмена

    private DataHelper.CardInfo cardFrom;
    private String amount;

    public CardReplenishmentPage(DataHelper.CardInfo cardFrom, String amount) {
        this.cardFrom = cardFrom;
        this.amount = amount;
        cardReplenishmentHeader.shouldHave(Condition.exactText("Пополнение карты")).shouldBe(Condition.visible); // Проверяем заголовок
    }

    public void fillFromCard(DataHelper.CardInfo otherCard) { // Метод для заполнения поля "откуда" другой картой
        payFromField.shouldBe(Condition.visible).setValue(otherCard.getNumber());
    }

    public void transferFunds() {
        amountField.shouldBe(Condition.visible).setValue(amount); // Убедитесь, что поле видно перед вводом
        replenishmentButton.shouldBe(Condition.visible).click(); // Нажимаем кнопку "Перевести"
    }

    public void clearFromCardField() {
        payFromField.shouldBe(Condition.visible); // Убедитесь, что поле видно перед очисткой
        payFromField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE); // Очистка поля
    }

    public void clearAmountField() {
        amountField.shouldBe(Condition.visible); // Убедитесь, что поле видно перед очисткой
        amountField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE); // Очистка поля
    }

    public void clickCancelButton() {
        cancelButton.shouldBe(Condition.visible).click(); // Ждем, пока кнопка станет видимой, и затем нажимаем
    }

    public void fillAmount(String amount) {
        amountField.shouldBe(Condition.visible).setValue(amount); // Убедитесь, что поле видно перед вводом
    }
}

