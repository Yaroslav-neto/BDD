package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class DashBoardPage {
    private SelenideElement header = $("[data-test-id=dashboard]");
    private SelenideElement reloadButton = $("[data-test-id='action-reload']");//кнопка "обновить"

    public DashBoardPage() {
        header.shouldBe(Condition.visible);
    }

    private SelenideElement getCardInfo(DataHelper.CardInfo cardInfo) {
        return $("div[data-test-id='" + cardInfo.getTestId() + "']");
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var cardElement = getCardInfo(cardInfo);
        return extractBalance(cardElement.getText());
    }

    private int extractBalance(String text) {
        var balanceStart = "баланс: ";
        var balanceFinish = " р.";
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    // кнопка пополнить указанную карту
    public void replenishCard(DataHelper.CardInfo cardInfo) {
        $("[data-test-id='" + cardInfo.getTestId() + "'] button").click();
    }

    public void clickReload() {// Кнопка для обновления баланса
        reloadButton.shouldBe(Condition.visible);
        reloadButton.click();
    }
}
