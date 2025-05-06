package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class DashBoardPage {
    private final SelenideElement header = $("[data-test-id=dashboard]");

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

    public TransferPage selectCard(DataHelper.CardInfo cardInfo) {
        getCardInfo(cardInfo).$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        var balanceStart = "баланс: ";
        var balanceFinish = " р.";
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
