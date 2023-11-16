package com.homework.one;

public class Transaction implements Runnable {
    private Account fromAccount;
    private Account toAccount;
    private int amount;

    public Transaction(Account fromAccount, Account toAccount, int amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    @Override
    public void run() {
        while (true) {
            if (fromAccount.tryLock()) {
                try {
                    if (toAccount.tryLock()) {
                        try {
                            System.out.println("Transferring $" + amount + " from " + fromAccount + " to " + toAccount);
                            System.out.println(
                                    "Before transfer: " + fromAccount + " has $" + fromAccount.getBalance() + ", "
                                            + toAccount + " has $" + toAccount.getBalance());

                            try {
                                Thread.sleep((long) (Math.random() * 9000) + 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            fromAccount.withdraw(amount);
                            toAccount.deposit(amount);

                            System.out.println(
                                    "After transfer: " + fromAccount + " has $" + fromAccount.getBalance() + ", "
                                            + toAccount + " has $" + toAccount.getBalance());
                            System.out.println();

                            return;
                        } finally {
                            toAccount.unlock();
                        }
                    }
                } finally {
                    fromAccount.unlock();
                }
            }

            // Sleep for a bit to ensure that other threads get a chance to acquire the
            // locks
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
