package app.calcounterapp.com.ruby.EventBus;

public class SendCashEvent {

    private String amount;

    public SendCashEvent(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
