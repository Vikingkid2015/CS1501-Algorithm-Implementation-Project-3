public class Car implements Comparable<Car>
{
    public String vin, make, model, color, make_model;
    public int price, mileage, make_model_index;
    private boolean sortByPrice;

    public Car(boolean sortByPrice)
    {
        this.sortByPrice = sortByPrice;
    }

    public String toString()
    {
        return "Vin: " + vin + "\n"
                + "Make: " + make + "\n"
                + "Model: " + model + "\n"
                + "Price: " + price + "\n"
                + "Mileage: " + mileage + "\n"
                + "Color: " + color;
    }

    public int compareTo(Car other)
    {
        if(sortByPrice)
        {
            return price - other.price;
        }
        else
        {
            return mileage - other.mileage;
        }
    }
}