package org.example.producer;

// Продюсер делает только тогда, когда у сейлера 0
// Сейлер покупает только тогда, когда у него 0 и у продюсера 5
// Сейлер продает только тогда, когда у консьюмер 0 и у него пять
// Консьюмер покупает только тогда, когда у него 0 и у сейлера пять

// пирожки у продюсера - тригером 0 и у сейлера 0 пирожков
// пирожки у сейлера - тригер покупки - 5 у сейлера 0, тригер продажи - у сейлера 5, у консьюмера 0
// пирожки у консьюмера - тригер - 5 пирожков у сейлера.тригер покупки, когда у него 0




public class Market {
    public static void main(String[] args) {
        int count = 1;
        Producer producer = new Producer(count);
        Seller seller = new Seller(count);
        Consumer consumer = new Consumer(5);
        producer.setSeller(seller);
        seller.setConsumer(consumer);
        seller.setProducer(producer);
        consumer.setSeller(seller);

        producer.start();
        seller.start();
        consumer.start();
    }
}

class Producer extends Thread {
    private final Object lock = new Object();
    private int bread = 0;
    Seller seller;
    int count;

    public Producer(int count) {
        this.count = count;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setBread(int bread) {
        this.bread = bread;
    }

    public int getBread() {
        return bread;
    }

    public void wakeUpProd() {
        synchronized (lock) {
            lock.notify();
        }
    }

    void make() {
        StringBuilder stringBuilder = new StringBuilder();
        while (bread != 5) {
            bread++;
            stringBuilder.append("Producer make a bread. Quantity: ").append(bread).append("\n");
        }
        System.out.println(stringBuilder);
        System.out.println();
    }

    @Override
    public void run() {
        for (int i = count; i != 0; i--) {
            synchronized (lock) {
                make();
                seller.wakeUpSel();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

class Seller extends Thread {
    private final Object lock = new Object();
    Producer producer;
    Consumer consumer;

    private int bread = 0;
    int count;

    public Seller(int count) {
        this.count = count;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void setBread(int bread) {
        this.bread = bread;
    }

    public int getBread() {
        return bread;
    }

    public void wakeUpSel() {
        synchronized (lock) {
            lock.notify();
        }
    }

    void buy() {
        StringBuilder stringBuilder = new StringBuilder();
        while (bread != 5) {
            bread++;
            producer.setBread(producer.getBread() - 1);
            stringBuilder.append("Seller buy a bread. Quantity: ").append(bread).append(" Quantity at the Producer: ").append(producer.getBread()).append("\n");
        }
        System.out.println(stringBuilder);
        System.out.println();
    }

    @Override
    public void run() {
        for (int i = count; i != 0; i--) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                buy();
                consumer.wakeUpCons();
                producer.wakeUpProd();
            }
        }
    }
}

class Consumer extends Thread {
    private final Object lock = new Object();
    private int bread = 0;
    Seller seller;
    int count;

    public Consumer(int count) {
        this.count = count;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }


    public void wakeUpCons() {
        synchronized (lock) {
            lock.notify();
        }
    }

    void buy() {
        StringBuilder stringBuilder = new StringBuilder();
        while (bread != 5) {
            bread++;
            seller.setBread(seller.getBread() - 1);
            stringBuilder.append("Consumer buy a bread. Quantity: ").append(bread).append(" Quantity at the Seller: ").append(seller.getBread()).append("\n");
        }
        System.out.println(stringBuilder);
        System.out.println();
    }

    void eat() {
        StringBuilder stringBuilder = new StringBuilder();
        while (bread > 0) {
            bread--;
            stringBuilder.append("Consumer eat a bread. Quantity: ").append(bread).append("\n");
        }
        System.out.println(stringBuilder);
        System.out.println();
    }

    @Override
    public void run() {
        for (int i = count; i != 0; i--) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                buy();
                eat();
            }
        }
    }
}