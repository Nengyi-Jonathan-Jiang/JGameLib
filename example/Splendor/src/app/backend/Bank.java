package app.backend;

public class Bank {
    private final Multiset<Gem> gemsInBank;
    private int goldAmount = 0;

    public Bank(Multiset<Gem> gemsInBank) {
        this.gemsInBank = gemsInBank;
    }

    public int getGemAmount(Gem gem) {
        return this.gemsInBank.count(gem);
    }

    public int getGoldAmount() {
        return this.goldAmount;
    }

    public void removeGem(Gem gem) {
        if (getGemAmount(gem) <= 0) {
            throw new RuntimeException("Cannot take gems when none are in bank");
        }
        this.gemsInBank.remove(gem);
    }

    public void removeGold() {
        if (getGoldAmount() <= 0) {
            throw new RuntimeException("Cannot take gold when none is in bank");
        }
        this.goldAmount--;
    }

    public void returnGem(Gem gem) {
        this.gemsInBank.add(gem);
    }
}
