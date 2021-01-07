import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.*;
import java.util.function.Supplier;
public class Customer implements Runnable {
    private Bakery bakery;
    private Random rnd = new Random();
    private List<BreadType> shoppingCart = new ArrayList<BreadType>();
    private int shopTime;
    private int checkoutTime;

    /**
     * Initialize a customer object and randomize its shopping cart
     */
    public Customer(Bakery bakery) {
        // TODO
        this.bakery = bakery;
        this.fillShoppingCart();
        this.shopTime = rnd.nextInt(400) + 100;
        this.checkoutTime = rnd.nextInt(400) + 100;
        //random number 100 ms to 500 ms
        
    }

    /**
     * Run tasks for the customer
     */
    public void run() {
        System.out.println(this.toString() + " has started shopping.");
        try{
           
            for (BreadType bread : this.shoppingCart){
                    //check if the shelf is available
                    //check if the bread is available done in take bread
                
                if (bread.ordinal() == 0){
                        //check if shelf rye is available
                    
                    this.bakery.rye.acquire();
                    //System.out.println("Rye shelf acquired");
                    Thread.sleep(this.shopTime);
                        //CS
                    this.bakery.takeBread(bread);
                    System.out.println(this.toString() + " has picked up rye bread");
                        // CS
                    this.bakery.rye.release();
                    //System.out.println("Rye shelf released");
                    }
                    //ask if this statement is correct
                else if (bread.ordinal() == 1) {
                    
                    this.bakery.sourdough.acquire();
                    //System.out.println("sourdough shelf acquired");
                    Thread.sleep(this.shopTime);
                        // CS

                    this.bakery.takeBread(bread);
                    System.out.println(this.toString() + " has picked up sourdough bread");
                        // CS
                    this.bakery.sourdough.release();
                    //System.out.println("sourdough shelf released");
                    }
                else if (bread.ordinal() == 2){
                    
                    this.bakery.wonder.acquire();
                    //System.out.println("wonder shelf acquired");
                    Thread.sleep(this.shopTime);
                        // CS
                    this.bakery.takeBread(bread);
                    System.out.println(this.toString() + " has picked up wonder bread");
                        // CS
                    this.bakery.wonder.release();
                    //System.out.println("wonder shelf released");
                    }
                }

                // the customer has taken their bread and now will go to the register to pay
                this.bakery.registers.acquire();
                Thread.sleep(this.checkoutTime);

                System.out.println("Customer " + this.hashCode() + " is currently buying bread. ");
                this.bakery.mutex_register.acquire();
                    // CS because of the shared variable sales
                this.bakery.addSales(this.getItemsValue());
                    // CS
                this.bakery.mutex_register.release();
               

                this.bakery.registers.release();

                System.out.println("Customer " + this.hashCode() + " is leaving the store. ");


                } //try 

                catch(InterruptedException e){
                    System.err.println(e);
                }
                         
    }

    /**
     * Return a string representation of the customer
     */
    public String toString() {
        return "Customer " + hashCode() + ": shoppingCart=" + Arrays.toString(shoppingCart.toArray()) + ", shopTime=" + shopTime + ", checkoutTime=" + checkoutTime;
    }

    /**
     * Add a bread item to the customer's shopping cart
     */
    private boolean addItem(BreadType bread) {
        // do not allow more than 3 items, chooseItems() does not call more than 3 times
        if (shoppingCart.size() >= 3) {
            return false;
        }
        shoppingCart.add(bread);
        return true;
    }

    /**
     * Fill the customer's shopping cart with 1 to 3 random breads
     */
    private void fillShoppingCart() {
        int itemCnt = 1 + rnd.nextInt(3);
        while (itemCnt > 0) {
            addItem(BreadType.values()[rnd.nextInt(BreadType.values().length)]);
            itemCnt--;
        }
    }

    /**
     * Calculate the total value of the items in the customer's shopping cart
     */
    private float getItemsValue() {
        float value = 0;
        for (BreadType bread : shoppingCart) {
            value += bread.getPrice();
        }
        return value;
    }
}