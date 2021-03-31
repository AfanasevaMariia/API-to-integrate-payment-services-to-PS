package mir.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * For Fit
 */
@Entity
@Table(name = "user_table")
public class User {
    @Id
    private String cardNumber;
    private String userName;
    private Double money;

    //region Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
    //endregion
}
