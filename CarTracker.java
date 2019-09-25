import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CarTracker
{
    // priority queue sorted by price
    IndexMinPQ<Car> priceQ = new IndexMinPQ<Car>(10);
    // priority queue sorted by mileage
    IndexMinPQ<Car> mileQ = new IndexMinPQ<Car>(10);
    // hash to store indices of cars
    LinearProbingHashST<String, Integer> hash = new LinearProbingHashST<String, Integer>();
    // counter for number of cars
    int numCars = 0;
    // scanner to read in user input
    Scanner scan = new Scanner(System.in);

    // hash to store priority queues by make and model
    LinearProbingHashST<String, IndexMinPQ> priceHashedPQ = new LinearProbingHashST<String, IndexMinPQ>();
    LinearProbingHashST<String, IndexMinPQ> mileHashedPQ = new LinearProbingHashST<String, IndexMinPQ>();

    public CarTracker() throws FileNotFoundException
    {
        // read in the cars.txt file so the initial listed cars are already added
        File file = new File("cars.txt");
        // if statement to ensure the file exists
        if(file.exists())
        {
            // if the file exists, then make a new scanner that will read the contents of the file
            Scanner reader = new Scanner(file);
            // while the file still has a next line
            while(reader.hasNextLine())
            {
                // write the line to a string variable
                String line = reader.nextLine();
                // if the first character is #, then ignore it, that line does not have car information
                if(line.startsWith("#"))
                {
                    continue;
                }
                // string array with elements separated by :
                String[] split = line.split(":");
                // addCar using the elements of the string array
                addCar(split[0], split[1], split[2], Integer.parseInt(split[3]), Integer.parseInt(split[4]), split[5]);
            }
            // close the reader scanner
            reader.close();
        }

        // continuous loop that runs until the program exits
        while(true)
        {
            // print the menu
            printMenu();
            // get the user inputted option
            int option = getOption();
            // if-else if block that will run the function coorespinding to the user's input
            // if user chooses option 1
            if(option == 1)
            {
                // run addCar
                addCar();
            }
            // if user chooses option 2
            else if(option == 2)
            {
                // run updateCar
                updateCar();
            }
            // if user chooses option 3
            else if(option == 3)
            {
                // run removeSpecCar
                removeSpecCar();
            }
            // if user chooses option 4
            else if(option == 4)
            {
                // run lowestPrice
                lowestPrice();
            }
            // if user chooses option 5
            else if(option == 5)
            {
                // run lowestMile
                lowestMile();
            }
            // if user chooses option 6
            else if(option == 6)
            {
                // run lowPMake
                lowPMake();
            }
            // if user chooses option 7
            else if(option == 7)
            {
                // run lowMMake
                lowMMake();
            }
            // if user chooses option 8
            else if(option == 8)
            {
                // exit the program
                // thank the user for using the program
                System.out.println("Thank you for using this application.");
                // tell them the program is being exitted
                System.out.println("Exit Program.");
                // break the loop, exitting the program
                break;
            }
            // if the user inputs something that is not one of the options
            else
            {
                // tell the user that they did not choose a valid option
                System.out.println("You did not choose a valid option.");
                System.out.println();
            }
        }
    }

    // addCar method that takes parameters
    public void addCar(String vin, String make, String model, int price, int mileage, String color)
    {
        // instantiate new Car objects for priceCar and mileCar
        // priceCar is sorted by price
        Car priceCar = new Car(true);
        // mileCar is not sorted by price
        Car mileCar = new Car(false);
        // make_model is a string that is used for hashing the priority queues
        String make_model = make + "$$" + model;

        // set all of priceCar's variables to be the parameters
        priceCar.vin = vin;
        priceCar.make = make;
        priceCar.model = model;
        priceCar.make_model = make_model;
        priceCar.price = price;
        priceCar.mileage = mileage;
        priceCar.color = color;

        // set all of mileCar's variables to be the parameters
        mileCar.vin = vin;
        mileCar.make = make;
        mileCar.model = model;
        mileCar.make_model = make_model;
        mileCar.price = price;
        mileCar.mileage = mileage;
        mileCar.color = color;

        // index is the counter before it is incremented
        int index = numCars;
        // increment counter
        numCars++;

        // is the hash of the price priority queue does not contain the make_model of this added car
        if(!priceHashedPQ.contains(make_model))
        {
            // hash a new queue with this make_model into the priceHashPQ
            priceHashedPQ.put(make_model, new IndexMinPQ<Car>(10));
        }
        // is the hash of the mileage priority queue does not contain the make_model of this added car
        if(!mileHashedPQ.contains(make_model))
        {
            // hash a new queue with this make_model into the mileHashPQ
            mileHashedPQ.put(make_model, new IndexMinPQ<Car>(10));
        }
        // now that we know that the hashPQs exist for this make_model, we can find the indices of this specific added car
        priceCar.make_model_index = priceHashedPQ.get(make_model).size();
        mileCar.make_model_index = mileHashedPQ.get(make_model).size();

        // insert this car into the pricePQ hashed into the priceHashPQ
        priceHashedPQ.get(make_model).insert(priceCar.make_model_index, priceCar);
        // insert this car into the milePQ hashed into the mileHashPQ
        mileHashedPQ.get(make_model).insert(mileCar.make_model_index, mileCar);
        // insert this car into the priceQ
        priceQ.insert(index, priceCar);
        // insert this car into the mileQ
        mileQ.insert(index, mileCar);
        // hash this car into the hash backed indirection
        hash.put(vin, index);
    }

    // addCar without parameters
    public void addCar()
    {
        // get user input for the varables of the car
        System.out.print("Enter VIN: ");
        String vin = scan.nextLine().trim();
        System.out.print("Enter Make: ");
        String make = scan.nextLine();
        System.out.print("Enter Model: ");
        String model = scan.nextLine();
        System.out.print("Enter Price: ");
        int price = scan.nextInt();
        scan.nextLine();
        System.out.print("Enter Mileage: ");
        int mileage = scan.nextInt();
        scan.nextLine();
        System.out.print("Enter Color: ");
        String color = scan.nextLine();
        // call the addCar method with parameters, using the user's input as the parameters
        addCar(vin, make, model, price, mileage, color);
    }

    //get the index of a car by providing its vin number
    public int getIndexWithVin()
    {
        // get user inputted vin number
        System.out.print("Enter VIN: ");
        String vin = scan.nextLine().trim();
        // if the hash does not contain that vin number
        if(!hash.contains(vin))
        {
            // tell the user that they did not provide a valid vin
            System.out.println("Not a valid VIN Number.");
            System.out.println();
            // return -1
            return -1;
        }
        // if the vin is contained then return the index hashed by the vin
        return hash.get(vin);
    }

    // update a car
    public void updateCar()
    {
        // get index with vin
        int index = getIndexWithVin();
        // if the user gave an invalid vin, then there is no car to update
        if(index == -1)
        {
            return;
        }
        // if the user's vin was valid, then get the car at that index from the priceQ and mileQ
        Car priceCar = priceQ.keyOf(index);
        Car mileCar = mileQ.keyOf(index);

        // print out menu of variables the user could update
        System.out.println("\n|--------Menu--------|");
        System.out.printf("|%-20s|\n", "1. Update Price");
        System.out.printf("|%-20s|\n", "2. Update Mileage");
        System.out.printf("|%-20s|\n", "3. Update Color");
        System.out.println("|--------------------|");
        System.out.println();
        // get the user's input
        int option = getOption();

        // if user inputted option 1
        if(option == 1)
        {
            // ask the user for a new price and save it to a variable
            System.out.print("Enter New Price: ");
            int price = scan.nextInt();
            scan.nextLine();
            // if the old price was greater than the new price, then the price has decreased, boolean decreased would be true, otherwise false
            boolean decreased = priceCar.price > price;
            // save the new price to priceCar and mileCar as their price
            priceCar.price = price;
            mileCar.price = price;
            // if the price decreased
            if(decreased)
            {
                // allow the queues to decrease the price and possibly change the position of the car in the queue
                priceQ.decreaseKey(index, priceCar);
                priceHashedPQ.get(priceCar.make_model).decreaseKey(priceCar.make_model_index, priceCar);
            }
            // if the price was not decreased
            else
            {
                // allow the queues to increase the price and possibly change the position of the car in the queue
                priceQ.increaseKey(index, priceCar);
                priceHashedPQ.get(priceCar.make_model).increaseKey(priceCar.make_model_index, priceCar);
            }
        }
        // is the user inputted option 2
        else if(option == 2)
        {
            // ask the user for the new mileage and save it to a variable
            System.out.print("Enter New Mileage: ");
            int mileage = scan.nextInt();
            scan.nextLine();
            // if the old mileage was greater than the new mileage, then the mileage has decreased, boolean decreased would be true, otherwise false
            boolean decreased = mileCar.mileage > mileage;
            // save the new mileage to the priceCar and mileCar as their mileage
            priceCar.mileage = mileage;
            mileCar.mileage = mileage;
            // if the mileage decreased
            if(decreased)
            {
                // allow the queues to decrease the mileage and possibly change the position of the car in the queue
                mileQ.decreaseKey(index, mileCar);
                mileHashedPQ.get(mileCar.make_model).decreaseKey(mileCar.make_model_index, mileCar);
            }
            // if the mileage was not decreased
            else
            {
                // allow the queues to increase the mileage and possibly change the position of the car in the queue
                mileQ.increaseKey(index, mileCar);
                mileHashedPQ.get(mileCar.make_model).increaseKey(mileCar.make_model_index, mileCar);
            }
        }
        // if the user inputted option 3
        else if(option == 3)
        {
            // ask the user for the new color
            System.out.print("Enter New Color: ");
            String color = scan.nextLine();
            // change the color varaible in the priceCar and mileCar
            priceCar.color = color;
            mileCar.color = color;
        }
        // if the user did not choose a valid option
        else
        {
            // tell the user they did not choose a valid option
            System.out.println("You did not choose a valid option.");
            System.out.println();
        }
        return;
    }

    // remove a specific car
    public void removeSpecCar()
    {
        // get the index of the car to be removed from the user
        int index = getIndexWithVin();
        // if the user did not enter a valid vin, then return
        if(index == -1)
        {
            return;
        }
        // get the make_model of the car to be removed
        String make_model = priceQ.keyOf(index).make_model;
        // get the make_model_index of the car to be removed
        int make_model_index = priceQ.keyOf(index).make_model_index;

        // delete the car to be removed from the hashedQs and the price and mile Qs
        priceHashedPQ.get(make_model).delete(make_model_index);
        mileHashedPQ.get(make_model).delete(make_model_index);
        priceQ.delete(index);
        mileQ.delete(index);
        return;
    }

    // find the car with the lowest price
    public void lowestPrice()
    {
        // if the priceQ is empty
        if(priceQ.isEmpty())
        {
            // then there have been no cars added yet
            System.out.println("No cars added.");
            return;
        }
        // if cars have been added, then just print the car at the min key of the priceQ, the lowest priced car
        System.out.println(priceQ.minKey());
        return;
    }

    // find the car with the lowest mileage
    public void lowestMile()
    {
        // if mileQ is empty
        if(mileQ.isEmpty())
        {
            // then there have been no cars added yet
            System.out.println("No cars added.");
            return;
        }
        // if cars have been added, then just print the car at the min key of the mileQ, the lowest mileage car
        System.out.println(mileQ.minKey());
        return;
    }

    // find the lowest priced car by a specific make and model
    public void lowPMake()
    {
        // get user inputted make and model
        System.out.print("Enter Make: ");
        String make = scan.nextLine();
        System.out.print("Enter Model: ");
        String model = scan.nextLine();
        System.out.println();
        // create make_model varable
        String make_model = make + "$$" + model;
        // if the priceHashedPQ contains a minIndexPQ for the specified make_model
        if(priceHashedPQ.contains(make_model))
        {
            // print the min key of the minIndexPQ for the make_model of the priceHashedPQ
            System.out.println(priceHashedPQ.get(make_model).minKey());
        }
        // if the priceHashedPQ does not have any minPQ for the specified make_model
        else
        {
            // then no cars of the specific make and model have been added yet
            System.out.println("No cars with specified Make and Model.");
        }
        return;
    }

    // find the lowest mileage car by a specific make and model
    public void lowMMake()
    {
        // get user inputted make and model
        System.out.print("Enter Make: ");
        String make = scan.nextLine();
        System.out.print("Enter Model: ");
        String model = scan.nextLine();
        System.out.println();
        // create make_model variable
        String make_model = make + "$$" + model;
        // if the mileHashedPQ contains a minIndexPQ for the specified make_model
        if(mileHashedPQ.contains(make_model))
        {
            // print the min key of the minIndexPQ for the make_model of the mileHashedPQ
            System.out.println(mileHashedPQ.get(make_model).minKey());
        }
        // if the mileHashedPQ does not have any minPQ for the specified make_model
        else
        {
            // then no cars of the specific make and model have been added yet
            System.out.println("No cars with specified Make and Model.");
        }
        return;
    }

    // print the main menu of the program
    public void printMenu()
    {
        System.out.println("\n|----------------------Menu----------------------|");
        System.out.printf("|%-48s|\n", "1. Add a Car");
        System.out.printf("|%-48s|\n", "2. Update Car");
        System.out.printf("|%-48s|\n", "3. Remove a Specific Car from Consideration");
        System.out.printf("|%-48s|\n", "4. Retrieve Lowest Price Car");
        System.out.printf("|%-48s|\n", "5. Retrieve Lowest Mileage Car");
        System.out.printf("|%-48s|\n", "6. Retrieve Lowest Price Car by Make and Model");
        System.out.printf("|%-48s|\n", "7. Retrieve Lowest Mileage Car by Make and Model");
        System.out.printf("|%-48s|\n", "8. Exit Program");
        System.out.println("|------------------------------------------------|");
        System.out.println();
    }

    // get the user inputted option they chose from the menu
    public int getOption()
    {
        System.out.print("Select Option: ");
        int ret = scan.nextInt();
        scan.nextLine();
        System.out.println();
        return ret;
    }

    // main(), make a new CarTracker()
    public static void main(String[] args) throws FileNotFoundException
    {
        new CarTracker();
    }
}